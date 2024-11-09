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

import com.wowo.wowo.annotations.jpa.IndexAndHash;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "year_analysis")
@Data
@AllArgsConstructor
public class YearAnalysis extends Analysis {

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

    public void updateMonthAnalysis(MonthAnalysis analysis) {
        removeMonthAnalysis(analysis);
        addMonthAnalysis(analysis);
    }

    public MonthAnalysis getMonthAnalysis(int month) {
        return monthAnalyses.stream()
                .filter(m -> m.getMonth() == month)
                .findFirst()
                .orElse(null);
    }
}
