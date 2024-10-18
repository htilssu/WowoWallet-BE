package com.wowo.wowo.validator;

import com.wowo.wowo.models.User;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class UserValidator {

    public static boolean isValidateUser(User user) {

        return true;
    }

    private static boolean isUserUnder18(@NotNull LocalDate dob) {
        LocalDate currentDate = LocalDate.now();
        return dob.plusYears(18).isAfter(currentDate);
    }

}
