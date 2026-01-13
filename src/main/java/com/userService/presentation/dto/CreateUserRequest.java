package com.userService.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateUserRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Email String email,
        String phone,
        String positionTitle,
        String sede,
        String area,

        @NotBlank String password,
        @NotBlank String role,

        @NotNull LocalDate birthday
) {}
