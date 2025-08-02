package com.example.scenic_spokes_backend.controllers;

import com.example.scenic_spokes_backend.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepository;

    @GetMapping
    public
}
