package com.userService.presentation.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateMyEmailRequest(
        @Email @NotBlank String email
) {}
