package com.userService.presentation.controller;

import com.userService.presentation.dto.UserBirthdayResponse;
import com.userService.service.interfaces.UserBirthdayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/birthdays")
@RequiredArgsConstructor
public class UserBirthdayController {

    private final UserBirthdayService userBirthdayService;

    @GetMapping
    public ResponseEntity<List<UserBirthdayResponse>> getAllBirthdays() {
        List<UserBirthdayResponse> birthdays = userBirthdayService.getAllUserBirthdays();
        return ResponseEntity.ok(birthdays);
    }
}
