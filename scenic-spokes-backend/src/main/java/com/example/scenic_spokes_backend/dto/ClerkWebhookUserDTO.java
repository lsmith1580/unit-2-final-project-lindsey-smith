package com.example.scenic_spokes_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClerkWebhookUserDTO {

    private String type;
    private ClerkUserData data;

    @Data
    public static class ClerkUserData {
        private String id;
        private String username;
        private String first_name;
        private String last_name;
        private List<EmailAddress> email_addresses;

        @Data
        public static class EmailAddress { //some of clerk's data is nested so had to set up my dto to account for that
            private String email_address;
        }
    }
}
