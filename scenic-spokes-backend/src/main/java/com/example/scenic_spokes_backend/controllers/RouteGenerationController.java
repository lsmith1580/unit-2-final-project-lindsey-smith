package com.example.scenic_spokes_backend.controllers;

import com.example.scenic_spokes_backend.dto.GeneratedRouteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routes")
public class RouteGenerationController {

    @Value("${tomtom.api.key}")
    private String tomtomApiKey;

    @GetMapping("/generate")
    public ResponseEntity<GeneratedRouteDTO> generateRoute( //will return custom object to send to frontend
            @RequestParam double startLat,
            @RequestParam double startLng,
            @RequestParam double endLat,
            @RequestParam double endLng) {

        String url = String.format( //constructs the tomtom api url with hardcoded query parameters for travel mode, route type, hilliness, and windingness
                "https://api.tomtom.com/routing/1/calculateRoute/%f,%f:%f,%f/json"
                        + "?key=%s&travelMode=motorcycle&hilliness=high&windingness=strong&routeType=thrilling",
                startLat, startLng, endLat, endLng, tomtomApiKey
        );

        RestTemplate restTemplate = new RestTemplate(); //creates the rest template to make the request to tomtom
        Map<String, Object> response = restTemplate.getForObject(url, Map.class); //gets the response and parses it into Map

        List<Map<String, Object>> routes = (List<Map<String, Object>>) response.get("routes"); //these extract the data we want from the tomtom response to only get the info we need
        Map<String, Object> firstRoute = routes.get(0);
        List<Map<String, Object>> legs = (List<Map<String, Object>>) firstRoute.get("legs");
        Map<String, Object> firstLeg = legs.get(0);
        List<Map<String, Double>> points = (List<Map<String, Double>>) firstLeg.get("points");

        List<List<Double>> coords = points.stream()
                .map(p -> List.of(p.get("latitude"), p.get("longitude")))
                .collect(Collectors.toList()); //converts the points into a format that leaflet needs

        Map<String, Object> summary = (Map<String, Object>) firstRoute.get("summary");
        double distanceKm = ((Number) summary.get("lengthInMeters")).doubleValue() / 1000; //extracting distance and time from the response to display to the user on the frontend
        int timeMin = ((Number) summary.get("travelTimeInSeconds")).intValue() / 60;

        GeneratedRouteDTO dto = new GeneratedRouteDTO(); //builds a new generated route DTO
        dto.setCoordinates(coords);
        dto.setDistanceKm(distanceKm);
        dto.setEstimatedTimeMin(timeMin);

        return ResponseEntity.ok(dto); //returns the dto to the frontend
    }
}
