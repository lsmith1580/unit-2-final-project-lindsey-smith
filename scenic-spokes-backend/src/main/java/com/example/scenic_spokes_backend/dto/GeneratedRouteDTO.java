package com.example.scenic_spokes_backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class GeneratedRouteDTO {
    private List<List<Double>> coordinates;
    private double distanceKm;
    private double estimatedTimeMin;
}
