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

## Sequence Diagram

Below is a typical flow for the "Get Team Standing" endpoint.  
**You can recreate or edit this diagram in [draw.io](https://www.draw.io):**

![Football API Sequence Diagram](https://raw.githubusercontent.com/your-username/your-repo/main/docs/footballapi-sequence-diagram.png)

<details>
<summary>Click to view draw.io XML (import into draw.io)</summary>

```xml
<mxfile host="app.diagrams.net">
  <diagram name="Football API Sequence">
    <mxGraphModel dx="1000" dy="1000" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="827" pageHeight="1169" math="0" shadow="0">
      <root>
        <mxCell id="0"/>
        <mxCell id="1" parent="0"/>
        <mxCell id="2" value="Client" style="swimlane" vertex="1" parent="1">
          <mxGeometry x="40" y="40" width="120" height="400" as="geometry"/>
        </mxCell>
        <mxCell id="3" value="FootballController" style="swimlane" vertex="1" parent="1">
          <mxGeometry x="200" y="40" width="120" height="400" as="geometry"/>
        </mxCell>
        <mxCell id="4" value="FootballApiService" style="swimlane" vertex="1" parent="1">
          <mxGeometry x="360" y="40" width="120" height="400" as="geometry"/>
        </mxCell>
        <mxCell id="5" value="APIFootball API" style="swimlane" vertex="1" parent="1">
          <mxGeometry x="520" y="40" width="120" height="400" as="geometry"/>
        </mxCell>
        <mxCell id="6" style="edgeStyle=orthogonalEdgeStyle;endArrow=block;html=1;" edge="1" parent="1" source="2" target="3">
          <mxGeometry relative="1" as="geometry"/>
        </mxCell>
        <mxCell id="7" value="getTeamStanding()" style="edgeStyle=orthogonalEdgeStyle;endArrow=block;html=1;" edge="1" parent="1" source="3" target="4">
          <mxGeometry relative="1" as="geometry"/>
        </mxCell>
        <mxCell id="8" value="getCountryIdByName()" style="edgeStyle=orthogonalEdgeStyle;endArrow=block;html=1;" edge="1" parent="1" source="4" target="5">
          <mxGeometry relative="1" as="geometry"/>
        </mxCell>
        <mxCell id="9" value="getLeaguesByCountryId()" style="edgeStyle=orthogonalEdgeStyle;endArrow=block;html=1;" edge="1" parent="1" source="4" target="5">
          <mxGeometry relative="1" as="geometry"/>
        </mxCell>
        <mxCell id="10" value="getStandingsByLeagueId()" style="edgeStyle=orthogonalEdgeStyle;endArrow=block;html=1;" edge="1" parent="1" source="4" target="5">
          <mxGeometry relative="1" as="geometry"/>
        </mxCell>
        <mxCell id="11" value="TeamStandingDTO" style="edgeStyle=orthogonalEdgeStyle;endArrow=block;html=1;" edge="1" parent="1" source="4" target="3">
          <mxGeometry relative="1" as="geometry"/>
        </mxCell>
        <mxCell id="12" value="TeamStandingDTO" style="edgeStyle=orthogonalEdgeStyle;endArrow=block;html=1;" edge="1" parent="1" source="3" target="2">
          <mxGeometry relative="1" as="geometry"/>
        </mxCell>
      </root>
    </mxGraphModel>
  </diagram>
</mxfile>
```
</details>

---

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

## License

This project is licensed under the MIT License.

---

**Happy Coding!**
