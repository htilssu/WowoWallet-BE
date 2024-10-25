package com.wowo.wowo.otp;

import com.wowo.wowo.constants.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class OtpSendDto extends OTPData {

    private Constant.OTPService type;
    @Setter
    private String sendTo;
}
