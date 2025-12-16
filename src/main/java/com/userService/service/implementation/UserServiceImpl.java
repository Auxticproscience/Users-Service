package com.userService.service.implementation;

import com.userService.persistence.entity.UserEntity;
import com.userService.persistence.repository.UserRepository;
import com.userService.presentation.dto.CreateUserRequest;
import com.userService.presentation.dto.UpdateUserRequest;
import com.userService.presentation.dto.UserResponse;
import com.userService.service.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;

    @Override
    public UUID create(CreateUserRequest req) {
        UserEntity user = UserEntity.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .email(req.email())
                .phone(req.phone())
                .positionTitle(req.position())
                .sede(req.sede())
                .area(req.area())
                .build();
        user = repo.save(user);
        return user.getId();
    }

    @Override
    public UserResponse findById(UUID id) {
        UserEntity user = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return toDto(user);
    }

    @Override
    public UserResponse update(UUID id, UpdateUserRequest req) {
        UserEntity user = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (req.firstName() != null) user.setFirstName(req.firstName());
        if (req.lastName() != null) user.setLastName(req.lastName());
        if (req.phone() != null) user.setPhone(req.phone());
        if (req.position() != null) user.setPositionTitle(req.position());
        if (req.area() != null) user.setArea(req.area());
        repo.save(user);
        return toDto(user);
    }

    @Override
    public List<UserResponse> getAll() {
        return repo.findAll()
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
                user.getArea());
    }
}
