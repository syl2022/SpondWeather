package no.example.weather.spond.controller;

import no.example.weather.spond.model.WeatherData;
import no.example.weather.spond.service.WeatherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spond")
public class WeatherController {

    @Autowired
    private WeatherServiceImpl weatherService;

    @GetMapping("/weather")
    public ResponseEntity getWeather(@RequestParam String eventId) {

        WeatherData weatherData = weatherService.getWeatherData(eventId);

        return ResponseEntity.status(HttpStatus.OK).body(weatherData);
    }
}
