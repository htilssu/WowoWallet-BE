package com.wowo.wowo.otp;

import com.wowo.wowo.constant.Constant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class OTPData {

    String otp;
    String userId;
    Constant.OTPService type;
}