package com.wowo.wowo.otp;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.wowo.wowo.constant.Constant.OTPType;
import com.wowo.wowo.data.MailContent;
import com.wowo.wowo.data.dto.OTPRequestDTO;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.otp.OTPFactory.OTPChannel;
import com.wowo.wowo.otp.storage.OTPRepository;
import com.wowo.wowo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            DecodedJWT decodedJWT = (DecodedJWT) authentication.getDetails();
            String userId = authentication.getPrincipal()
                    .toString();
            String role = decodedJWT.getClaim("role")
                    .asString();

            // Xác thực quyền của người dùng
            if (!userId.equals(otpRequestDTO.getUserId())) {
                throw new SecurityException("User ID không khớp với người dùng hiện tại");
            }

            // Lấy email hoặc số điện thoại dựa vào channel
            String recipient;
            var user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException(role + " không tìm thấy"));

            // Chọn phương thức liên hệ dựa trên kênh gửi
            if (channel == OTPChannel.EMAIL) {
                recipient = user.getEmail();
                if (recipient == null || recipient.isEmpty()) {
                    throw new IllegalArgumentException("Không có email để gửi OTP");
                }
            }
            else { // SMS
                recipient = user.getPhoneNumber();
                if (recipient == null || recipient.isEmpty()) {
                    throw new IllegalArgumentException("Không có số điện thoại để gửi SMS");
                }
            }

            // Lấy loại OTP từ DTO
            OTPType otpType = otpRequestDTO.getOtpType();

            // Kiểm tra xem có phải OTP giao dịch không
            boolean isTransactionRelated = otpType == OTPType.TRANSACTION_CONFIRMATION ||
                    otpType == OTPType.WITHDRAW_CONFIRMATION;

            // Tạo OTP phù hợp với loại và kênh gửi
            OTP otp;
            if (isTransactionRelated) {
                String transactionId = otpRequestDTO.getTransactionId();
                if (transactionId == null || transactionId.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Transaction ID là bắt buộc cho OTP giao dịch");
                }
                otp = otpFactory.createOTP(otpType, userId, recipient, transactionId, channel);
            }
            else {
                otp = otpFactory.createOTP(otpType, userId, recipient, channel);
            }

            // Tạo mã OTP
            String otpCode = otpGenerator.generateOTP()
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
     * Phương thức mặc định để gửi OTP qua email
     */
    public boolean send(OTPRequestDTO otpRequestDTO, Authentication authentication) {
        return send(otpRequestDTO, authentication, OTPChannel.EMAIL);
    }

    /**
     * Phương thức để xác minh OTP, xử lý cả OTP thông thường và OTP giao dịch
     *
     * @param userId        ID người dùng
     * @param otpCode       mã OTP cần xác minh
     * @param otpType       loại OTP
     * @param transactionId ID giao dịch (tùy chọn, cần thiết cho OTP giao dịch)
     *
     * @return true nếu OTP hợp lệ, false nếu không hợp lệ
     */
    public boolean verify(String userId, String otpCode, OTPType otpType, String transactionId) {
        try {
            // Xác định loại OTP
            boolean isTransactionRelated = otpType == OTPType.TRANSACTION_CONFIRMATION ||
                    otpType == OTPType.WITHDRAW_CONFIRMATION;

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

            // Đối với OTP giao dịch, cần kiểm tra thêm transactionId
            if (isTransactionRelated) {
                if (transactionId == null || transactionId.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Transaction ID là bắt buộc để xác minh OTP giao dịch");
                }

                if (!(otp instanceof TransactionOTP transactionOTP)) {
                    return false; // Không phải TransactionOTP
                }

                // Kiểm tra ID giao dịch có khớp không
                if (!transactionOTP.getTransactionId()
                        .equals(transactionId)) {
                    return false;
                }
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
     * Phương thức xác minh OTP thông thường (không liên quan đến giao dịch)
     */
    public boolean verify(String userId, String otpCode, OTPType otpType) {
        boolean isTransactionRelated = otpType == OTPType.TRANSACTION_CONFIRMATION ||
                otpType == OTPType.WITHDRAW_CONFIRMATION;

        if (isTransactionRelated) {
            throw new IllegalArgumentException(
                    "Transaction ID là bắt buộc để xác minh OTP giao dịch");
        }

        return verify(userId, otpCode, otpType, null);
    }

    // Phương thức hỗ trợ

    /**
     * Lấy tiêu đề dựa trên loại OTP
     */
    private String getSubject(OTPType otpType, boolean isTransactionRelated) {
        if (isTransactionRelated) {
            return switch (otpType) {
                case WITHDRAW_CONFIRMATION -> "Xác nhận rút tiền - Mã xác thực OTP";
                case TRANSACTION_CONFIRMATION -> "Xác nhận giao dịch - Mã xác thực OTP";
                default -> "Mã xác thực OTP giao dịch";
            };
        }
        else {
            return switch (otpType) {
                case PASSWORD_RESET -> "Đặt lại mật khẩu - Mã xác thực OTP";
                case EMAIL_VERIFICATION -> "Xác minh email - Mã xác thực OTP";
                case ACCOUNT_VERIFICATION -> "Xác minh tài khoản - Mã xác thực OTP";
                default -> "Mã xác thực OTP";
            };
        }
    }

    /**
     * Lấy nội dung email/SMS dựa trên loại OTP
     */
    private String getContent(String otpCode,
            OTPType otpType,
            boolean isTransactionRelated,
            String transactionId) {
        if (isTransactionRelated) {
            String baseTemplate = MailContent.TRANSACTION_OTP_BODY
                    .replace("{{OTP}}", otpCode)
                    .replace("{{TRANSACTION_ID}}", transactionId);

            // Thêm thông báo tùy theo loại OTP giao dịch
            String messageByType = switch (otpType) {
                case WITHDRAW_CONFIRMATION -> "để xác nhận yêu cầu rút tiền";
                case TRANSACTION_CONFIRMATION -> "để xác nhận giao dịch";
                default -> "để xác nhận thao tác tài chính";
            };

            return baseTemplate.replace("{{MESSAGE}}", messageByType);
        }
        else {
            String baseTemplate = MailContent.OTP_BODY.replace("{{OTP}}", otpCode);

            // Thêm thông báo tùy theo loại OTP
            String messageByType = switch (otpType) {
                case PASSWORD_RESET -> "để đặt lại mật khẩu cho tài khoản của bạn.";
                case EMAIL_VERIFICATION -> "để xác minh địa chỉ email của bạn.";
                case ACCOUNT_VERIFICATION -> "để xác minh tài khoản của bạn.";
                default -> "để xác nhận thao tác của bạn.";
            };

            return baseTemplate.replace("{{MESSAGE}}", messageByType);
        }
    }
}
