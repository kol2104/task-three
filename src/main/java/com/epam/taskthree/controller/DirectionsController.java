package com.epam.taskthree.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class DirectionsController {

    private static final String DIRECTIONS_API_URL = "https://maps.googleapis.com/maps/api/directions/json";

    private final RestTemplate restTemplate;
    @Value("${google.api-key}")
    private String apiKey;

    @Value("${google.destination}")
    private String destination;

    @GetMapping(value = "/directions", produces="application/json")
    public String getDirections(@RequestParam String origin) {
        // Construct the URL for the Directions API
        String apiUrl = String.format("%s?origin=%s&destination=%s&key=%s", DIRECTIONS_API_URL, origin, destination, apiKey);

        // Make a GET request to the Directions API and return the JSON response
        return restTemplate.getForObject(apiUrl, String.class);
    }
}
