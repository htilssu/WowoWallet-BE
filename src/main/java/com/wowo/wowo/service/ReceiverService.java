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
 *  * Created: 28-10-2024
 *  ******************************************************
 */

package com.wowo.wowo.service;

import com.wowo.wowo.data.dto.ReceiverDto;
import com.wowo.wowo.model.GroupFund;
import com.wowo.wowo.model.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReceiverService {

    private final UserService userService;

    public ReceiverDto getReceiver(Transaction transaction) {
        switch (transaction.getVariant()) {
            case WALLET -> {
                var receiver = userService.getUserById(
                        transaction.getWalletTransaction().getReceiverWallet().getOwnerId());
                return new ReceiverDto(receiver.getLastName() + " " + receiver.getFirstName(), null,
                        receiver.getEmail(), "USER");
            }
            case GROUP_FUND -> {
                final GroupFund groupFund = transaction.getGroupFundTransaction().getGroup();
                return new ReceiverDto(groupFund.getName(), groupFund.getImage(), null,
                        "GROUP_FUND");
            }
            default -> throw new RuntimeException("Transaction target not found");
        }
    }

}
