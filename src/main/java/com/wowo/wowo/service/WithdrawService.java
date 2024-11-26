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

package com.wowo.wowo.service;

import com.wowo.wowo.data.dto.WithdrawDTO;
import com.wowo.wowo.model.FlowType;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.model.Wallet;
import com.wowo.wowo.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class WithdrawService {

    private final WalletService walletService;
    private final TransferService transferService;
    private final TransactionRepository transactionRepository;

    public WithdrawService(WalletService walletService, TransferService transferService,
            TransactionRepository transactionRepository) {
        this.walletService = walletService;
        this.transferService = transferService;
        this.transactionRepository = transactionRepository;
    }

    public void withdraw(WithdrawDTO withdrawDTO) {
        final Wallet wallet = walletService.getWallet(Long.valueOf(withdrawDTO.getSourceId()));
        final Wallet rootWallet = walletService.getRootWallet();
        transferService.transferWithNoFee(wallet, rootWallet, withdrawDTO.getAmount());
        Transaction transaction = new Transaction();
        transaction.setAmount(withdrawDTO.getAmount());
        transaction.setSenderWallet(wallet);
        transaction.setReceiveWallet(rootWallet);
        transaction.setFlowType(FlowType.WITHDRAW);
        transactionRepository.save(transaction);
    }
}
