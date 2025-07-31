package com.example.scenic_spokes_backend.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Entity
@Table(name = "motorcycle_routes")
public class MotorcycleRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String origin;
    private String destination;
    private String routeData;

    @Column(nullable = false)
    private String clerkUserId;
}
