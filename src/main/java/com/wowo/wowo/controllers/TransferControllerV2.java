package com.wowo.wowo.controllers;

import com.wowo.wowo.data.vms.TransferVm;
import com.wowo.wowo.services.TransferService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v2/transfer")
@Tag(name = "Transfer", description = "Chuyển tiền")
public class TransferControllerV2 {

    private final TransferService transferService;

    public TransferControllerV2(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<?> transfer(@Validated @RequestBody TransferVm data) {
        return transferService.transfer(data);
    }

}
