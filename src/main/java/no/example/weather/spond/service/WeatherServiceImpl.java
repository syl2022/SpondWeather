package no.example.weather.spond.service;

import com.github.benmanes.caffeine.cache.Cache;
import no.example.weather.spond.configuration.Helper;
import no.example.weather.spond.exceptionhandlers.WeatherApiException;
import no.example.weather.spond.model.CachedWeather;
import no.example.weather.spond.model.Event;
import no.example.weather.spond.model.WeatherData;
import no.example.weather.spond.model.WeatherResponse;
import no.example.weather.spond.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Value("${weather.api.user-agent}")
    private String USER_AGENT = "";

    @Value("${weather.api.base-url}")
    private String BASE_URL = "";

    @Autowired
    private WebClient.Builder webClientBuilder;

    private WebClient webClient;
    private Cache<String, CachedWeather> weatherCache;

    @Autowired
    private EventRepository eventRepository;

    public WeatherServiceImpl(Cache<String, CachedWeather> weatherCache, EventRepository eventRepository) {
        this.weatherCache = weatherCache;
        this.eventRepository = eventRepository;
    }

    @Override
    public WeatherData getWeatherData(String eventId) {
        Event event = eventRepository.getEventByEventId(eventId);
        if (event == null) {
            throw new IllegalArgumentException("Event not found with ID: " + eventId);
        }
        if (event.getEndTime().isBefore(Instant.now()) || event.getStartTime().isAfter(Instant.now().plus(Duration.ofDays(7))))
            return null;
        // Create cache key based on event's location
        String cacheKey = event.getLatitude() + "," + event.getLongitude();

        // Retrieve weather data from cache or fetch if not available
        CachedWeather cachedWeather = weatherCache.get(cacheKey, key -> fetchWeatherData(event.getLatitude(), event.getLongitude()));

        // Check if the cached data is outdated, if so, re-fetch and update the cache
        if (Instant.now().isAfter(cachedWeather.getExpiresAt())) {
            cachedWeather = fetchWeatherData(event.getLatitude(), event.getLongitude());
            weatherCache.put(cacheKey, cachedWeather);
        }

        // Find the closest weather data to the event's time window
        WeatherResponse.Data eventWeather = findClosestTimeSeries(cachedWeather.getTimeseries(), event.getStartTime(), event.getEndTime()).getData();

        return new WeatherData(eventId, eventWeather.getInstant().getDetails().getAir_temperature(), eventWeather.getInstant().getDetails().getWind_speed(), eventWeather.getNext_1_hours() != null ? eventWeather.getNext_1_hours().getSummary().getSymbol_code() : eventWeather.getNext_12_hours().getSummary().getSymbol_code());
    }

    @Override
    public CachedWeather fetchWeatherData(double lat, double lon) {

        webClient = webClientBuilder
                .defaultHeader("User-Agent", USER_AGENT)
                .baseUrl(BASE_URL)
                .build();

        String uri = String.format("?lat=%s&lon=%s", lat, lon);
        try {
            // Perform the API request and block to get the response synchronously
            ResponseEntity<WeatherResponse> responseEntity = webClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .toEntity(WeatherResponse.class)
                    .block();  // Blocking for simplicity, consider async if non-blocking is needed

            // Check for a successful response and ensure it's not null
            if (responseEntity == null || !responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new WeatherApiException("Failed to fetch weather data, response is null or unsuccessful", null);
            }

            WeatherResponse weatherResponse = responseEntity.getBody();
            String expiresHeader = responseEntity.getHeaders().getFirst("Expires");

            // Parse the 'Expires' header to get the expiration time of the data
            Instant fetchedAt = Helper.convertToInstantFromRFC(expiresHeader);

            // Return the fetched weather data wrapped in CachedWeather
            return new CachedWeather(weatherResponse.getProperties().getTimeseries(), fetchedAt);

        } catch (Exception e) {
            e.printStackTrace();
            throw new WeatherApiException("Failed to fetch weather data", e.getCause());
        }
    }

    private WeatherResponse.Timeseries findClosestTimeSeries(List<WeatherResponse.Timeseries> timeSeries, Instant startTime, Instant endTime) {
        Instant midpoint = startTime.plus(Duration.between(startTime, endTime).dividedBy(2));

        WeatherResponse.Timeseries closest = null;
        long minDifference = Long.MAX_VALUE;

        // Loop through all timeseries entries to find the closest one
        for (WeatherResponse.Timeseries ts : timeSeries) {
            long difference = Math.abs(Duration.between(ts.getTime(), midpoint).toSeconds());

            if (difference < minDifference) {
                minDifference = difference;
                closest = ts;
            }
        }

        // If no closest timeseries was found, throw an exception
        if (closest == null) {
            throw new WeatherApiException("No suitable weather data found", null);
        }
        return closest;
    }
}
