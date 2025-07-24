package com.example.scenic_spokes_backend.controllers;

import com.example.scenic_spokes_backend.entities.AppUser;
import com.example.scenic_spokes_backend.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/appUsers")
public class AppUserController {

    @Autowired
    AppUserRepository appUserRepository;

    //get full list of users
    //http://localhost:8080/api/appUsers
    @GetMapping("")
    public ResponseEntity<?> getAllUsers() {
        List<AppUser> allAppUsers = appUserRepository.findAll();
        return new ResponseEntity<>(allAppUsers, HttpStatus.OK);
    }

    //get a single user using id
    @GetMapping(value = "/details/{appUserId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppUserById(@PathVariable(value = "appUserId") int appUserId) {
        AppUser currentAppUser = appUserRepository.findById(appUserId).orElse(null);
        if (currentAppUser != null) {
            return new ResponseEntity<>(currentAppUser, HttpStatus.OK);
        } else {
            String response = "User with ID of " + appUserId + " not found.";
            return new ResponseEntity<>(Collections.singletonMap("response", response), HttpStatus.NOT_FOUND);
        }
    }

    //create a new user
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewAppUser(@RequestBody )
}
