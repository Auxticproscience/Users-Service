package com.userService.presentation.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String position,
        String sede
) {}
