package com.userService.service.implementation;

import com.userService.persistence.repository.UserBirthdayRepository;
import com.userService.presentation.dto.UserBirthdayResponse;
import com.userService.service.interfaces.UserBirthdayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserBirthdayServiceImpl implements UserBirthdayService {

    private final UserBirthdayRepository userBirthdayRepository;

    @Override
    public List<UserBirthdayResponse> getAllUserBirthdays() {
        return userBirthdayRepository.findAllRaw().stream()
                .map(row -> new UserBirthdayResponse(
                        safeToString(row[0]),
                        safeToString(row[1]),
                        safeToString(row[2]),
                        parseToLocalDate(row[3])
                ))
                .collect(Collectors.toList());
    }

    private String safeToString(Object obj) {
        return obj != null ? obj.toString() : null;
    }

    private LocalDate parseToLocalDate(Object dateObj) {
        return switch (dateObj) {
            case null -> null;
            case LocalDate localDate -> localDate;
            case Date sqlDate -> sqlDate.toLocalDate();
            case String strDate -> LocalDate.parse(strDate);
            default -> throw new IllegalArgumentException("Tipo de fecha no soportado: " + dateObj.getClass());
        };
    }
}
