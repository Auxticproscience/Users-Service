package com.userService.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateAuthRequest(
        @NotNull UUID userId,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotBlank String role
) {}

