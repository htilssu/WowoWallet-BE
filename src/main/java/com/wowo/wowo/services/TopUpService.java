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

package com.wowo.wowo.services;

import com.paypal.sdk.models.Order;
import com.wowo.wowo.exceptions.BadRequest;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.mongo.documents.TopUpRequest;
import com.wowo.wowo.mongo.repositories.TopUpRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TopUpService {

    private final TopUpRequestRepository topUpRequestRepository;
    private final WalletService walletService;

    public Wallet topUp(Order result) {
        final TopUpRequest order = topUpRequestRepository.findByOrderId(
                        result.getId())
                .orElseThrow(() -> new BadRequest("Order not found"));
        return walletService.plusBalance(order.getWalletId(), order.getAmount());
    }
}