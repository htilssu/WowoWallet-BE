package com.wowo.wowo.controllers;

import com.wowo.wowo.constants.Constant;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.data.dto.request.TransactionRequest;
import com.wowo.wowo.data.dto.response.ResponseMessage;
import com.wowo.wowo.services.EquityService;
import com.wowo.wowo.models.Transaction;
import com.wowo.wowo.models.User;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.models.WalletTransaction;
import com.wowo.wowo.repositories.ConstantRepository;
import com.wowo.wowo.repositories.TransactionRepository;
import com.wowo.wowo.repositories.WalletTransactionRepository;
import com.wowo.wowo.services.TransactionService;
import com.wowo.wowo.repositories.UserRepository;
import com.wowo.wowo.repositories.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = "v1/transfer", produces = "application/json; charset=UTF-8")
public class TransferController {

    private final TransactionService transactionService;
    private final ConstantRepository constantRepository;
    private final EquityService equityService;
    TransactionRepository transactionRepository;
    UserRepository userRepository;
    WalletRepository walletRepository;
    WalletTransactionRepository walletTransactionRepository;
    TransactionMapper transactionMapper;

    @PostMapping
    public ResponseEntity<?> transfer(@RequestBody TransactionRequest data,
            Authentication authentication) {
        if (data.getMoney() <= 0) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Số tiền phải lớn hơn 0"));
        }

        if (data.getSendTo().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Người nhận không được để trống"));
        }

        String senderId = authentication.getName();
        return handleTransfer(senderId, data);
    }

    private ResponseEntity<?> handleTransfer(String senderId, TransactionRequest data) {
        switch (data.getTransactionTarget()) {
            case "wallet":
                return transferToWallet(senderId, data);
            case "fund":
                return ResponseEntity.ok()
                        .body(new ResponseMessage(
                                "Chức năng chuyển tiền vào quỹ chưa được hỗ trợ"));
            //                return transferToEmail(senderId, data);
            default:
                return ResponseEntity.badRequest()
                        .body(new ResponseMessage("Loại ví không hợp lệ"));
        }
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

        if (data.getMoney() > maximumTransferConstant.getValue()){
            return  ResponseEntity.badRequest()
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

        User receiver = userRepository.findByEmail(data.getSendTo());

        if (receiver == null) {
            return ResponseEntity.ok()
                    .body(new ResponseMessage("Không tìm thấy người nhận"));
        }

        Optional<Wallet> optionalReceiverWallet = walletRepository.findByOwnerIdAndOwnerType(
                receiver.getId(), "user");

        if (optionalReceiverWallet.isEmpty()) {
            return ResponseEntity.ok()
                    .body(new ResponseMessage("Không tìm thấy ví của người nhận"));
        }

        Wallet receiverWallet = optionalReceiverWallet.get();

        if (Objects.equals(senderWallet.getId(), receiverWallet.getId())) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage("Không thể chuyển tiền cho chính mình"));
        }

        senderWallet.sendMoneyTo(receiverWallet, data.getMoney());
        Transaction transaction = transactionMapper.toEntity(data);
        transaction.setSenderId(senderId);
        transaction.setReceiverId(receiver.getId());
        transactionRepository.save(transaction);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .build();


        walletTransaction.setTransaction(transaction);
        walletTransactionRepository.save(walletTransaction);

        walletRepository.saveAll(List.of(senderWallet, receiverWallet));


        final Optional<WalletTransaction> walletTransactionOptional =
                walletTransactionRepository.findById(
                        walletTransaction.getId());


        if (walletTransactionOptional.isEmpty()) {
            transaction.setStatus("FAILED");
            transactionRepository.save(transaction);
            return ResponseEntity.ok()
                    .body(new ResponseMessage("Chuyển tiền thất bại"));
        }

        equityService.updateEquity(transaction);

        return ResponseEntity.ok(walletTransactionOptional.get());
    }
}
