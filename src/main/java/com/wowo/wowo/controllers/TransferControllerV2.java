package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.data.dto.TransferDto;
import com.wowo.wowo.kafka.producers.TransferProducer;
import com.wowo.wowo.models.WalletTransaction;
import com.wowo.wowo.services.TransferService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<?> transfer(@Validated @RequestBody TransferDto data) {
        final WalletTransaction walletTransaction = transferService.transfer(data);
        transferProducer.sendTransferMessage(data);
        //        emailService.sendEmail();
        //TODO: send email
        return ResponseEntity.ok(walletTransaction.getTransaction());
    }

}
