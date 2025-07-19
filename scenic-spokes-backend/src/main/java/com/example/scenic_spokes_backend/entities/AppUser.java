package com.example.scenic_spokes_backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor //Getter, Setter, NoArgsConstructor, and AllArgsConstructor a part of Lombok library, generates getters, setters, and constructors when you run the program
@Entity
@Table(name = "app_users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) //ensures Lombok doesn't generate a setter for id
    private Long id;

    @Column(unique = true, nullable = false) //ensures no duplicate usernames in database and cannot be null
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password; //password hashing?

    @Column(updatable = false) //created at timestamp cannot be changed once created
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //private Boolean isActive?
    //private Boolean isLoggedIn?

}
