package com.userService.service.implementation;

import com.userService.persistence.repository.UserBirthdayRepository;
import com.userService.presentation.dto.UserBirthdayResponse;
import com.userService.service.interfaces.UserBirthdayService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class UserBirthdayServiceImpl implements UserBirthdayService {

    private final UserBirthdayRepository repo;

    @Override
    public List<UserBirthdayResponse> getAllUserBirthdays() {
        return repo.findAllBirthdays().stream()
                .map(v -> new UserBirthdayResponse(
                        v.getFirstName(),
                        v.getLastName(),
                        v.getSede(),
                        v.getBirthdayDate()
                ))
                .toList();
    }
}
