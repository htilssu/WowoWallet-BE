package com.wowo.wowo.otp;

import com.wowo.wowo.constants.Constant;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@Data
public class OtpSendDto extends OTPData {

    @NotNull
    private Constant.OTPService type;
    @Setter
    @NotNull
    private String sendTo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OtpSendDto that = (OtpSendDto) o;
        return getType() == that.getType() && Objects.equals(getSendTo(), that.getSendTo());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(getType());
        result = 31 * result + Objects.hashCode(getSendTo());
        return result;
    }
}
