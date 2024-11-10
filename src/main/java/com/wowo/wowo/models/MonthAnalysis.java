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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    @JsonIgnore
    public DayAnalysis getDayAnalysis(int day) {
        var analysis = dayAnalysis.stream()
                .filter(d -> d.getDay() == day)
                .findFirst()
                .orElse(null);

        if (analysis == null) {
            analysis = new DayAnalysis(day);
            dayAnalysis.add(analysis);
        }

        return analysis;
    }

    @JsonIgnore
    public DayAnalysis getCurrentDayAnalysis() {
        return getDayAnalysis(LocalDate.now()
                .getDayOfMonth());
    }

    @Override
    public void updateAnalysis(double inMoney, double outMoney) {
        final DayAnalysis currentDayAnalysis = getCurrentDayAnalysis();
        removeDayAnalysis(currentDayAnalysis);
        currentDayAnalysis.updateAnalysis(inMoney, outMoney);
        addDayAnalysis(currentDayAnalysis);
    }

    public static List<MonthAnalysis> createMonthAnalysisList() {
        List<MonthAnalysis> monthAnalyses = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            monthAnalyses.add(new MonthAnalysis(i, DayAnalysis.createDayAnalysisList(i)));
        }

        return monthAnalyses;
    }
}
