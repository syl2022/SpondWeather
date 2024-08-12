# <div align="center">Weather App</div>

- This Web Application is based on Java, SpringBoot, JPA and Liquidbase. Uses caching from caffeine. Webflux for WebCLient
- It has OpenAPI swagger-ui accessible at http://localhost:8080/swagger-ui/index.html

# Input:
  - GetRequest:
  - http://localhost:8080/spond/weather?eventId={id}
  - It takes longitude, latitude, startDate, endDate from the Database.

  - EventId should be string
  - Refer sample data from db-changelog for eventId

# Sample Response
```
{
    "eventId": "event_001",
    "temperature": 29.5,
    "windSpeed": 1.4,
    "forcast": "rain"
}
```
  The logic that I tried to follow is to pick the middle time from the start and end time for the event to get the approximation and then find the closest timseries in the WeatherResponse/cached data to get the forcast for it. I am using caching to avoid making repeated API calls to met.no using combination of lattituted and longitude as the key.

