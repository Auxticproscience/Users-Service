package com.userService.service.interfaces;

import com.userService.presentation.dto.CreateUserRequest;
import com.userService.presentation.dto.UpdateUserRequest;
import com.userService.presentation.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse create(CreateUserRequest req);
    UserResponse findById(UUID id);
    UserResponse update(UUID id, UpdateUserRequest req);
    void updateEmail(UUID id, String email);
    void delete(UUID id);
    List<UserResponse> getAll();
}



