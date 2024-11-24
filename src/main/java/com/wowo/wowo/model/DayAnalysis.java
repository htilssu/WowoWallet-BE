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

package com.wowo.wowo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void updateAnalysis(double inMoney, double outMoney) {
        this.totalInMoney += inMoney;
        this.totalOutMoney += outMoney;
        this.totalTransactions++;
    }

    public static List<DayAnalysis> createDayAnalysisList(int month) {
        List<DayAnalysis> dayAnalyses = new ArrayList<>();
        var year = LocalDate.now()
                .getYear();

        var dayOfMonth = LocalDate.of(year, month, 1).lengthOfMonth();
        for (int i = 1; i <= dayOfMonth; i++) {
            dayAnalyses.add(new DayAnalysis(i));
        }
        return dayAnalyses;
    }
}
