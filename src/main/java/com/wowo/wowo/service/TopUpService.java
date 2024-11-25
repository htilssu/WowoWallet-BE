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
import com.wowo.wowo.model.FlowType;
import com.wowo.wowo.model.TopUpRequest;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.model.Wallet;
import com.wowo.wowo.repository.TopUpRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TopUpService {

    private final TopUpRequestRepository topUpRequestRepository;
    private final WalletService walletService;
    private final TransferService transferService;
    private final TransactionService transactionService;

    public Wallet topUpWithLimit(String orderId) {
        final TopUpRequest order = topUpRequestRepository.findByOrderId(
                        orderId)
                .orElseThrow(() -> new BadRequest("Order not found"));
        final Wallet wallet = walletService.getWallet(Long.valueOf(order.getWalletId()));
        transferService.transferWithNoFee(walletService.getRootWallet(), wallet, order.getAmount());
        Transaction transaction = new Transaction();
        transaction.setAmount(order.getAmount());
        transaction.setSenderWallet(walletService.getRootWallet());
        transaction.setReceiveWallet(wallet);
        transaction.setFlowType(FlowType.TOP_UP);
        transaction.setMessage("Nạp tiền vào ví");
        transaction.setSenderName("Paypal");
        transactionService.save(transaction);
        return wallet;
    }

    public Wallet topUpWithLimit(String walletId, long amount) {
        final Wallet wallet = walletService.getWallet(Long.valueOf(walletId));
        final Wallet rootWallet = walletService.getRootWallet();
        transferService.transferWithNoFee(rootWallet, wallet, amount);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSenderWallet(rootWallet);
        transaction.setReceiveWallet(wallet);
        transaction.setFlowType(FlowType.TOP_UP);
        transaction.setMessage("Nạp tiền vào ví");
        transaction.setSenderName("Ngân hàng liên kết");
        transactionService.save(transaction);
        return wallet;
    }

}