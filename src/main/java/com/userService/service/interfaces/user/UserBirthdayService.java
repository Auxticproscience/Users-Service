package com.userService.service.interfaces.user;

import com.userService.presentation.dto.user.response.UserBirthdayResponse;
import java.util.List;

public interface UserBirthdayService{
    List<UserBirthdayResponse> getAllUserBirthdays();
}
