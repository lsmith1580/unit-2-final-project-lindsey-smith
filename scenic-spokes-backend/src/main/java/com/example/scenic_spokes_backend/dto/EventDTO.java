package com.example.scenic_spokes_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO {
    private int id;
    private String title;
    private String description;
    private LocalDate date;
    private String image;
    private boolean isUserEvent;
}
