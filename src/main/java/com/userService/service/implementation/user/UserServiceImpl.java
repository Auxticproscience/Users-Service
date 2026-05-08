package com.userService.service.implementation.user;

import com.userService.exception.UserNotFoundException;
import com.userService.persistence.entity.user.UserEntity;
import com.userService.persistence.enums.AccountStatus;
import com.userService.persistence.repository.user.UserBirthdayRepository;
import com.userService.persistence.repository.user.UserRepository;
import com.userService.presentation.dto.auth.request.CreateAuthRequest;
import com.userService.presentation.dto.user.request.CreateUserRequest;
import com.userService.presentation.dto.user.request.UpdateUserRequest;
import com.userService.presentation.dto.user.response.UserResponse;
import com.userService.service.interfaces.user.UserService;
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

    @Override
    public UserResponse create(CreateUserRequest req) {

        UserEntity user = userRepository.save(
                UserEntity.builder()
                        .firstName(req.firstName())
                        .lastName(req.lastName())
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
    public void delete(UUID id) {
        UserEntity user = userRepository.findByIdAndStatus(id, AccountStatus.ACTIVE)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setStatus(AccountStatus.INACTIVE);
        userRepository.save(user);
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
                user.getPhone(),
                user.getPositionTitle(),
                user.getSede(),
                user.getArea(),
                user.getStatus()
        );
    }
}


