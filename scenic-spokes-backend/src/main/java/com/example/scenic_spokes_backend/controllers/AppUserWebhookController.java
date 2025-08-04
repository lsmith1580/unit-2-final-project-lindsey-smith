package com.example.scenic_spokes_backend.controllers;

import com.example.scenic_spokes_backend.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/webhooks/clerk")
@RequiredArgsConstructor
public class AppUserWebhookController {

    private final AppUserRepository appUserRepository;

    @PostMapping
    public ResponseEntity<Void> handleClerkWebhook(@RequestBody Map<String, Object> payload) {
        String
    }
}
