package no.example.weather.spond.exceptionhandlers;

public class WeatherApiException extends RuntimeException {
    public WeatherApiException(String message, Throwable cause) {
        super(message, cause);
    }
}