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
 *  * Created: 24-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.controller;

import com.wowo.wowo.annotation.authorized.IsApplication;
import com.wowo.wowo.data.dto.WalletDTO;
import com.wowo.wowo.data.mapper.WalletMapper;
import com.wowo.wowo.service.ApplicationService;
import com.wowo.wowo.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/wallet")
public class WalletController {

    private final WalletService walletService;
    private final WalletMapper walletMapper;
    private final ApplicationService applicationService;

    public WalletController(WalletService walletService,
            WalletMapper walletMapper, ApplicationService applicationService) {
        this.walletService = walletService;
        this.walletMapper = walletMapper;
        this.applicationService = applicationService;
    }

    @GetMapping("/{id}")
    public WalletDTO getWallet(@PathVariable Long id) {
        return walletMapper.toDto(walletService.getWallet(id));
    }

    @PostMapping
    @IsApplication
    public WalletDTO createApplicationPartnerWallet(Authentication authentication) {
        var id = Long.valueOf(authentication.getPrincipal()
                .toString());
        log.info("Creating wallet for application with id: {}", id);
        return walletMapper.toDto(applicationService.createWallet(id));
    }
}
