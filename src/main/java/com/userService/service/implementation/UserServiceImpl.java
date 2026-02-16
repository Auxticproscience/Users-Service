package com.userService.service.implementation;

import com.userService.config.AuthClient;
import com.userService.exception.UserNotFoundException;
import com.userService.persistence.entity.UserEntity;
import com.userService.persistence.enums.AccountStatus;
import com.userService.persistence.repository.UserBirthdayRepository;
import com.userService.persistence.repository.UserRepository;
import com.userService.presentation.dto.CreateAuthRequest;
import com.userService.presentation.dto.CreateUserRequest;
import com.userService.presentation.dto.UpdateUserRequest;
import com.userService.presentation.dto.UserResponse;
import com.userService.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserBirthdayRepository  userBirthdayRepository;
    private final AuthClient authClient;

    @Override
    public UserResponse create(CreateUserRequest req) {

        UserEntity user = userRepository.save(
                UserEntity.builder()
                        .firstName(req.firstName())
                        .lastName(req.lastName())
                        .email(req.email())
                        .phone(req.phone())
                        .positionTitle(req.position())
                        .sede(req.sede())
                        .area(req.area())
                        .build()
        );

        if(req.birthday() != null) {
            userBirthdayRepository.insertBirthday(
                    user.getId(),
                    req.birthday()
            );
        }

        authClient.createAuth(
                new CreateAuthRequest(
                        user.getId(),
                        user.getEmail(),
                        req.password(),
                        req.role() != null ? req.role() : "EMPLOYEE"
                )
        );

        return toDto(user);
    }

    @Override
    public UserResponse findById(UUID id) {
        UserEntity user = userRepository.findByIdAndStatus(id, AccountStatus.ACTIVE)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return toDto(user);
    }

    @Override
    public UserResponse update(UUID id, UpdateUserRequest req) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (req.firstName() != null) user.setFirstName(req.firstName());
        if (req.lastName() != null) user.setLastName(req.lastName());
        if (req.phone() != null) user.setPhone(req.phone());
        if (req.position() != null) user.setPositionTitle(req.position());
        if (req.area() != null) user.setArea(req.area());

        return toDto(user);
    }

    @Override
    public void updateEmail(UUID id, String email) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setEmail(email);
        userRepository.save(user);

        authClient.updateEmail(id, email);
    }


    @Override
    public void delete(UUID id) {
        UserEntity user = userRepository.findByIdAndStatus(id, AccountStatus.ACTIVE)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setStatus(AccountStatus.INACTIVE);
        userRepository.save(user);

        authClient.deleteAuth(user.getId());
    }


    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAllByStatus(AccountStatus.ACTIVE)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private UserResponse toDto(UserEntity user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getPositionTitle(),
                user.getSede(),
                user.getArea(),
                user.getStatus()
        );
    }
}


