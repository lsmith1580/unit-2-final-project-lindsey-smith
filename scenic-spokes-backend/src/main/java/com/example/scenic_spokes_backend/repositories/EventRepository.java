package com.example.scenic_spokes_backend.repositories;

import com.example.scenic_spokes_backend.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
