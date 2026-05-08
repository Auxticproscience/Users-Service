package com.userService.presentation.dto.auth.request;

import java.util.UUID;

public record CreateAuthRequest(
        UUID userId,
        String email,
        String password,
        String role
) {}

