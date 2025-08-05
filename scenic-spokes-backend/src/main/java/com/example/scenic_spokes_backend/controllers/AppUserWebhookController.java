package com.example.scenic_spokes_backend.controllers;

import com.example.scenic_spokes_backend.dto.ClerkWebhookUserDTO;
import com.example.scenic_spokes_backend.entities.AppUser;
import com.example.scenic_spokes_backend.repositories.AppUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/api/clerk/webhook")
@RequiredArgsConstructor
@Slf4j //annotation from lombok that enables logging
public class AppUserWebhookController {

    private final AppUserRepository appUserRepository;

    @PostMapping
    //returning void because you don't need to return a body for webhooks
    public ResponseEntity<Void> handleClerkWebhook(@RequestBody ClerkWebhookUserDTO webhook, HttpServletRequest request) {
        //to log what was received from clerk

        log.info("=== WEBHOOK HEADERS ===");
        request.getHeaderNames().asIterator().forEachRemaining(headerName ->
                log.info("{}: {}", headerName, request.getHeader(headerName))
        );
        log.info("=====================");

        log.info("Received webhook from Clerk: {}", webhook.getType());

        try { //if not user event, return
            if (!webhook.getType().startsWith("user.")) {
                return ResponseEntity.ok().build();
            }

            //get the user data from the webhook
            ClerkWebhookUserDTO.ClerkUserData userData = webhook.getData();

            //validation for user data
            if (userData == null || userData.getId() == null || userData.getId().trim().isEmpty()) {
                log.warn("Webhook has invalid user data");
                return ResponseEntity.ok().build();
            }
            //create or update the user
            saveOrUpdateUser(userData);

            log.info("Successfully processed user: {}", userData.getId());
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            //log it if anything goes wrong
            log.error("Error processing webhook: {}", e.getMessage());
            return ResponseEntity.ok().build(); //return success to prevent clerk from retrying
        }
    }

    // method to create new user or update existing user
    private void saveOrUpdateUser(ClerkWebhookUserDTO.ClerkUserData userData) {
        String clerkId = userData.getId();
        String username = userData.getUsername();
        String firstName = userData.getFirst_name();
        String lastName = userData.getLast_name();
        String email = getEmailFromUserData(userData);

        //check if user already exists
        Optional<AppUser> existingUser = appUserRepository.findByClerkId(clerkId);

        AppUser user;
        if (existingUser.isPresent()) {
            //update information if user exists
            user = existingUser.get();
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            log.info("Updating existing user: {}", clerkId);
        } else {
            //create new user if user doesn't exist
            user = AppUser.builder()
                    .clerkId(clerkId)
                    .username(username)
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .build();
            log.info("Creating new user: {}", clerkId);
        }

        //save to database
        appUserRepository.save(user);
    }

    //method to get email from clerk's json since it contains nested data
    private String getEmailFromUserData(ClerkWebhookUserDTO.ClerkUserData userData) {
        //clerk stores emails in an array, only want the first one
        if (userData.getEmail_addresses() != null &&
                !userData.getEmail_addresses().isEmpty()) {

            var firstEmail = userData.getEmail_addresses().getFirst();
            if (firstEmail != null && firstEmail.getEmail_address() != null) {
                return firstEmail.getEmail_address();
            }
        }
        return ""; //return empty string if no email found
    }
}
