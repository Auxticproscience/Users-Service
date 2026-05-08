package com.userService.service.implementation.user;

import com.userService.persistence.repository.user.UserBirthdayRepository;
import com.userService.presentation.dto.user.response.UserBirthdayResponse;
import com.userService.service.interfaces.user.UserBirthdayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
