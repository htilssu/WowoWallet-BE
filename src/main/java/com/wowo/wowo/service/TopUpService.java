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
 *  * Created: 30-10-2024
 *  ******************************************************
 */

package com.wowo.wowo.service;

import com.paypal.sdk.models.Order;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.model.UserWallet;
import com.wowo.wowo.model.TopUpRequest;
import com.wowo.wowo.repository.TopUpRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TopUpService {

    private final TopUpRequestRepository topUpRequestRepository;
    private final WalletService walletService;

    public UserWallet topUpWithLimit(Order order) {
        return topUpWithLimit(order.getId());
    }

    public UserWallet topUpWithLimit(String orderId) {
        final TopUpRequest order = topUpRequestRepository.findByOrderId(
                        orderId)
                .orElseThrow(() -> new BadRequest("Order not found"));
        return walletService.plusBalance(order.getWalletId(), order.getAmount());
    }

    public UserWallet topUpWithLimit(String walletId, long amount) {
        return walletService.plusBalance(walletId, amount);
    }

}