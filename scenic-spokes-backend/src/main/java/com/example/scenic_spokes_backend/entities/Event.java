package com.example.scenic_spokes_backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private int id;

    private String title;
    private String description;
    private LocalDate eventDate;
    private String location;
    private String imageUrl;

    private String clerkUserid;
}
