package com.userService.service.interfaces.auth;

import com.userService.presentation.dto.auth.request.ChangePasswordRequest;
import com.userService.presentation.dto.auth.request.CreateAuthRequest;
import com.userService.presentation.dto.auth.request.UpdateAuthRequest;
import com.userService.presentation.dto.auth.request.UpdateMyEmailRequest;
import com.userService.presentation.dto.auth.response.AuthResponse;

import java.util.List;
import java.util.UUID;

public interface AuthService {
    String login(String email, String password);

    void changePassword(ChangePasswordRequest changePasswordRequest);

    AuthResponse create(CreateAuthRequest createAuthRequest);

    AuthResponse findByUserId(UUID userId);

    List<AuthResponse> findAll();

    AuthResponse update(UUID userId, UpdateAuthRequest updateAuthRequest);

    AuthResponse updateMyEmail(UpdateMyEmailRequest request);

    void delete(UUID userId);
}

