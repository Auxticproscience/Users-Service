package com.userService.service.interfaces;

import java.time.LocalDate;

public interface UserBirthdayView {
    String getFirstName();
    String getLastName();
    String getSede();
    LocalDate getBirthdayDate();
}
