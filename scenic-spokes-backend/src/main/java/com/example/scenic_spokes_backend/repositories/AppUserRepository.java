package com.example.scenic_spokes_backend.repositories;

import com.example.scenic_spokes_backend.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findByClerkId(String clerkId); //method to find user by clerk id
}
