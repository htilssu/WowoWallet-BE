package com.wowo.wowo.otp;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class OTPVerifyDto extends OTPData {

    @NotNull
    String otp;
    String type;
}
