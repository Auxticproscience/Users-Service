package com.userService.service.interfaces.user;

import com.userService.presentation.dto.user.request.CreateUserRequest;
import com.userService.presentation.dto.user.request.UpdateUserRequest;
import com.userService.presentation.dto.user.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse create(CreateUserRequest req);
    UserResponse findById(UUID id);
    UserResponse update(UUID id, UpdateUserRequest req);
    void delete(UUID id);
    List<UserResponse> getAll();
}



