package com.wowo.wowo.otp;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.wowo.wowo.constant.Constant.OTPType;
import com.wowo.wowo.data.dto.OTPRequestDTO;
import com.wowo.wowo.data.dto.OTPSendDTO;
import com.wowo.wowo.data.dto.OTPVerifyDTO;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.otp.OTPFactory.OTPChannel;
import com.wowo.wowo.otp.storage.OTPRepository;
import com.wowo.wowo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Lớp quản lý chức năng OTP, điều phối việc tạo, gửi và xác minh OTP
 */
@RequiredArgsConstructor
@Service
public class OTPManager {

    private final OTPGenerator otpGenerator;
    private final OTPFactory otpFactory;
    private final UserRepository userRepository;
    private final OTPRepository otpRepository;

    @Autowired
    @Qualifier("emailOTPSender")
    private OTPSender emailOTPSender;

    @Autowired
    @Qualifier("smsOTPSender")
    private OTPSender smsOTPSender;

    /**
     * Phương thức thống nhất để tạo và gửi mã OTP cho người dùng.
     * Phương thức này sẽ tự động xác định loại OTP và cách thức gửi dựa trên thông
     * tin đầu vào.
     *
     * @param otpRequestDTO  DTO chứa thông tin yêu cầu OTP
     * @param authentication thông tin xác thực của người dùng
     * @param channel        kênh gửi OTP (EMAIL, SMS)
     *
     * @return true nếu gửi thành công, false nếu thất bại
     */
    public boolean send(OTPRequestDTO otpRequestDTO,
            Authentication authentication,
            OTPChannel channel) {
        try {
            // Lấy thông tin người dùng từ authentication
            String userId = extractUserIdFromAuthentication(authentication);
            String role = extractRoleFromAuthentication(authentication);

            // Lấy recipient từ DTO hoặc từ người dùng
            String recipient = otpRequestDTO.getRecipient();
            if (recipient == null || recipient.isEmpty()) {
                var user = userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundException(role + " không tìm thấy"));

                // Chọn phương thức liên hệ dựa trên kênh gửi
                if (channel == OTPChannel.EMAIL) {
                    recipient = user.getEmail();
                    if (recipient == null || recipient.isEmpty()) {
                        throw new IllegalArgumentException("Không có email để gửi OTP");
                    }
                } else { // SMS
                    recipient = user.getPhoneNumber();
                    if (recipient == null || recipient.isEmpty()) {
                        throw new IllegalArgumentException("Không có số điện thoại để gửi SMS");
                    }
                }
            }

            // Lấy loại OTP từ DTO
            OTPType otpType = otpRequestDTO.getOtpType();

            // Tạo OTP phù hợp với loại và kênh gửi
            OTP otp = otpFactory.createOTP(otpType, userId, recipient, channel);

            // Tạo mã OTP dựa trên loại OTP
            String otpCode = otpGenerator.generateOTP(otpType)
                    .get();
            otp.setCode(otpCode);

            // Lưu OTP vào repository
            otpRepository.save(otp);

            // Gửi OTP trực tiếp thông qua đối tượng OTP
            return otp.send();

        } catch (ExecutionException | InterruptedException e) {
            Logger.getAnonymousLogger()
                    .log(Level.SEVERE, "Lỗi khi tạo OTP: " + e.getMessage(), e);
            return false;
        } catch (Exception e) {
            Logger.getAnonymousLogger()
                    .log(Level.SEVERE, "Lỗi khi gửi OTP: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Phương thức để xác minh OTP
     *
     * @param userId  ID người dùng
     * @param otpCode mã OTP cần xác minh
     * @param otpType loại OTP
     *
     * @return true nếu OTP hợp lệ, false nếu không hợp lệ
     */
    public boolean verify(String userId, String otpCode, OTPType otpType) {
        try {
            // Tìm OTP từ repository
            Optional<OTP> otpOptional = otpRepository.findByUserIdAndType(userId, otpType);

            if (otpOptional.isEmpty()) {
                return false; // Không tìm thấy OTP
            }

            OTP otp = otpOptional.get();

            // Kiểm tra OTP đã hết hạn chưa
            if (otp.isExpired()) {
                return false;
            }

            // Xác minh mã OTP
            boolean isValid = otp.verify(otpCode, otpGenerator);

            // Nếu OTP hợp lệ, xóa nó để không thể sử dụng lại
            if (isValid) {
                otpRepository.deleteByUserIdAndType(userId, otpType);
            }

            return isValid;

        } catch (Exception e) {
            Logger.getAnonymousLogger()
                    .log(Level.SEVERE, "Lỗi khi xác minh OTP: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Phương thức xác minh OTP từ OTPVerifyDTO
     * 
     * @param authentication thông tin xác thực người dùng
     * @param verifyDTO      DTO chứa thông tin xác minh OTP
     * @return true nếu OTP hợp lệ, false nếu không hợp lệ
     */
    public boolean verify(Authentication authentication, OTPVerifyDTO verifyDTO) {
        try {
            // Lấy userId từ authentication
            String userId = extractUserIdFromAuthentication(authentication);

            // Lấy OTPType trực tiếp từ DTO
            OTPType otpType = verifyDTO.getOtpType();

            // Xác minh OTP
            return verify(userId, verifyDTO.getOtpCode(), otpType);
        } catch (Exception e) {
            Logger.getAnonymousLogger()
                    .log(Level.SEVERE, "Lỗi khi xác minh OTP: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Phương thức gửi OTP từ OTPSendDTO
     * 
     * @param sendDTO        DTO chứa thông tin gửi OTP
     * @param authentication thông tin xác thực của người dùng
     * @return true nếu gửi thành công, false nếu thất bại
     */
    public boolean send(OTPSendDTO sendDTO, Authentication authentication) {
        try {
            // Lấy userId từ authentication
            String userId = extractUserIdFromAuthentication(authentication);

            // Tạo OTPRequestDTO từ OTPSendDTO
            OTPRequestDTO requestDTO = new OTPRequestDTO();
            requestDTO.setRecipient(sendDTO.getRecipient());

            // Lấy OTPType từ sendDTO
            OTPType otpType = sendDTO.getOtpType();
            requestDTO.setOtpType(otpType);
            requestDTO.setSendChannel(sendDTO.getSendMethod());

            // Gửi OTP với kênh đã chọn
            return send(requestDTO, authentication, requestDTO.getSendChannel());
        } catch (Exception e) {
            Logger.getAnonymousLogger()
                    .log(Level.SEVERE, "Lỗi khi gửi OTP: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Chuyển đổi chuỗi thành OTPType
     * 
     * @param typeStr chuỗi biểu diễn loại OTP
     * @return OTPType tương ứng
     */
    private OTPType convertToOTPType(String typeStr) {
        try {
            return OTPType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Loại OTP không hợp lệ: " + typeStr);
        }
    }

    /**
     * Trích xuất userId từ authentication object
     * Xử lý trường hợp từ TokenFilter và ApiServiceFilter
     */
    private String extractUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null) {
            throw new SecurityException("Không có thông tin xác thực");
        }

        // Lấy principal là userId từ authentication
        return authentication.getPrincipal().toString();
    }

    /**
     * Trích xuất role từ authentication object
     * Xử lý trường hợp từ TokenFilter và ApiServiceFilter
     */
    private String extractRoleFromAuthentication(Authentication authentication) {
        if (authentication == null) {
            throw new SecurityException("Không có thông tin xác thực");
        }

        // Kiểm tra nếu là ApiServiceFilter (APPLICATION role)
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_APPLICATION"))) {
            return "application";
        }

        // Kiểm tra nếu là TokenFilter (USER hoặc PARTNER role)
        Object details = authentication.getDetails();
        if (details instanceof DecodedJWT) {
            DecodedJWT decodedJWT = (DecodedJWT) details;
            return decodedJWT.getClaim("role").asString();
        }

        // Trường hợp mặc định
        return "unknown";
    }
}
