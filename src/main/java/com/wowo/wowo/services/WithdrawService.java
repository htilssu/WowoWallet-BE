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
 *  * Created: 5-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.WithdrawDto;
import org.springframework.stereotype.Service;

@Service
public class WithdrawService {

    private final WalletService walletService;

    public WithdrawService(WalletService walletService) {this.walletService = walletService;}

    public void withdraw(WithdrawDto withdrawDto) {
        walletService.plusBalance(withdrawDto.getSourceId(), withdrawDto.getAmount() * -1);
    }
}
