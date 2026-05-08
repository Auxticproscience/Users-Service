package com.userService.presentation.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBirthdayResponse {
    private String firstName;
    private String lastName;
    private String sede;
    private LocalDate birthdayDate;
}
