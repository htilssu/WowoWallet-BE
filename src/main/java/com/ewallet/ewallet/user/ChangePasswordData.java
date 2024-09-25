package com.ewallet.ewallet.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordData {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
