# <div align="center">Weather App</div>

- This Web Application is based on Java, SpringBoot, JPA and Liquidbase. Uses caching from caffeine. Webflux for WebCLient. Postgress DB
- It has OpenAPI swagger-ui accessible at http://localhost:8080/swagger-ui/index.html

# Input:
  - GetRequest:
  - http://localhost:8080/spond/weather?eventId={id}

  - EventId should be string
  - Refer sample data from db-changelog for eventId

# Sample Response
```{
    "eventId": "event_001",
    "temperature": 29.5,
    "windSpeed": 1.4,
    "forcast": "rain"
}

  

