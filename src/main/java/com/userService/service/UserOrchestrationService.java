package com.userService.service;

import com.userService.persistence.entity.UserBirthdayEntity;
import com.userService.persistence.entity.UserEntity;
import com.userService.persistence.repository.UserBirthdayRepository;
import com.userService.persistence.repository.UserRepository;
import com.userService.presentation.controller.AuthClient;
import com.userService.presentation.dto.CreateAuthRequest;
import com.userService.presentation.dto.CreateUserRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserOrchestrationService {

    private final UserRepository userRepository;
    private final UserBirthdayRepository userBirthdayRepository;
    private final AuthClient authClient;

    @Transactional
    public void createUser(CreateUserRequest request) {

        UserEntity user = userRepository.save(
                UserEntity.from(request)
        );

        userBirthdayRepository.save(
                new UserBirthdayEntity(user.getId(), request.birthday())
        );

        try {
            authClient.createAuth(
                    new CreateAuthRequest(
                            user.getId(),
                            request.email(),
                            request.password(),
                            request.role()
                    )
            );
        } catch (Exception ex) {
            userRepository.deleteById(user.getId());

            throw new RuntimeException(
                    "Error creando credenciales de autenticación",
                    ex
            );
        }
    }
}

