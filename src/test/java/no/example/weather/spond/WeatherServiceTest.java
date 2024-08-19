package no.example.weather.spond;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.benmanes.caffeine.cache.Cache;
import no.example.weather.spond.configuration.Helper;
import no.example.weather.spond.exceptionhandlers.WeatherApiException;
import no.example.weather.spond.model.CachedWeather;
import no.example.weather.spond.model.Event;
import no.example.weather.spond.model.WeatherData;
import no.example.weather.spond.model.WeatherResponse;
import no.example.weather.spond.repository.EventRepository;
import no.example.weather.spond.service.WeatherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.TimeZone;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WeatherServiceTest {

    @Mock
    private Cache<String, CachedWeather> weatherCache;

    @Mock
    private EventRepository eventRepository;

    @Autowired
    private WeatherServiceImpl weatherService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
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

        Instant currentInstant = Instant.parse("2024-08-12T10:00:00Z");

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class, invocation -> invocation.callRealMethod())) {
            mockedStatic.when(Instant::now).thenReturn(currentInstant);

            // Prepare mock weather response
            WeatherResponse weatherResponse = loadWeatherResponseFromFile("response.json");
            CachedWeather cachedWeather = new CachedWeather(weatherResponse.getProperties().getTimeseries(), Instant.now().plus(Duration.ofHours(1)));

            when(eventRepository.getEventByEventId("event_001")).thenReturn(event);
            when(weatherCache.get(anyString(), any())).thenReturn(cachedWeather);

            // Execute with the spy object to avoid calling the real method
            WeatherData result = weatherService.getWeatherData("event_001");

            // Verify
            assertNotNull(result);
            assertEquals(29.5, result.getTemperature());
            assertEquals(1.4, result.getWindSpeed());
            assertEquals("rain", result.getForcast());
        }
    }

    @Test
    public void testGetWeatherDataEventNotFound() {
        // Mock the repository to return null for the event
        when(eventRepository.getEventByEventId("non_existent_event")).thenReturn(null);

        // Execute and verify that an exception is thrown
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            weatherService.getWeatherData("non_existent_event");
        });

        assertEquals("Event not found with ID: non_existent_event", exception.getMessage());
    }

    @Test
    public void testFetchWeatherDataApiFailure(){
        // Prepare mock event
        Event event = new Event();
        event.setEventId("event_001");
        event.setLatitude(28.6139);
        event.setLongitude(77.2090);
        event.setStartTime(Helper.convertToInstant("2024-08-15 09:00:00"));
        event.setEndTime(Helper.convertToInstant("2024-08-15 11:00:00"));

        Instant instantExpected = Instant.parse("2024-08-12T10:00:00Z");
        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class, invocation -> invocation.callRealMethod())) {
            mockedStatic.when(Instant::now).thenReturn(instantExpected);
            when(eventRepository.getEventByEventId("event_001")).thenReturn(event);

            // Mock the cache to return cachedWeather
            when(weatherCache.get(anyString(), any())).thenThrow(new WeatherApiException("API Failure", null));

            // Execute and verify that the exception is thrown
            Exception exception = assertThrows(WeatherApiException.class, () -> {
                weatherService.getWeatherData("event_001");
            });

            assertEquals("API Failure", exception.getMessage());
        }
    }


    /*TODO: Add more test cases*/
}
