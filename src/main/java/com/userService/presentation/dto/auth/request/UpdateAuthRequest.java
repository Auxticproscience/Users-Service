package com.userService.presentation.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateAuthRequest(
        @NotBlank @Email String email,
        @NotBlank String role,
        @NotNull String status
) {}
