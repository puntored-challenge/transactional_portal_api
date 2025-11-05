package com.codechallenge.transactional_portal_api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "Name is required")
    @Size(min = 8, message = "Name must have at least 8 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;
}
