package com.userService.presentation.controller;

import com.userService.presentation.dto.CreateAuthRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "auth-service",
        url = "${auth.service.url}"
)
public interface AuthClient {

    @PostMapping("/api/auth/internal/auth")
    void createAuth(@RequestBody CreateAuthRequest request);
}

