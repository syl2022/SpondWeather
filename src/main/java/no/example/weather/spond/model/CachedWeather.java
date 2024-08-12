package no.example.weather.spond.model;

import java.time.Instant;
import java.util.ArrayList;

public class CachedWeather {
    private final ArrayList<WeatherResponse.Timeseries> timeseries;
    private final Instant expiresAt;

    public CachedWeather(ArrayList<WeatherResponse.Timeseries> timeseries, Instant expiresAt) {
        this.timeseries = timeseries;
        this.expiresAt = expiresAt;
    }

    public ArrayList<WeatherResponse.Timeseries> getTimeseries() {
        return timeseries;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}