package com.example.scenic_spokes_backend.controllers;

import com.example.scenic_spokes_backend.dto.ClerkWebhookUserDTO;
import com.example.scenic_spokes_backend.entities.AppUser;
import com.example.scenic_spokes_backend.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/webhooks/clerk")
@RequiredArgsConstructor
public class AppUserWebhookController {

    private final AppUserRepository appUserRepository;

    @PostMapping
    //returning void because you don't need to return a body for webhooks
    public ResponseEntity<Void> handleClerkWebhook(@RequestBody ClerkWebhookUserDTO webhook) {
        if (!webhook.getType().startsWith("user.")) { //clerk sends events like user.created and user.updated so any time one of those events occur,
            return ResponseEntity.ok().build();         //it will trigger an update to either create the user or update an existing user
        }

        ClerkWebhookUserDTO.ClerkUserData data = webhook.getData();

        String clerkId = data.getId();
        String username = data.getUsername();
        String firstName = data.getFirst_name();
        String lastName = data.getLast_name();
        String email = data.getEmail_addresses() != null && !data.getEmail_addresses().isEmpty()
                ? data.getEmail_addresses().getFirst().getEmail_address() //since clerk's json stores email address information in an array and I only need to access the email address itself
                : "";
        Optional<AppUser> existingAppUser = appUserRepository.findByClerkId(clerkId);
        AppUser appUser = existingAppUser.orElseGet(AppUser::new); //checks to see if the user already exists, if they do return the existing user, if not return a new user

        appUser.setClerkId(clerkId);
        appUser.setUsername(username);
        appUser.setFirstName(firstName);
        appUser.setLastName(lastName);
        appUser.setEmail(email);

        appUserRepository.save(appUser);
        return ResponseEntity.ok().build();
    }
}
