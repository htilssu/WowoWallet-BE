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
 *  * Created: 9-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayAnalysis extends Analysis {

    int day;

    public void update(Transaction transaction) {
        switch (transaction.getType()) {
            case IN:
                this.totalInMoney += transaction.getAmount();
                break;
            case OUT:
                this.totalOutMoney += transaction.getAmount();
                break;
            default:
                throw new IllegalArgumentException("Invalid transaction type");
        }

        totalTransactions++;
    }
}
