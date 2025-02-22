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

import com.wowo.wowo.constant.Constant;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.FlowType;
import com.wowo.wowo.model.TopUpRequest;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.model.Wallet;
import com.wowo.wowo.repository.ConstantRepository;
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
    private final ConstantRepository constantRepository;

    public Wallet topUpWithLimit(String orderId) {
        final TopUpRequest order = topUpRequestRepository.findByOrderId(
                        orderId)
                .orElseThrow(() -> new BadRequest("Order not found"));
        if (!checkLimit(order.getAmount())) {
            throw new BadRequest("Số tiền nạp vượt quá giới hạn");
        }
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

    public boolean checkLimit(long amount) {
        final com.wowo.wowo.model.Constant constant = constantRepository.findById(
                        Constant.MAXIMUM_TOP_UP_AMOUNT)
                .orElseThrow(() -> new NotFoundException("Constant not found"));

        return amount <= constant.getValue();
    }

    public Wallet topUpWithLimit(String walletId, long amount) {
        if (!checkLimit(amount)) {
            throw new BadRequest("Số tiền nạp vượt quá giới hạn");
        }

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