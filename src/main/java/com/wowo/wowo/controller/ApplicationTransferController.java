/*
 * ******************************************************
 *  * Copyright (c) 2024 htilssu
 *  *
 *  * This code is the property of htilssu. All rights reserved.
 *  * Redistribution or reproduction of any part of this code
 *  * in any form, with or without modification, is strictly
 *  * prohibited without prior written permission from the author.
 *  *
 *  * Author: htilssu
 *  * Created: 27-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.controller;

import com.wowo.wowo.annotation.authorized.IsApplication;
import com.wowo.wowo.annotation.authorized.IsAuthenticated;
import com.wowo.wowo.annotation.authorized.IsUser;
import com.wowo.wowo.data.dto.ApplicationTransferDTO;
import com.wowo.wowo.data.dto.TransactionDTO;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.service.ApplicationService;
import com.wowo.wowo.service.TransferService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/application")
@AllArgsConstructor
@IsAuthenticated
public class ApplicationTransferController {

    private final TransactionMapper transactionMapper;
    private final TransferService transferService;
    private final ApplicationService applicationService;

    @PostMapping("transfer")
    @IsApplication
    public ResponseEntity<Object> transfer(@RequestBody ApplicationTransferDTO transferDTO,
            Authentication authentication) {
        transferService.transfer(transferDTO, authentication);
        log.info("Transfer from application {} with {} successfully",
                authentication.getPrincipal()
                        .toString(), transferDTO);

        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("withdraw")
    @IsApplication
    public ResponseEntity<Object> withdraw(@RequestBody ApplicationTransferDTO transferDTO,
            Authentication authentication) {
        transferService.withdraw(transferDTO, authentication);
        log.info("Withdraw to application {} with {} successfully",
                authentication.getPrincipal()
                        .toString(), transferDTO);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("{id}/withdraw")
    @IsUser
    public TransactionDTO withdraw(@PathVariable String id,
            @RequestBody ApplicationTransferDTO transferDTO) {
        return transactionMapper.toDto(applicationService.withdraw(id, transferDTO.getAmount()));
    }
}
