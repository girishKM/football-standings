package com.example.footballapi.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.example.footballapi.dto.ApiErrorResponseDTO;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn("Resource not found: {}", ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<Object> handleExternalApiException(ExternalApiException ex) {
        logger.error("External API error: {}", ex.getMessage(), ex.getCause());
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Error communicating with the external football API.");
        body.put("details", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex) {
        logger.warn("HTTP Client Error: {} - Body: {}", ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
        ApiErrorResponseDTO apiError = ex.getResponseBodyAs(ApiErrorResponseDTO.class);
        if (apiError != null && apiError.getMessage() != null) {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "External API returned an error: " + apiError.getMessage());
            body.put("statusCodeFromApi", apiError.getError());
            return new ResponseEntity<>(body, ex.getStatusCode());
        }
        Map<String, Object> body = new HashMap<>();
        body.put("message", "An error occurred while calling the external API.");
        body.put("details", ex.getResponseBodyAsString());
        return new ResponseEntity<>(body, ex.getStatusCode());
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Object> handleResourceAccessException(ResourceAccessException ex) {
        logger.error("Resource Access Exception (e.g., connection timeout): {}", ex.getMessage(), ex);
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Could not connect to the external football API. It might be temporarily unavailable.");
        body.put("details", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.SERVICE_UNAVAILABLE);
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        logger.warn("Missing request parameter: {}", ex.getParameterName());
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Required request parameter '" + ex.getParameterName() + "' is not present.");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("Illegal argument: {}", ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        logger.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        Map<String, Object> body = new HashMap<>();
        body.put("message", "An unexpected internal server error occurred.");
        body.put("details", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}