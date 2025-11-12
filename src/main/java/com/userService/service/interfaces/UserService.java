package com.userService.service.interfaces;

import com.userService.presentation.dto.CreateUserRequest;
import com.userService.presentation.dto.UpdateUserRequest;
import com.userService.presentation.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UUID create(CreateUserRequest req);
    UserResponse findById(UUID id);
    UserResponse update(UUID id, UpdateUserRequest req);
    List<UserResponse> getAll();
}
