package no.example.weather.spond;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.benmanes.caffeine.cache.Cache;
import no.example.weather.spond.configuration.Helper;
import no.example.weather.spond.model.CachedWeather;
import no.example.weather.spond.model.Event;
import no.example.weather.spond.model.WeatherData;
import no.example.weather.spond.model.WeatherResponse;
import no.example.weather.spond.repository.EventRepository;
import no.example.weather.spond.service.WeatherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WeatherServiceTest {

    @Mock
    private Cache<String, CachedWeather> weatherCache;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Autowired
    private WeatherServiceImpl weatherService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        weatherService = new WeatherServiceImpl(weatherCache, eventRepository); // Manually instantiate with mocks
    }

    public static WeatherResponse loadWeatherResponseFromFile(String filePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(filePath);
        StringBuilder fileContents = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContents.append(line).append("\n");
            }
        }
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(fileContents.toString(), WeatherResponse.class);
    }

    @Test
    public void testGetWeatherData() throws IOException {
        // Prepare mock event
        Event event = new Event();
        event.setEventId("event_001");
        event.setLatitude(28.6139);
        event.setLongitude(77.2090);
        event.setStartTime(Helper.convertToInstant("2024-08-15 09:00:00"));
        event.setEndTime(Helper.convertToInstant("2024-08-15 11:00:00"));

        // Prepare mock weather response
        WeatherResponse weatherResponse = loadWeatherResponseFromFile("response.json");

        CachedWeather cachedWeather = new CachedWeather(weatherResponse.getProperties().getTimeseries(), Instant.now().plus(Duration.ofHours(1)));

        // Spy on the weatherService to mock fetchWeatherData
        WeatherServiceImpl spyService = Mockito.spy(weatherService);
        Mockito.doReturn(cachedWeather)
                .when(spyService)
                .fetchWeatherData(Mockito.anyDouble(), Mockito.anyDouble());

        // Mock repository to return the event
        when(eventRepository.getEventByEventId("event_001")).thenReturn(event);

        // Mock the cache to return cachedWeather
        when(weatherCache.get(anyString(), any())).thenReturn(cachedWeather);

        // Execute with the spy object to avoid calling the real method
        WeatherData result = spyService.getWeatherData("event_001");

        // Verify
        assertNotNull(result);
        assertEquals(29.1, result.getTemperature());
        assertEquals(1.6, result.getWindSpeed());
        assertEquals("rain", result.getForcast());
    }
}
