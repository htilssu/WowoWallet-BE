package com.wowo.wowo.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordData {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
