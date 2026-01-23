package com.userService.presentation.dto;

import java.util.UUID;

public record UserProfileResponse(UUID id, String firstName, String lastName) {
    public String fullName() {
        return firstName + " " + lastName;
    }
}
