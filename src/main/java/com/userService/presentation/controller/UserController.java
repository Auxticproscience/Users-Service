package com.userService.presentation.controller;

import com.userService.presentation.dto.*;
import com.userService.service.interfaces.UserService;
import com.userService.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<UserResponse> getCurrentUser() {
        String userId = SecurityUtils.getId();
        return ResponseEntity.ok(userService.findById(UUID.fromString(userId)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE') or #id.toString() == authentication.principal")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUser(){
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, UUID>> createUser(@Valid @RequestBody CreateUserRequest req) {
        UUID id = userService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id.toString() == authentication.principal")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest req) {
        return ResponseEntity.ok(userService.update(id, req));
    }

    @GetMapping("/{id}/profile")
    @PreAuthorize("hasRole('ADMIN') or #id.toString() == authentication.principal")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable UUID id) {
        UserResponse user = userService.findById(id);

        return ResponseEntity.ok(
                new UserProfileResponse(
                        user.id(),
                        user.firstName(),
                        user.lastName()
                )
        );
    }
}
