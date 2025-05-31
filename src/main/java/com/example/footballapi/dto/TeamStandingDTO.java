package com.example.footballapi.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamStandingDTO {

    @JsonProperty("country_name")
    private String countryName; // This is often part of the standings record

    @JsonProperty("league_id")
    private String leagueId;

    @JsonProperty("league_name")
    private String leagueName;

    @JsonProperty("team_id")
    private String teamId;

    @JsonProperty("team_name")
    private String teamName;

    @JsonProperty("overall_league_position")
    private String overallLeaguePosition;

    @JsonProperty("overall_league_payed")
    private String overallLeaguePlayed;

    @JsonProperty("overall_league_W")
    private String overallLeagueW;

    @JsonProperty("overall_league_D")
    private String overallLeagueD;

    @JsonProperty("overall_league_L")
    private String overallLeagueL;

    @JsonProperty("overall_league_GF")
    private String overallLeagueGF;

    @JsonProperty("overall_league_GA")
    private String overallLeagueGA;

    @JsonProperty("overall_league_PTS")
    private String overallLeaguePTS;

    // Note: APIFootball's get_standings might return country_id at the same level as team_name.
    // If you need country_id directly associated with each team standing and it's not in the
    // standings response, you might infer it from the league details.
    // For now, we assume country_name is in the standing record.
}