# Football API Spring Boot Service

This project is a Spring Boot REST API that provides football (soccer) data such as countries, leagues, and team standings. It integrates with the [APIFootball](https://apifootball.com/documentation/) service and uses Caffeine for caching, supporting offline access for previously fetched data.

---

## Features

- **Get Supported Countries:**  
  Returns the list of countries available for your API key.

- **Get Leagues by Country:**  
  Returns all leagues for a given country.

- **Get Team Standings:**  
  Returns the standings for a specific team in a league.

- **Caching:**  
  Uses Caffeine to cache countries, leagues, and standings for fast and offline access.

- **Offline Support:**  
  If data is cached, endpoints will work even if the external API is unavailable.

---

## API Endpoints

| Method | Endpoint                                      | Description                                 |
|--------|-----------------------------------------------|---------------------------------------------|
| GET    | `/api/football/countries`                     | List all supported countries                |
| GET    | `/api/football/leagues?countryId={countryId}` | List all leagues for a country              |
| GET    | `/api/football/standings/team`                | Get a team's standing in a league           |

### Example: Get Team Standing

```
GET /api/football/standings/team?countryName=England&leagueName=Premier%20League&teamName=Liverpool
```

---

## API Documentation

- **Swagger UI:**  
  Visit [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) for interactive API docs.

## Error Codes

| Code | Meaning                                  |
|------|------------------------------------------|
| 200  | Success                                  |
| 400  | Invalid input parameters                 |
| 404  | Country, league, or team not found       |
| 500  | Internal server error or API failure     |

---
![football drawio](https://github.com/user-attachments/assets/376a0ab7-3437-4cb4-aa3c-7bc1f48ef3d8)

## Sequence Diagram

Below is a typical flow for the "Get Team Standing" endpoint.  
**You can recreate or edit this diagram in [draw.io](https://www.draw.io):**


## Design Patterns Used

- **Service Layer Pattern:**  
  Business logic is encapsulated in service classes (`FootballApiService`), separate from controllers.

- **DTO (Data Transfer Object) Pattern:**  
  All API responses use DTOs (`CountryDTO`, `LeagueDTO`, `TeamStandingDTO`) to decouple internal models from external representations.

- **Dependency Injection:**  
  Spring's `@Autowired` and constructor injection are used throughout for loose coupling.

- **Caching Pattern:**  
  Caffeine cache is used to optimize repeated data fetches and support offline access.

- **Exception Handling Pattern:**  
  Custom exceptions and a global exception handler provide clear error responses.

---

## Setup

1. **Clone the repository:**
   ```sh
   git clone https://github.com/your-username/your-repo.git
   cd your-repo
   ```

2. **Configure your API key:**
   - Edit `src/main/resources/application.properties` and set:
     ```
     apifootball.api.key=YOUR_API_KEY_HERE
     ```

3. **Build and run:**
   ```sh
   ./mvnw spring-boot:run
   ```
   or
   ```sh
   mvn spring-boot:run
   ```

4. **Access the API:**  
   By default, the API runs at [http://localhost:8080](http://localhost:8080).

---

## Caching & Offline Mode

- Data is cached for a configurable TTL (see `application.properties`).
- If the external API is down, cached data will be served (if available and not expired).
- To ensure offline access, access the endpoints at least once while online.

---

## How to Build and Run

### Prerequisites
- Java 17+
- Maven
- Docker

### Build and Run with Docker

1. Build the JAR:
   ```sh
   mvn clean package
   ```
2. Build the Docker image:
   ```sh
   docker build -t footballapi:latest .
   ```
3. Run the Docker container (replace `YOUR_API_KEY`):
   ```sh
   docker run -d -e APIFOOTBALL_API_KEY=YOUR_API_KEY -p 8080:8080 footballapi:latest
   ```
4. Access the API at [http://localhost:8080](http://localhost:8080)

### Jenkins Pipeline

- The `Jenkinsfile` is included for CI/CD demonstration.
- Reviewer can view pipeline steps or use it in their Jenkins setup.

### Configuration

- Edit `src/main/resources/application.properties` as needed.
- Or override `apifootball.api.key` with the `APIFOOTBALL_API_KEY` environment variable.