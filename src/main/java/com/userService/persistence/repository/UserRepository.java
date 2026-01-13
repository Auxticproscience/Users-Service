package com.userService.persistence.repository;

import com.userService.persistence.entity.UserEntity;
import com.userService.presentation.dto.UserBirthdayResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Query("""
        SELECT new com.userService.presentation.dto.UserBirthdayResponse(
            u.firstName,
            u.lastName,
            u.sede,
            b.birthdayDate
        )
        FROM UserEntity u
        JOIN UserBirthdayEntity b ON u.id = b.userId
        ORDER BY b.birthdayDate
    """)
    List<UserBirthdayResponse> findAllUserBirthdays();
}

