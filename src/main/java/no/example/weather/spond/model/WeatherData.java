package no.example.weather.spond.model;

public class WeatherData {

    private String eventId;
    private double temperature; //This should be string to incorporate unit(celcius)
    private double windSpeed; //This should be string to incorporate unit(m/s)
    private String forcast;

    public WeatherData(String eventId, double temperature, double windSpeed, String forcast) {
        this.eventId = eventId;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.forcast = forcast;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getForcast() {
        return forcast;
    }

    public void setForcast(String forcast) {
        this.forcast = forcast;
    }
}
