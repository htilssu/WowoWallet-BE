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
import com.wowo.wowo.annotations.jpa.IndexAndHash;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "year_analysis")
@Data
@AllArgsConstructor
public class YearAnalysis extends Analysis {

    @Id
    String id;
    int year;
    @IndexAndHash
    String target;
    Collection<MonthAnalysis> monthAnalyses;

    public void addMonthAnalysis(MonthAnalysis analysis) {
        monthAnalyses.add(analysis);
        totalTransactions += analysis.getTotalTransactions();
        totalInMoney += analysis.getTotalInMoney();
        totalOutMoney += analysis.getTotalOutMoney();
    }

    private void removeMonthAnalysis(MonthAnalysis analysis) {
        monthAnalyses.remove(analysis);
        totalTransactions -= analysis.getTotalTransactions();
        totalInMoney -= analysis.getTotalInMoney();
        totalOutMoney -= analysis.getTotalOutMoney();
    }


    @JsonIgnore
    public MonthAnalysis getMonthAnalysis(int month) {
        var analysis = monthAnalyses.stream()
                .filter(m -> m.getMonth() == month)
                .findFirst()
                .orElse(null);

        if (analysis == null) {
            analysis = new MonthAnalysis(month, new ArrayList<>());
            monthAnalyses.add(analysis);
        }

        return analysis;
    }

    @JsonIgnore
    public MonthAnalysis getCurrentMonthAnalysis() {
        var date = LocalDate.now();
        return getMonthAnalysis(date.getMonth()
                .getValue());
    }

    @Override
    public void updateAnalysis(double inMoney, double outMoney) {
        final MonthAnalysis currentMonthAnalysis = getCurrentMonthAnalysis();
        removeMonthAnalysis(currentMonthAnalysis);
        currentMonthAnalysis.updateAnalysis(inMoney, outMoney);
        addMonthAnalysis(currentMonthAnalysis);
    }
}
