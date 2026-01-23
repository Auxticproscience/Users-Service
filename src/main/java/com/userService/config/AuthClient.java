package com.userService.config;

import com.userService.presentation.dto.AuthResponse;
import com.userService.presentation.dto.CreateAuthRequest;
import com.userService.presentation.dto.UpdateAuthEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthClient {

    private final WebClient webClient;

    public AuthResponse createAuth(CreateAuthRequest request) {
        return webClient.post()
                .uri("/api/auth/internal")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .block();
    }

    public void updateEmail(UUID userId, String email) {
        webClient.put()
                .uri("/api/auth/internal/{id}/email", userId)
                .bodyValue(Map.of("email", email))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void deleteAuth(UUID userId) {
        webClient.delete()
                .uri("/api/auth/internal/{id}", userId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}


