package com.wowo.wowo.controller;

import com.wowo.wowo.annotation.authorized.IsUser;
import com.wowo.wowo.data.dto.TransferDto;
import com.wowo.wowo.kafka.producer.NotifyProducer;
import com.wowo.wowo.kafka.producer.TransferProducer;
import com.wowo.wowo.model.WalletTransaction;
import com.wowo.wowo.service.TransferService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("v2/transfer")
@IsUser
@Tag(name = "Transfer", description = "Chuyển tiền")
public class TransferControllerV2 {

    private final TransferService transferService;
    private final TransferProducer transferProducer;
    private final NotifyProducer notifyProducer;

    @PostMapping
    public ResponseEntity<?> transfer(@Validated @RequestBody TransferDto data,
            Authentication authentication) {
        final WalletTransaction walletTransaction = transferService.transferWithLimit(data, authentication);
        transferProducer.sendTransferMessage(data);
        notifyProducer.pushNotifyMessage(walletTransaction);
        //        emailService.sendEmail();
        //TODO: send email
        return ResponseEntity.ok(walletTransaction.getTransaction());
    }

}
