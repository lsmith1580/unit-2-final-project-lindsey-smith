package com.example.scenic_spokes_backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data //@Data a part of Lombok library, generates common boilerplate code when you run the program
@Entity
@Table(name = "app_users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) //ensures Lombok doesn't generate a setter for id
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false) //ensures no duplicate usernames in database and cannot be null
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "appUser")
    @JsonBackReference
    private final List<PackingList> packingLists = new ArrayList<>();

    //private Boolean isActive?
    //private Boolean isLoggedIn?

}
