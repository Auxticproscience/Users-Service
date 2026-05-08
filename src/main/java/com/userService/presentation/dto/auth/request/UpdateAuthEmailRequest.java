package com.userService.presentation.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateAuthEmailRequest(@Email @NotBlank String email) {}
