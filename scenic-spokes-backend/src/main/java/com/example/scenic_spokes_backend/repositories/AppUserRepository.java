package com.example.scenic_spokes_backend.repositories;

import com.example.scenic_spokes_backend.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
}
