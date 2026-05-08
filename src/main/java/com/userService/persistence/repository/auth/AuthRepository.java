package com.userService.persistence.repository.auth;

import com.userService.persistence.entity.auth.AuthCredential;
import com.userService.persistence.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<AuthCredential, Long> {

    Optional<AuthCredential> findByEmail(String email);
    Optional<AuthCredential> findByUserId(UUID userId);

    List<AuthCredential> findAllByStatus(AccountStatus status);
    Optional<AuthCredential> findByUserIdAndStatus(UUID userId, AccountStatus status);
    Optional<AuthCredential> findByEmailAndStatus(String email, AccountStatus status);

    boolean existsByEmail(String email);
}
