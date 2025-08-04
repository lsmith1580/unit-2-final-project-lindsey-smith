package com.example.scenic_spokes_backend.controllers;

import com.example.scenic_spokes_backend.dto.EventDTO;
import com.example.scenic_spokes_backend.mappers.EventMapper;
import com.example.scenic_spokes_backend.repositories.AppUserRepository;
import com.example.scenic_spokes_backend.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepository;
    private final AppUserRepository appUserRepository;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents(@AuthenticationPrincipal Jwt jwtOrNull) {

    }
}
