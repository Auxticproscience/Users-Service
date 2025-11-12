package com.userService.service.interfaces;

import com.userService.presentation.dto.UserBirthdayResponse;
import java.util.List;

public interface UserBirthdayService {
    List<UserBirthdayResponse> getAllUserBirthdays();
}
