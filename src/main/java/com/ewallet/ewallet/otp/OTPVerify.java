package com.ewallet.ewallet.otp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OTPVerify extends OTPData {
    String type;
    String otp;
}
