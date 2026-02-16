package com.userService.presentation.dto;

import com.userService.persistence.enums.AccountStatus;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String position,
        String sede,
        String area,
        AccountStatus status
) {}
