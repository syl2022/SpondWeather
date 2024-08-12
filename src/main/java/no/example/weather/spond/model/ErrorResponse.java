package no.example.weather.spond.model;

import java.time.Instant;

public class ErrorResponse {
    private String message;
    private Instant timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = Instant.now();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
// Getters and Setters
}
