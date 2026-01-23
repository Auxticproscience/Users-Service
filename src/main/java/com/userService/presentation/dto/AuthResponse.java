package com.userService.presentation.dto;

import java.util.UUID;

public record AuthResponse(
        Long id,
        UUID userId,
        String email,
        String role
) {}
