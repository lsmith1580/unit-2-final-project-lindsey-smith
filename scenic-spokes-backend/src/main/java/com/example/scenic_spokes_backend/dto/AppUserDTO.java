package com.example.scenic_spokes_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class AppUserDTO {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
}
