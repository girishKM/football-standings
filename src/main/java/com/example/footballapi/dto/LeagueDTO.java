package com.example.footballapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeagueDTO {

    @JsonProperty("league_id")
    private String leagueId;

    @JsonProperty("league_name")
    private String leagueName;

    @JsonProperty("country_id")
    private String countryId;

    @JsonProperty("country_name")
    private String countryName;

}