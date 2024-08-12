package no.example.weather.spond.configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import no.example.weather.spond.model.CachedWeather;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
public class WeatherConfig {

    private static final Duration CACHE_DURATION = Duration.ofHours(2);

    @Bean
    public Cache<String, CachedWeather> cacheBean() {
        return Caffeine.newBuilder()
                .expireAfterWrite(CACHE_DURATION)
                .maximumSize(1000)
                .build();
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

}
