package no.example.weather.spond.model;

import java.util.ArrayList;
import java.util.Date;


public class WeatherResponse {
    private String type;
    private Geometry geometry;
    private Properties properties;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public static class Properties {
        private Meta meta;
        private ArrayList<Timeseries> timeseries;

        public Meta getMeta() {
            return meta;
        }

        public void setMeta(Meta meta) {
            this.meta = meta;
        }

        public ArrayList<Timeseries> getTimeseries() {
            return timeseries;
        }

        public void setTimeseries(ArrayList<Timeseries> timeseries) {
            this.timeseries = timeseries;
        }
    }

    public static class Geometry {
        private String type;
        private ArrayList<Double> coordinates;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public ArrayList<Double> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(ArrayList<Double> coordinates) {
            this.coordinates = coordinates;
        }
    }

    public static class Data {
        private Instant instant;
        private Next12Hours next_12_hours;
        private Next1Hours next_1_hours;
        private Next6Hours next_6_hours;

        public Instant getInstant() {
            return instant;
        }

        public void setInstant(Instant instant) {
            this.instant = instant;
        }

        public Next12Hours getNext_12_hours() {
            return next_12_hours;
        }

        public void setNext_12_hours(Next12Hours next_12_hours) {
            this.next_12_hours = next_12_hours;
        }

        public Next1Hours getNext_1_hours() {
            return next_1_hours;
        }

        public void setNext_1_hours(Next1Hours next_1_hours) {
            this.next_1_hours = next_1_hours;
        }

        public Next6Hours getNext_6_hours() {
            return next_6_hours;
        }

        public void setNext_6_hours(Next6Hours next_6_hours) {
            this.next_6_hours = next_6_hours;
        }
    }

    public static class Details {
        private double air_pressure_at_sea_level;
        private double air_temperature;
        private double cloud_area_fraction;
        private double relative_humidity;
        private double wind_from_direction;
        private double wind_speed;
        private double precipitation_amount;

        public double getAir_pressure_at_sea_level() {
            return air_pressure_at_sea_level;
        }

        public void setAir_pressure_at_sea_level(double air_pressure_at_sea_level) {
            this.air_pressure_at_sea_level = air_pressure_at_sea_level;
        }

        public double getAir_temperature() {
            return air_temperature;
        }

        public void setAir_temperature(double air_temperature) {
            this.air_temperature = air_temperature;
        }

        public double getCloud_area_fraction() {
            return cloud_area_fraction;
        }

        public void setCloud_area_fraction(double cloud_area_fraction) {
            this.cloud_area_fraction = cloud_area_fraction;
        }

        public double getRelative_humidity() {
            return relative_humidity;
        }

        public void setRelative_humidity(double relative_humidity) {
            this.relative_humidity = relative_humidity;
        }

        public double getWind_from_direction() {
            return wind_from_direction;
        }

        public void setWind_from_direction(double wind_from_direction) {
            this.wind_from_direction = wind_from_direction;
        }

        public double getWind_speed() {
            return wind_speed;
        }

        public void setWind_speed(double wind_speed) {
            this.wind_speed = wind_speed;
        }

        public double getPrecipitation_amount() {
            return precipitation_amount;
        }

        public void setPrecipitation_amount(double precipitation_amount) {
            this.precipitation_amount = precipitation_amount;
        }
    }

    public static class Instant {
        private Details details;

        public Details getDetails() {
            return details;
        }

        public void setDetails(Details details) {
            this.details = details;
        }
    }

    public static class Meta {
        private Date updated_at;
        private Units units;

        public Date getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(Date updated_at) {
            this.updated_at = updated_at;
        }

        public Units getUnits() {
            return units;
        }

        public void setUnits(Units units) {
            this.units = units;
        }
    }

    public static class Next12Hours {
        private Summary summary;
        private Details details;

        public Summary getSummary() {
            return summary;
        }

        public void setSummary(Summary summary) {
            this.summary = summary;
        }

        public Details getDetails() {
            return details;
        }

        public void setDetails(Details details) {
            this.details = details;
        }
    }

    public static class Next1Hours {
        private Summary summary;
        private Details details;

        public Summary getSummary() {
            return summary;
        }

        public void setSummary(Summary summary) {
            this.summary = summary;
        }

        public Details getDetails() {
            return details;
        }

        public void setDetails(Details details) {
            this.details = details;
        }
    }

    public static class Next6Hours {
        private Summary summary;
        private Details details;

        public Summary getSummary() {
            return summary;
        }

        public void setSummary(Summary summary) {
            this.summary = summary;
        }

        public Details getDetails() {
            return details;
        }

        public void setDetails(Details details) {
            this.details = details;
        }
    }

    public static class Summary {
        private String symbol_code;

        public String getSymbol_code() {
            return symbol_code;
        }

        public void setSymbol_code(String symbol_code) {
            this.symbol_code = symbol_code;
        }
    }

    public static class Timeseries {
        private java.time.Instant time;
        private Data data;

        public java.time.Instant getTime() {
            return time;
        }

        public void setTime(java.time.Instant time) {
            this.time = time;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    public static class Units {
        private String air_pressure_at_sea_level;
        private String air_temperature;
        private String cloud_area_fraction;
        private String precipitation_amount;
        private String relative_humidity;
        private String wind_from_direction;
        private String wind_speed;

        public String getAir_pressure_at_sea_level() {
            return air_pressure_at_sea_level;
        }

        public void setAir_pressure_at_sea_level(String air_pressure_at_sea_level) {
            this.air_pressure_at_sea_level = air_pressure_at_sea_level;
        }

        public String getAir_temperature() {
            return air_temperature;
        }

        public void setAir_temperature(String air_temperature) {
            this.air_temperature = air_temperature;
        }

        public String getCloud_area_fraction() {
            return cloud_area_fraction;
        }

        public void setCloud_area_fraction(String cloud_area_fraction) {
            this.cloud_area_fraction = cloud_area_fraction;
        }

        public String getPrecipitation_amount() {
            return precipitation_amount;
        }

        public void setPrecipitation_amount(String precipitation_amount) {
            this.precipitation_amount = precipitation_amount;
        }

        public String getRelative_humidity() {
            return relative_humidity;
        }

        public void setRelative_humidity(String relative_humidity) {
            this.relative_humidity = relative_humidity;
        }

        public String getWind_from_direction() {
            return wind_from_direction;
        }

        public void setWind_from_direction(String wind_from_direction) {
            this.wind_from_direction = wind_from_direction;
        }

        public String getWind_speed() {
            return wind_speed;
        }

        public void setWind_speed(String wind_speed) {
            this.wind_speed = wind_speed;
        }
    }

}


