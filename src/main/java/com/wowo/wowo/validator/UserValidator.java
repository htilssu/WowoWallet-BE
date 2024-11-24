package com.wowo.wowo.validator;

import com.wowo.wowo.model.User;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class UserValidator {

    public static boolean isValidateUser(User user) {

        return true;
    }

    private static boolean isUserUnder18(@NotNull LocalDate dob) {
        LocalDate currentDate = LocalDate.now();
        return dob.plusYears(18).isAfter(currentDate);
    }

}
