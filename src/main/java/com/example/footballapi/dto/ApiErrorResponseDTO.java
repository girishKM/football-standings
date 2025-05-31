package com.example.footballapi.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiErrorResponseDTO {
    // Based on common APIFootball error structure like {"error": 404, "message": "No_league_found"}
    private int error;
    private String message;
}