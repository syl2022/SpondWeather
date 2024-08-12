package no.example.weather.spond.controller;

import no.example.weather.spond.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spond")
public class GenericController {

    @GetMapping("/healthCheck")
    public ResponseEntity<Object> healthCheck() {
        try {
            return ResponseEntity.ok("Service is Up!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An unexpected error occurred: " + e.getMessage()));
        }
    }
}
