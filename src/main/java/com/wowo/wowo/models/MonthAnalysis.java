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

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthAnalysis extends Analysis {

    int month;
    Collection<DayAnalysis> dayAnalysis;

    public void addDayAnalysis(DayAnalysis analysis) {
        dayAnalysis.add(analysis);
        totalTransactions += analysis.getTotalTransactions();
        totalInMoney += analysis.getTotalInMoney();
        totalOutMoney += analysis.getTotalOutMoney();
    }

    private void removeDayAnalysis(DayAnalysis analysis) {
        dayAnalysis.remove(analysis);
        totalTransactions -= analysis.getTotalTransactions();
        totalInMoney -= analysis.getTotalInMoney();
        totalOutMoney -= analysis.getTotalOutMoney();
    }

    public void updateDayAnalysis(DayAnalysis analysis) {
        removeDayAnalysis(analysis);
        addDayAnalysis(analysis);
    }

    public DayAnalysis getDayAnalysis(int day) {
        return dayAnalysis.stream()
                .filter(d -> d.getDay() == day)
                .findFirst()
                .orElse(null);
    }
}
