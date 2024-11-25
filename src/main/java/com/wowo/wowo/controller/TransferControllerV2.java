package com.wowo.wowo.controller;

import com.wowo.wowo.annotation.authorized.IsUser;
import com.wowo.wowo.data.dto.TransactionDTO;
import com.wowo.wowo.data.dto.TransferDTO;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.kafka.producer.NotifyProducer;
import com.wowo.wowo.kafka.producer.TransferProducer;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.service.TransferService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
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
    private final TransactionMapper transactionMapper;

    @PostMapping
    public TransactionDTO transfer(@Validated @RequestBody TransferDTO data,
            Authentication authentication) {
        final Transaction transaction = transferService.transferWithLimit(data, authentication);
        transferProducer.sendTransferMessage(data);
        notifyProducer.pushNotifyMessage(transaction);
        //        emailService.sendEmail();
        //TODO: send email
        return transactionMapper.toDto(transaction);
    }

}
