package no.example.weather.spond.service;

import no.example.weather.spond.model.CachedWeather;
import no.example.weather.spond.model.WeatherData;

public interface WeatherService {

    public WeatherData getWeatherData(String eventId);

    public CachedWeather fetchWeatherData(double lat, double lon);

}
