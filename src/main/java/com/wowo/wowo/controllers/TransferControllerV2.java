package com.wowo.wowo.controllers;

import com.wowo.wowo.data.dto.TransferDto;
import com.wowo.wowo.services.EmailService;
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
@Tag(name = "Transfer", description = "Chuyển tiền")
public class TransferControllerV2 {

    private final TransferService transferService;
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<?> transfer(@Validated @RequestBody TransferDto data) {
        transferService.transfer(data);
        emailService.sendEmail();
        return ResponseEntity.ok().build();
    }

}
