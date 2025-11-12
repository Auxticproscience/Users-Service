package com.userService.persistence.repository;

import com.userService.persistence.entity.UserBirthdayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBirthdayRepository extends JpaRepository<UserBirthdayEntity, Long> {

    @Query(value = "SELECT first_name, last_name, sede, birthday_date FROM user_birthdays", nativeQuery = true)
    List<Object[]> findAllRaw();
}
