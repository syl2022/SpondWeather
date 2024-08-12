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

@SpringBootTest
public class WeatherServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private Cache<String, CachedWeather> weatherCache;

    @Mock
    private EventRepository eventRepository;

    @Autowired
    private WeatherServiceImpl weatherService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // Initialize WebClient and other beans
        Mockito.when(webClientBuilder.build()).thenReturn(webClient);
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

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
        // Convert JSON string to WeatherResponse object
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

        // Create a spy for WeatherService
        WeatherServiceImpl spyService = Mockito.spy(weatherService);

        // Stub the fetchWeatherData method
        Mockito.doReturn(cachedWeather)
                .when(spyService)
                .fetchWeatherData(Mockito.anyDouble(), Mockito.anyDouble());

        // Mock repository and cache
        Mockito.when(eventRepository.getEventByEventId("event_001")).thenReturn(event);

        // Execute
        WeatherData result = spyService.getWeatherData("event_001");

        // Verify
        assertNotNull(result);
        assertEquals(29.5, result.getTemperature());
        assertEquals(1.4, result.getWindSpeed());
        assertEquals("rain", result.getForcast());
    }
}
