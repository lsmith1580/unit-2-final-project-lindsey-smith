package com.example.scenic_spokes_backend.controllers;

import com.example.scenic_spokes_backend.dto.EventDTO;
import com.example.scenic_spokes_backend.entities.AppUser;
import com.example.scenic_spokes_backend.entities.Event;
import com.example.scenic_spokes_backend.mappers.EventMapper;
import com.example.scenic_spokes_backend.repositories.AppUserRepository;
import com.example.scenic_spokes_backend.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
//a lot of imports, not sure how to make this look cleaner

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepository;
    private final AppUserRepository appUserRepository;
    private final EventMapper eventMapper;

    private static final String UPLOAD_DIR = "uploads"; //for the images users upload

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents(@AuthenticationPrincipal Jwt jwtOrNull) {
        String callerClerkId = jwtOrNull != null ? jwtOrNull.getSubject() : null; //if jwt token is not null, get the subject of the token
        List<EventDTO> dtos = eventRepository.findAll().stream()
                .map(event -> eventMapper.toDtoWithOwnership(event, callerClerkId))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate date,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        AppUser user = getCurrentUser(jwt.getSubject());

        Event event = Event.builder()
                .title(title)
                .description(description)
                .date(date)
                .image(saveFileIfPresent(file))
                .appUser(user)
                .build();

        Event saved = eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventMapper.toDtoWithOwnership(saved, user.getClerkId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(
            @PathVariable int id,
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        Event event = getEventOwnedByUser(id, jwt.getSubject()); //method defined below to get event by user id

        event.setTitle(title);
        event.setDescription(description);
        event.setDate(date);

        if (file != null && !file.isEmpty()) {
            event.setImage(saveFileIfPresent(file)); //have to do some extra validation since I am allowing users to upload images with events
        }

        Event updated = eventRepository.save(event);
        return ResponseEntity.ok(eventMapper.toDtoWithOwnership(updated, jwt.getSubject()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable int id, @AuthenticationPrincipal Jwt jwt) {
        Event event = getEventOwnedByUser(id, jwt.getSubject());
        eventRepository.delete(event);
        return ResponseEntity.noContent().build();
    }

    //the below methods were created to avoid repeating code in the controller

    private AppUser getCurrentUser(String clerkId) {
        return appUserRepository.findByClerkId(clerkId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found."));
    }

    private Event getEventOwnedByUser(int eventId, String clerkId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found."));

        if (!event.getAppUser().getClerkId().equals(clerkId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this event.");
        }
        return event;
    }

    private String saveFileIfPresent(MultipartFile file) { //in the future will move to saving images in Amazon S3 or something similar, but
        if(file == null || file.isEmpty()) return null;    // for now I am saving images in a folder in the project

        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR, fileName);
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed.");
        }
    }
}
