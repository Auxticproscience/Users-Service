package com.userService.presentation.dto;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        String phone,
        String position,
        String area
) {
}
