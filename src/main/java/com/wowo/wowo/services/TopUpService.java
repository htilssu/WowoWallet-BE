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

import com.paypal.sdk.exceptions.ApiException;
import com.paypal.sdk.models.Order;
import com.wowo.wowo.data.dto.TopUpRequestDto;
import com.wowo.wowo.exceptions.BadRequest;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.mongo.documents.TopUpRequest;
import com.wowo.wowo.mongo.repositories.TopUpRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class TopUpService {

    private final PaypalService paypalService;
    private final TopUpRequestRepository topUpRequestRepository;
    private final WalletService walletService;

    public Order topUpByPaypal(TopUpRequestDto topUpRequestDto) throws IOException, ApiException {
        final Order topUpOrder = paypalService.createTopUpOrder(topUpRequestDto);
        topUpRequestRepository.save(TopUpRequest.builder()
                .amount(topUpRequestDto.getAmount())
                .walletId(topUpRequestDto.getTo())
                .orderId(topUpOrder.getId())
                .build());
        return topUpOrder;
    }

    public Wallet topUp(Order result) {
        final TopUpRequest topUpRequest = topUpRequestRepository.findByOrderId(result.getId())
                .orElseThrow(() -> new BadRequest("Order not found"));
        return walletService.plusBalance(topUpRequest.getWalletId(), topUpRequest.getAmount());
    }
}