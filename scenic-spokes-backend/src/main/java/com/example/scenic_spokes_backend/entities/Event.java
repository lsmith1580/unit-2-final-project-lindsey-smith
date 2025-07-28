package com.example.scenic_spokes_backend.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private Long id;

    private String title;
    private String description;
    private LocalDate eventDate;
    private String location;
    private String imageUrl;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;
}
