package com.wowo.wowo.validator;

import java.util.regex.Pattern;

public class EmailValidator {

    private static final String REGEX = "^([a-zA-Z0-9.+]+)@([a-zA-Z0-9]+)\\.([a-zA-Z]{2,5})$";
    private static final Pattern pattern = Pattern.compile(REGEX);

    public static boolean isValid(String email) {
        return pattern.matcher(email).matches();
    }

}
