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
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class Analysis {

    long totalTransactions;
    double totalInMoney;
    double totalOutMoney;

    public void updateAnalysis(double inMoney, double outMoney) {
        totalTransactions++;
        totalInMoney += inMoney;
        totalOutMoney += outMoney;
    }
}
