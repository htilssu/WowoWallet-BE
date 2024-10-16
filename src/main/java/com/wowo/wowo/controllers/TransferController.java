package com.wowo.wowo.controllers;

import com.wowo.wowo.constants.Constant;
import com.wowo.wowo.data.dto.request.TransactionRequest;
import com.wowo.wowo.data.dto.response.ResponseMessage;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.repositories.*;
import com.wowo.wowo.services.EquityService;
import com.wowo.wowo.services.TransactionService;
import com.wowo.wowo.services.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
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

    private final TransactionService transactionService;
    private final ConstantRepository constantRepository;
    private final EquityService equityService;
    private final UserService userService;
    TransactionRepository transactionRepository;
    UserRepository userRepository;
    WalletRepository walletRepository;
    WalletTransactionRepository walletTransactionRepository;
    TransactionMapper transactionMapper;

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
        final com.wowo.wowo.models.Constant minimumTransferConstant =
                constantRepository.findById(
                        Constant.MINIMUM_TRANSFER_AMOUNT).orElseThrow();

        final com.wowo.wowo.models.Constant maximumTransferConstant = constantRepository.findById(
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
                "user");

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
        return ResponseEntity.ok(userService.getUserByIdOrUsernameOrEmail(id));
    }
}
