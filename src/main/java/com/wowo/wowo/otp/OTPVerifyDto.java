package com.wowo.wowo.otp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class OTPVerifyDto extends OTPData {
    String otp;
    String type;
}
