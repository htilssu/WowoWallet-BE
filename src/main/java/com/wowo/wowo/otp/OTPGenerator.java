package com.wowo.wowo.otp;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.SecretGenerator;
import com.bastiaanjansen.otp.TOTPGenerator;
import com.wowo.wowo.constant.Constant.OTPType;
import lombok.Getter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class OTPGenerator {
    private final byte[] secret = SecretGenerator.generate();

    // Cache của các TOTP generators theo thời gian hết hạn
    private final Map<Integer, TOTPGenerator> totpGenerators = new HashMap<>();

    // TOTP generator mặc định với period 30 giây
    @Getter
    private final TOTPGenerator defaultTotp;

    public OTPGenerator() {
        defaultTotp = createTOTPGenerator(30);
    }

    /**
     * Tạo mới một TOTPGenerator với thời gian period được chỉ định
     * 
     * @param periodInSeconds thời gian tính bằng giây cho mỗi khoảng thời gian OTP
     * @return một TOTPGenerator mới
     */
    private TOTPGenerator createTOTPGenerator(int periodInSeconds) {
        return new TOTPGenerator.Builder(secret)
                .withHOTPGenerator(builder -> {
                    builder.withPasswordLength(6);
                    builder.withAlgorithm(HMACAlgorithm.SHA512);
                })
                .withPeriod(Duration.ofSeconds(periodInSeconds))
                .build();
    }

    /**
     * Lấy TOTPGenerator cho loại OTP cụ thể, tạo mới nếu chưa tồn tại
     * 
     * @param otpType loại OTP cần tạo generator
     * @return TOTPGenerator phù hợp với loại OTP
     */
    private TOTPGenerator getTOTPGenerator(OTPType otpType) {
        // Chuyển đổi từ phút sang giây
        int periodInSeconds = otpType.getExpirationTimeInMinutes() * 60;

        // Nếu đã có trong cache thì trả về
        if (totpGenerators.containsKey(periodInSeconds)) {
            return totpGenerators.get(periodInSeconds);
        }

        // Tạo mới nếu chưa có
        TOTPGenerator generator = createTOTPGenerator(periodInSeconds);
        totpGenerators.put(periodInSeconds, generator);
        return generator;
    }

    /**
     * Tạo mã OTP mới sử dụng generator mặc định và trả về CompletableFuture chứa mã
     * OTP
     * 
     * @return CompletableFuture chứa mã OTP
     */
    @Async
    public CompletableFuture<String> generateOTP() {
        var otp = defaultTotp.now();
        return CompletableFuture.completedFuture(otp);
    }

    /**
     * Tạo mã OTP mới dựa vào loại OTP và thời gian hết hạn tương ứng
     * 
     * @param otpType loại OTP cần tạo mã
     * @return CompletableFuture chứa mã OTP
     */
    @Async
    public CompletableFuture<String> generateOTP(OTPType otpType) {
        TOTPGenerator generator = getTOTPGenerator(otpType);
        var otp = generator.now();
        return CompletableFuture.completedFuture(otp);
    }

    /**
     * Xác thực OTP có đúng hay không dựa vào tham số OTP được truyền vào,
     * sử dụng generator mặc định
     * 
     * @param otpCode mã OTP cần xác thực
     * @return {@code true} nếu mã OTP đúng, ngược lại trả về {@code false}
     */
    public boolean verify(String otpCode) {
        return defaultTotp.verify(otpCode);
    }

    /**
     * Xác thực OTP có đúng hay không dựa vào tham số OTP và loại OTP
     * 
     * @param otpCode mã OTP cần xác thực
     * @param otpType loại OTP để xác định thời gian hết hạn
     * @return {@code true} nếu mã OTP đúng, ngược lại trả về {@code false}
     */
    public boolean verify(String otpCode, OTPType otpType) {
        TOTPGenerator generator = getTOTPGenerator(otpType);
        return generator.verify(otpCode);
    }
}
