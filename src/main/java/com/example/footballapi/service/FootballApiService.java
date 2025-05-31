package com.example.footballapi.service;


import com.example.footballapi.config.CacheConfig;
import com.example.footballapi.dto.CountryDTO;
import com.example.footballapi.dto.LeagueDTO;
import com.example.footballapi.dto.TeamStandingDTO;
import com.example.footballapi.exception.ExternalApiException;
import com.example.footballapi.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FootballApiService {

    private static final Logger logger = LoggerFactory.getLogger(FootballApiService.class);

    private final RestTemplate restTemplate;

    @Value("${apifootball.api.url}")
    private String apiUrlBase;

    @Value("${apifootball.api.key}")
    private String apiKey;

    @Value("${app.features.prefer-cache-over-live:false}")
    private boolean preferCacheOverLive;

    private static final List<String> SUPPORTED_COUNTRIES = Arrays.asList("England", "France");

    public FootballApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Main public method to get a specific team's standing.
     */
    public TeamStandingDTO getTeamStanding(String countryName, String leagueName, String teamName) {
        if (!SUPPORTED_COUNTRIES.contains(countryName.trim())) {
            throw new ResourceNotFoundException("Country '" + countryName + "' is not supported by your API key.");
        }
        if (apiKey == null || apiKey.isEmpty() || "YOUR_API_KEY_HERE".equals(apiKey)) {
            logger.error("API Key is not configured. Please set apifootball.api.key in application.properties.");
            throw new ExternalApiException("API Key for football data service is not configured.");
        }
        
        // Step 1: Find the league ID using country name and league name.
        // The API might allow direct search by country name for leagues, or you might need country_id first.
        // Assuming APIFootball `get_leagues` can take `country_name` or you have a mapping.
        // For simplicity, let's assume APIFootball `get_leagues` takes country_id,
        // and you'd first need to resolve country_name to country_id (another API call or local map).
        // Let's simplify here: assume we can find leagues by country name directly or we get all leagues and filter.
        // A more robust solution might involve calling `get_countries` first if `country_id` is mandatory for `get_leagues`.

        LeagueDTO targetLeague = findLeague(countryName, leagueName);
        if (targetLeague == null) {
            throw new ResourceNotFoundException("League '" + leagueName + "' in country '" + countryName + "' not found.");
        }

        // Step 2: Get all standings for that league.
        List<TeamStandingDTO> leagueStandings = getStandingsByLeagueId(targetLeague.getLeagueId());

        // Step 3: Filter for the specific team.
        Optional<TeamStandingDTO> teamStandingOpt = leagueStandings.stream()
                .filter(standing -> teamName.equalsIgnoreCase(standing.getTeamName()))
                .findFirst();

        return teamStandingOpt.orElseThrow(() ->
                new ResourceNotFoundException("Team '" + teamName + "' not found in league '" + leagueName + "'.")
        );
    }

    /**
     * Finds a league by country name and league name.
     * This is a simplified version. APIFootball might require country_id for get_leagues.
     * If so, you'd first need to resolve country_name to country_id.
     * Alternatively, if get_leagues doesn't support country_name filter,
     * you might fetch all leagues (if feasible) or leagues for a country_id and then filter.
     */
    protected LeagueDTO findLeague(String countryName, String leagueName) {
        String countryId = getCountryIdByName(countryName);
        List<LeagueDTO> leagues = getLeaguesByCountryId(countryId);
        return leagues.stream()
            .filter(league -> leagueName.equalsIgnoreCase(league.getLeagueName()))
            .findFirst()
            .orElse(null);
    }


    /**
     * Fetches all leagues from APIFootball. This might be a large dataset.
     * Ideally, APIFootball provides a way to filter leagues by country name or ID.
     * This method is cached.
     */
    @Cacheable(value = CacheConfig.LEAGUES_CACHE, unless = "#result == null || #result.isEmpty()")
    public List<LeagueDTO> getAllLeagues() {
        // If offline mode is preferred and cache exists, this method won't be called if Spring Cache hits.
        // The actual "preferCacheOverLive" logic needs to be more nuanced with @Cacheable.
        // @Cacheable primarily serves from cache if data exists and is not expired.
        // The "preferCacheOverLive" toggle is more about *not even trying* to hit the API if cache is sufficient.
        // For simplicity with @Cacheable, we assume if data is in cache and valid, it's used.
        // The toggle might be used at a higher level or to adjust cache TTLs.

        logger.info("Fetching all leagues from API. This might be slow and data-intensive.");
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrlBase)
                .queryParam("action", "get_leagues")
                .queryParam("APIkey", apiKey);
        String url = uriBuilder.toUriString();
        logger.debug("Fetching leagues from URL: {}", url);

        try {
            LeagueDTO[] leagues = restTemplate.getForObject(url, LeagueDTO[].class);
            return leagues != null ? Arrays.asList(leagues) : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            logger.error("HttpClientErrorException fetching all leagues: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new ExternalApiException("Error fetching leagues from API: " + e.getResponseBodyAsString(), e);
        } catch (ResourceAccessException e) { // Catches I/O errors like connection timeout
            logger.error("ResourceAccessException fetching all leagues: {}", e.getMessage(), e);
            throw new ExternalApiException("Failed to connect to leagues API: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error fetching all leagues", e);
            throw new ExternalApiException("An unexpected error occurred while fetching leagues.", e);
        }
    }

    /**
     * Fetches standings for a given league ID. Cached.
     */
    @Cacheable(value = CacheConfig.STANDINGS_BY_LEAGUE_CACHE, key = "#leagueId", unless = "#result == null || #result.isEmpty()")
    public List<TeamStandingDTO> getStandingsByLeagueId(String leagueId) {
        logger.info("Fetching standings for leagueId: {}", leagueId);
        String url = UriComponentsBuilder.fromHttpUrl(apiUrlBase)
                .queryParam("action", "get_standings")
                .queryParam("league_id", leagueId)
                .queryParam("APIkey", apiKey)
                .toUriString();

        TeamStandingDTO[] standings = restTemplate.getForObject(url, TeamStandingDTO[].class);
        if (standings == null || standings.length == 0) {
            logger.warn("No standings found or returned null for leagueId: {}", leagueId);
            return Collections.emptyList();
        }
        return Arrays.asList(standings);
    }

    /**
     * Fetches the country_id for a given country name.
     */
    protected String getCountryIdByName(String countryName) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrlBase)
                .queryParam("action", "get_countries")
                .queryParam("APIkey", apiKey)
                .toUriString();

        try {
            CountryDTO[] countries = restTemplate.getForObject(url, CountryDTO[].class);
            if (countries != null) {
                for (CountryDTO country : countries) {
                    if (countryName.equalsIgnoreCase(country.getCountryName())) {
                        return country.getCountryId();
                    }
                }
            }
            throw new ResourceNotFoundException("Country '" + countryName + "' not found.");
        } catch (Exception e) {
            logger.error("Error fetching country ID for '{}': {}", countryName, e.getMessage());
            throw new ExternalApiException("Failed to fetch country ID for '" + countryName + "'", e);
        }
    }

    /**
     * Fetches all leagues for a given country_id.
     */
    @Cacheable(value = CacheConfig.LEAGUES_CACHE, key = "#countryId", unless = "#result == null || #result.isEmpty()")
    public List<LeagueDTO> getLeaguesByCountryId(String countryId) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrlBase)
                .queryParam("action", "get_leagues")
                .queryParam("country_id", countryId)
                .queryParam("APIkey", apiKey)
                .toUriString();

        LeagueDTO[] leagues = restTemplate.getForObject(url, LeagueDTO[].class);
        return leagues != null ? Arrays.asList(leagues) : Collections.emptyList();
    }

    /**
     * Fetches all countries. This method is cached.
     */
    @Cacheable(value = CacheConfig.COUNTRIES_CACHE, unless = "#result == null || #result.isEmpty()")
    public List<CountryDTO> getCountries() {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrlBase)
                .queryParam("action", "get_countries")
                .queryParam("APIkey", apiKey)
                .toUriString();

        CountryDTO[] countries = restTemplate.getForObject(url, CountryDTO[].class);
        return countries != null ? Arrays.asList(countries) : Collections.emptyList();
    }

    // Note on "offline mode" toggle (app.features.prefer-cache-over-live):
    // The @Cacheable annotation handles the "read from cache if available" part.
    // If `preferCacheOverLive` is true AND the external API is down, Spring's caching
    // will serve stale data if the cache entry hasn't expired or if an
    // CacheErrorHandler is configured to return stale on error.
    // For a true "offline mode" where you *never* hit the API if the toggle is on
    // and data is in cache (even if stale by TTL), you'd need more complex logic
    // potentially involving programmatic cache interaction or AOP around cached methods.
    // The current setup prioritizes freshness up to TTL, then re-fetches.
    // If API fails during re-fetch, an exception is thrown unless cache is configured for stale-on-error.
}