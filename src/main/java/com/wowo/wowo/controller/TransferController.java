package com.wowo.wowo.controller;

import com.wowo.wowo.annotation.authorized.IsUser;
import com.wowo.wowo.constant.Constant;
import com.wowo.wowo.data.dto.ResponseMessage;
import com.wowo.wowo.data.dto.TransactionRequest;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.model.Wallet;
import com.wowo.wowo.model.WalletOwnerType;
import com.wowo.wowo.otp.OTPManager;
import com.wowo.wowo.data.dto.OTPVerifyDto;
import com.wowo.wowo.data.dto.OtpSendDto;
import com.wowo.wowo.repository.ConstantRepository;
import com.wowo.wowo.repository.WalletRepository;
import com.wowo.wowo.service.EmailService;
import com.wowo.wowo.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = "v1/transfer", produces = "application/json; charset=UTF-8")
@Tag(name = "Transfer", description = "Chuyển tiền")
public class TransferController {

    private final ConstantRepository constantRepository;
    private final OTPManager otpManager;
    private final UserService userService;
    private final EmailService emailService;
    WalletRepository walletRepository;

    @PostMapping
    public ResponseEntity<?> transfer(@Validated @RequestBody TransactionRequest data,
            Authentication authentication) {

        String senderId = authentication.getName();
        return handleTransfer(senderId, data);
    }

    private ResponseEntity<?> handleTransfer(String senderId, TransactionRequest data) {
        return switch (data.getTransactionTarget()) {
            case "wallet" -> transferToWallet(senderId, data);
            case "fund" -> ResponseEntity.ok()
                    .body(new ResponseMessage(
                            "Chức năng chuyển tiền vào quỹ chưa được hỗ trợ"));
            //                return transferToEmail(senderId, data);
            default -> ResponseEntity.badRequest()
                    .body(new ResponseMessage("Loại ví không hợp lệ"));
        };
    }

    private ResponseEntity<?> transferToWallet(String senderId, TransactionRequest data) {
        final com.wowo.wowo.model.Constant minimumTransferConstant =
                constantRepository.findById(
                        Constant.MINIMUM_TRANSFER_AMOUNT).orElseThrow();

        final com.wowo.wowo.model.Constant maximumTransferConstant = constantRepository.findById(
                Constant.MAXIMUM_TRANSFER_AMOUNT).orElseThrow();

        if (data.getMoney() < minimumTransferConstant.getValue()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Số tiền chuyển phải lớn hơn "
                            + minimumTransferConstant.getValue()));
        }

        if (data.getMoney() > maximumTransferConstant.getValue()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Số tiền chuyển phải nhỏ hơn "
                            + minimumTransferConstant.getValue()));
        }


        Optional<Wallet> optionalSenderWallet = walletRepository.findByOwnerIdAndOwnerType(senderId,
                WalletOwnerType.USER);

        if (optionalSenderWallet.isEmpty()) {
            return ResponseEntity.ok()
                    .body(new ResponseMessage("Không tìm thấy ví của người gửi"));
        }

        Wallet senderWallet = optionalSenderWallet.get();

        if (senderWallet.getBalance() < data.getMoney() || senderWallet.getBalance()
                <= 0) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Số dư không đủ"));
        }


        return ResponseEntity.ok().build();
    }

    @GetMapping("/check/{id}")
    public ResponseEntity<?> checkUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserByIdOrUsernameOrEmail(id, id, id));
    }

    @PostMapping("/send-otp")
    @IsUser
    public ResponseEntity<?> sendOtp(@RequestBody @Validated OtpSendDto data,
            Authentication authentication) {
        otpManager.send(emailService, data, authentication);
        return ResponseEntity.ok(new ResponseMessage("Gửi mã otp thành công"));
    }

    @PostMapping("/verify-otp")
    @IsUser
    public ResponseEntity<?> verifyOtp(@RequestBody @Validated OTPVerifyDto data,
            Authentication authentication) {
        final boolean verified = otpManager.verify(((String) authentication.getPrincipal()), data);
        if (!verified) {
            throw new BadRequest("OTP không hợp lệ");
        }
        return ResponseEntity.ok(new ResponseMessage("Xác thực thành công"));
    }
}
