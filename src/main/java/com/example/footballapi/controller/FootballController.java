package com.example.footballapi.controller;


import com.example.footballapi.dto.CountryDTO;
import com.example.footballapi.dto.LeagueDTO;
import com.example.footballapi.dto.TeamStandingDTO;
import com.example.footballapi.service.FootballApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;


@RestController
@RequestMapping("/api/football")
// Configure CORS as needed. For development, allowing localhost (Angular default port 4200)
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
public class FootballController {

    private static final Logger logger = LoggerFactory.getLogger(FootballController.class);
    private final FootballApiService footballApiService;

    @Autowired
    public FootballController(FootballApiService footballApiService) {
        this.footballApiService = footballApiService;
    }

    @GetMapping("/standings/team")
    public ResponseEntity<TeamStandingDTO> getTeamStanding(
            @RequestParam String countryName,
            @RequestParam String leagueName,
            @RequestParam String teamName) {
        
        logger.info("Received request for team standing: Country='{}', League='{}', Team='{}'",
                countryName, leagueName, teamName);

        if (countryName.trim().isEmpty() || leagueName.trim().isEmpty() || teamName.trim().isEmpty()) {
            throw new IllegalArgumentException("Country name, league name, and team name must not be empty.");
        }
        
        TeamStandingDTO standing = footballApiService.getTeamStanding(countryName, leagueName, teamName);
        return ResponseEntity.ok(standing);
    }

    @GetMapping("/countries")
    public ResponseEntity<List<CountryDTO>> getCountries() {
        return ResponseEntity.ok(footballApiService.getCountries());
    }

    @GetMapping("/leagues")
    public ResponseEntity<List<LeagueDTO>> getLeagues(@RequestParam String countryId) {
        return ResponseEntity.ok(footballApiService.getLeaguesByCountryId(countryId));
    }
}