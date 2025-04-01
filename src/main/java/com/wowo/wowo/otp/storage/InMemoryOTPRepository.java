package com.wowo.wowo.otp.storage;

import com.wowo.wowo.constant.Constant.OTPType;
import com.wowo.wowo.otp.OTP;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Triển khai của OTPRepository sử dụng HashMap để lưu trữ OTP trong bộ nhớ
 * Thích hợp cho triển khai đơn giản hoặc môi trường test
 * Sử dụng ConcurrentHashMap để đảm bảo thread-safe
 */
@Component
public class InMemoryOTPRepository implements OTPRepository {

    // Key: userId_otpType (e.g. "123_TRANSACTION_CONFIRMATION")
    private final Map<String, OTP> otpStore = new ConcurrentHashMap<>();

    @Override
    public void save(OTP otp) {
        String key = generateKey(otp.getUserId(), otp.getOtpType());
        otpStore.put(key, otp);
    }

    @Override
    public Optional<OTP> findByUserIdAndType(String userId, OTPType otpType) {
        String key = generateKey(userId, otpType);
        OTP otp = otpStore.get(key);

        if (otp != null && otp.isExpired()) {
            otpStore.remove(key);
            return Optional.empty();
        }

        return Optional.ofNullable(otp);
    }

    @Override
    public Optional<OTP> findByUserIdAndRecipientAndType(String userId, String recipient, OTPType otpType) {
        Optional<OTP> optionalOTP = findByUserIdAndType(userId, otpType);

        return optionalOTP.filter(otp -> otp.getRecipient().equals(recipient));
    }

    @Override
    public void deleteByUserIdAndType(String userId, OTPType otpType) {
        String key = generateKey(userId, otpType);
        otpStore.remove(key);
    }

    @Override
    public void deleteExpiredOTPs() {
        Map<String, OTP> expiredOTPs = otpStore.entrySet().stream()
                .filter(entry -> entry.getValue().isExpired())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        expiredOTPs.keySet().forEach(otpStore::remove);
    }

    /**
     * Tạo key cho Map từ userId và otpType
     */
    private String generateKey(String userId, OTPType otpType) {
        return userId + "_" + otpType.name();
    }
}