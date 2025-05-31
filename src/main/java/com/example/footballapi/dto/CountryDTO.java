package com.example.footballapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CountryDTO {
    @JsonProperty("country_id")
    private String countryId;

    @JsonProperty("country_name")
    private String countryName;

    // Getters and setters
    public String getCountryId() { return countryId; }
    public void setCountryId(String countryId) { this.countryId = countryId; }

    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }
}
