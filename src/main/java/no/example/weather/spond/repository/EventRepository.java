package no.example.weather.spond.repository;

import no.example.weather.spond.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event getEventByEventId(String eventId);
}
