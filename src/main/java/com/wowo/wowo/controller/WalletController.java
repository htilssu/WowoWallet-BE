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

import com.wowo.wowo.data.dto.WalletDTO;
import com.wowo.wowo.data.mapper.WalletMapper;
import com.wowo.wowo.service.WalletService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/wallet")
public class WalletController {

    private final WalletService walletService;
    private final WalletMapper walletMapper;

    public WalletController(WalletService walletService,
            WalletMapper walletMapper) {this.walletService = walletService;
        this.walletMapper = walletMapper;
    }

    @GetMapping("/{id}")
    public WalletDTO getWallet(@PathVariable Long id) {
        return walletMapper.toDto(walletService.getWallet(id));
    }
}
