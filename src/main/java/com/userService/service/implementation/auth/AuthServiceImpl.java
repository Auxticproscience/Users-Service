package com.userService.service.implementation.auth;

import com.userService.exception.UserValidationException;
import com.userService.persistence.entity.auth.AuthCredential;
import com.userService.persistence.enums.AccountStatus;
import com.userService.persistence.repository.auth.AuthRepository;
import com.userService.presentation.dto.auth.request.ChangePasswordRequest;
import com.userService.presentation.dto.auth.request.CreateAuthRequest;
import com.userService.presentation.dto.auth.request.UpdateAuthRequest;
import com.userService.presentation.dto.auth.request.UpdateMyEmailRequest;
import com.userService.presentation.dto.auth.response.AuthResponse;
import com.userService.service.interfaces.auth.AuthService;
import com.userService.utils.JwtUtils;
import com.userService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUtils jwtUtils;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(String email, String password) {

        AuthCredential credential = authRepository
                .findByEmailAndStatus(email, AccountStatus.ACTIVE)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado o inactivo"));

        if (!passwordEncoder.matches(password, credential.getPasswordHash())) {
            throw new UserValidationException("Correo o contraseña incorrectos");
        }

        return jwtUtils.createToken(
                credential.getId().toString(),
                credential.getUserId().toString(),
                credential.getEmail(),
                "ROLE_" + credential.getRole()
        );
    }

    @Override
    public AuthResponse create(CreateAuthRequest createAuthRequest) {
        if(authRepository.existsByEmail(createAuthRequest.email())){
            throw new UsernameNotFoundException("Email ya registrado");
        }

        AuthCredential credential = AuthCredential.builder()
                .userId(createAuthRequest.userId())
                .email(createAuthRequest.email())
                .passwordHash(passwordEncoder.encode(createAuthRequest.password()))
                .role(createAuthRequest.role())
                .build();

        AuthCredential saved = authRepository.save(credential);

        return mapToResponse(saved);
    }

    @Override
    public AuthResponse findByUserId(UUID userId) {
        return authRepository.findByUserIdAndStatus(userId, AccountStatus.ACTIVE)
                .map(this::mapToResponse)
                .orElseThrow(() -> new IllegalArgumentException("Credencial no encontrado"));
    }

    @Override
    public List<AuthResponse> findAll() {
        return authRepository.findAllByStatus(AccountStatus.ACTIVE)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        AuthCredential credential = authRepository.findByEmail(changePasswordRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + changePasswordRequest.getEmail()));
        String encodedPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        credential.setPasswordHash(encodedPassword);
        authRepository.save(credential);
    }

    @Override

    public AuthResponse update(UUID userId, UpdateAuthRequest updateAuthRequest) {

        AuthCredential credential = authRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if(!credential.getEmail().equals(updateAuthRequest.email())  && authRepository.existsByEmail(updateAuthRequest.email())){
            throw new UsernameNotFoundException("Email ya registrado");
        }

        credential.setEmail(updateAuthRequest.email());
        credential.setRole(updateAuthRequest.role());

        AuthCredential updated = authRepository.save(credential);
        return mapToResponse(updated);
    }

    @Override
    public AuthResponse updateMyEmail(UpdateMyEmailRequest request) {

        UUID userId = SecurityUtils.getCurrentUserId();

        AuthCredential credential = authRepository
                .findByUserIdAndStatus(userId, AccountStatus.ACTIVE)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (!credential.getEmail().equals(request.email())
                && authRepository.existsByEmail(request.email())) {
            throw new UsernameNotFoundException("Email ya registrado");
        }

        credential.setEmail(request.email());

        AuthCredential updated = authRepository.save(credential);

        return mapToResponse(updated);
    }


    @Override
    public void delete(UUID userId) {
        AuthCredential credential = authRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Credencial no encontrado"));

        credential.setStatus(AccountStatus.INACTIVE);
        authRepository.save(credential);
    }

    private AuthResponse mapToResponse(AuthCredential credential) {
        return new AuthResponse(
                credential.getId(),
                credential.getUserId(),
                credential.getEmail(),
                credential.getRole()
        );
    }
}
