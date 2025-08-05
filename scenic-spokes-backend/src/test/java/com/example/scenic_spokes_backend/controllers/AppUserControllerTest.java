package com.example.scenic_spokes_backend.controllers;

import com.example.scenic_spokes_backend.entities.AppUser;
import com.example.scenic_spokes_backend.repositories.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AppUserControllerTest {
    @Mock
    private AppUserRepository appUserRepository;

    private AppUserController controller;

    @BeforeEach
    void setUp() {
        controller = new AppUserController(appUserRepository);
    }

    @Test
    void getAllUsers() {
        // Given users exist
        var users = List.of(AppUser.builder().build());
        when(appUserRepository.findAll()).thenReturn(users);
        // When
        ResponseEntity<List<AppUser>> response = controller.getAllUsers();
        // Then
        // Assert response.status == 200
        // assert response.body == users
    }

    @Test
    void getAppUserById() {
    }

    @Test
    void createAppUser() {
    }

    @Test
    void deleteAppUser() {
    }
}