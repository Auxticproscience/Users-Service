package com.userService.presentation.controller.auth;

import com.userService.presentation.dto.auth.request.*;
import com.userService.presentation.dto.auth.response.AuthResponse;
import com.userService.presentation.dto.auth.response.LoginResponse;
import com.userService.service.interfaces.auth.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints para autenticación y gestión de credenciales")
public class AuthController {

    private final AuthService authService;

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AuthResponse> create(@Valid @RequestBody CreateAuthRequest createAuthRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.create(createAuthRequest));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}")
    public ResponseEntity<AuthResponse> getByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(authService.findByUserId(userId));
    }

    @GetMapping()
    public ResponseEntity<List<AuthResponse>> getAll() {
        return ResponseEntity.ok(authService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<AuthResponse> update(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateAuthRequest request
    ) {
        return ResponseEntity.ok(authService.update(userId, request));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/me/email")
    public ResponseEntity<AuthResponse> updateMyEmail(
            @RequestBody @Valid UpdateMyEmailRequest request) {

        return ResponseEntity.ok(authService.updateMyEmail(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        authService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok("");
    }
}
