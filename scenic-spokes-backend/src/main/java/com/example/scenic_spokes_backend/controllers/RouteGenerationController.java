package com.example.scenic_spokes_backend.controllers;

import com.example.scenic_spokes_backend.dto.GeneratedRouteDTO;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routes")
public class RouteGenerationController {

    @Value("${tomtom.api.key}")
    private String tomtomApiKey;

    @GetMapping("/generate")
    public ResponseEntity<GeneratedRouteDTO> generateRoute(
            @RequestParam double startLat,
            @RequestParam double startLng,
            @RequestParam double endLat,
            @RequestParam double endLng) {

        // build the tomtom url used to calculate route with desired parameters
        String url = String.format(
                Locale.US,
                "https://api.tomtom.com/routing/1/calculateRoute/%f,%f:%f,%f/json" +
                        "?key=%s&routeType=thrilling&hilliness=high&windingness=high&travelMode=motorcycle",
                startLat, startLng, endLat, endLng, tomtomApiKey
        );

        RestTemplate restTemplate = new RestTemplate();

        try {
            // call tomtom, if no body return custom http response
            JsonNode body = restTemplate.getForObject(url, JsonNode.class);
            if (body == null) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Empty response from TomTom.");
            }
            // parse the first route
            JsonNode route = body.path("routes").get(0);
            if (route == null || route.isMissingNode()) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "No routes returned by TomTom.");
            }
            JsonNode leg = route.path("legs").get(0);
            JsonNode points = leg.path("points");

            // build latitude and longitude pairs for leaflet
            List<List<Double>> coords = new ArrayList<>();
            for (JsonNode p : points) {
                coords.add(List.of(
                        p.path("latitude").asDouble(),
                        p.path("longitude").asDouble()
                ));
            }
            // retrieve distance and time of route
            JsonNode summary = route.path("summary");
            double distanceKm = summary.path("lengthInMeters").asDouble() / 1000.0;
            int estimatedTimeMin = (int) Math.round(summary.path("travelTimeInSeconds").asDouble() / 60.0);

            // build the dto
            GeneratedRouteDTO dto = new GeneratedRouteDTO();
            dto.setCoordinates(coords);
            dto.setDistanceKm(distanceKm);
            dto.setEstimatedTimeMin(estimatedTimeMin);

            return ResponseEntity.ok(dto);

        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "TomTom rejected request: " + e.getResponseBodyAsString(),
                    e
            );
        } catch (RestClientException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Route provider error. Please try again.",
                    e
            );
        }
    }
}

