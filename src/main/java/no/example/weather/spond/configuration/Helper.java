package no.example.weather.spond.configuration;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Helper {
    private static final DateTimeFormatter RFC_1123_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;

    public static Instant convertToInstantFromRFC(String dateTime){
       return ZonedDateTime.parse(dateTime, RFC_1123_FORMATTER).toInstant();
    }
    public static Instant convertToInstant(String dateTime){
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .atZone(ZoneId.systemDefault())
                .toInstant();
    }
}
