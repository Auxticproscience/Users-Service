package com.userService.service.interfaces.user;

import java.time.LocalDate;

public interface UserBirthdayView {
    String getFirstName();
    String getLastName();
    String getSede();
    LocalDate getBirthdayDate();
}
