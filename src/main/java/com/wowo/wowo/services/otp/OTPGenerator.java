package com.wowo.wowo.services.otp;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.SecretGenerator;
import com.bastiaanjansen.otp.TOTPGenerator;
import lombok.Getter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
public class OTPGenerator {
    byte[] secret = SecretGenerator.generate();

    @Getter
    TOTPGenerator totp = new TOTPGenerator.Builder(secret)
            .withHOTPGenerator(builder -> {
                builder.withPasswordLength(6);
                builder.withAlgorithm(HMACAlgorithm.SHA512);
            })
            .withPeriod(Duration.ofSeconds(30))
            .build();

    /**
     * Tạo mã OTP mới và trả về 1 {@link CompletableFuture} chứa mã OTP
     * để lấy được mã OTP, sử dụng phương thức {@link CompletableFuture#get()}
     * hoặc nếu muốn xử lý bất đồng bộ thì sử dụng phương thức {@link CompletableFuture#thenAccept(java.util.function.Consumer)}
     * truyền vào một {@link java.util.function.Consumer} để xử lý mã OTP
     * @return mã otp
     */
    @Async
    public CompletableFuture<String> generateOTP() {
        var otp = totp.now();
        return CompletableFuture.completedFuture(otp);
    }

    /**
     * Xác thực OTP có đúng hay không dựa vào tham số OTP được truyền vào,
     * nếu đúng thì trả về {@code  true}, ngược lại trả về {@code false}
     * @param otpCode mã OTP cần xác thực
     * @return {@code true} nếu mã OTP đúng, ngược lại trả về {@code false}
     */
   public boolean verify(String otpCode){
       return totp.verify(otpCode);
   }
}
