package com.example.scenic_spokes_backend.controllers;

import com.example.scenic_spokes_backend.dto.AppUserDTO;
import com.example.scenic_spokes_backend.entities.AppUser;
import com.example.scenic_spokes_backend.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserRepository appUserRepository;

    //get full list of users
    //http://localhost:8080/api/appUsers
    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        var allAppUsers = appUserRepository.findAll();
        return new ResponseEntity<>(allAppUsers, HttpStatus.OK);
    }

    //get a single user using id
    @GetMapping(value = "/{appUserId}")
    public ResponseEntity<?> getAppUserById(@PathVariable(value = "appUserId") int appUserId) {
        Optional<ResponseEntity<?>> appUser = appUserRepository.findById(appUserId)
                .map((user -> new ResponseEntity<>(user, HttpStatus.OK)));

        // { "response": "message" }
        return appUser.orElse(new ResponseEntity<>(
                Collections.singletonMap("response", "User with ID of " + appUserId + " not found."),
                HttpStatus.NOT_FOUND)
        );
    }

    //create a new user
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAppUser(@RequestBody AppUserDTO appUserData) {
        var appUser = AppUser.builder() //have to use builder from the lombok library for object creation
                .email(appUserData.getEmail())
                .username(appUserData.getUsername())
                .firstName(appUserData.getFirstName())
                .lastName(appUserData.getLastName())
                .build();
        var saved = appUserRepository.save(appUser);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    //delete user
    @DeleteMapping(value = "/delete/{appUserId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAppUser(@PathVariable(value = "appUserId") int appUserId) {
        AppUser currentAppUser = appUserRepository.findById(appUserId).orElse(null);
        if (currentAppUser != null) {
            appUserRepository.deleteById(appUserId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            String response = "User with ID of " + appUserId + " not found.";
            return new ResponseEntity<>(Collections.singletonMap("response", response), HttpStatus.NOT_FOUND);
        }
    }
}
