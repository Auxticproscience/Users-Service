package com.userService.persistence.repository.user;

import com.userService.persistence.entity.user.UserEntity;
import com.userService.persistence.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    List<UserEntity> findAllByStatus(AccountStatus status);

    Optional<UserEntity> findByIdAndStatus(UUID id, AccountStatus status);
}

