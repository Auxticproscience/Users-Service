package com.userService.persistence.repository;

import com.userService.persistence.entity.UserEntity;
import com.userService.service.interfaces.UserBirthdayView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserBirthdayRepository extends JpaRepository<UserEntity, UUID>{
    @Query(value = """
    SELECT 
      first_name AS firstName,
      last_name AS lastName,
      sede AS sede,
      birthday_date AS birthdayDate
    FROM user_birthdays
""", nativeQuery = true)
    List<UserBirthdayView> findAllBirthdays();

    @Modifying
    @Query(value = """
        INSERT INTO birthdays (user_id, birthday_date)
        VALUES (:userId, :birthday)
        """, nativeQuery = true)
    void insertBirthday(UUID userId, LocalDate birthday);
}
