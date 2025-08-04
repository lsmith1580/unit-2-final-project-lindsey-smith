package com.example.scenic_spokes_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MotorcycleRouteDTO {
    private int id;
    private String name;
    private String startPoint;
    private String endPoint;
    private String routeData;
}
