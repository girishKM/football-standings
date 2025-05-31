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

## Development

- Java 17+ recommended
- Spring Boot 3.x
- Caffeine cache

---

## License

This project is licensed under the MIT License.

---

## Credits

- [APIFootball](https://apifootball.com/documentation/) for football data API

---

**Happy Coding!**
