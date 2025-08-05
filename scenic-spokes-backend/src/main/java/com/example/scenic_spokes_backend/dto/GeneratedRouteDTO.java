package com.example.scenic_spokes_backend.dto;

import lombok.Data;

import java.util.List;

//this is what will be sent to my frontend to be used
@Data
public class GeneratedRouteDTO {
    private List<List<Double>> coordinates;
    private double distanceKm;
    private double estimatedTimeMin;
}
