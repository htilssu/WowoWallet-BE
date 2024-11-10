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

package com.wowo.wowo.services;

import com.wowo.wowo.models.YearAnalysis;
import com.wowo.wowo.repositories.YearAnalysisRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class YearAnalysisService implements AnalysisService<YearAnalysis> {

    private final YearAnalysisRepository yearAnalysisRepository;

    public YearAnalysisService(YearAnalysisRepository yearAnalysisRepository) {
        this.yearAnalysisRepository = yearAnalysisRepository;
    }

    @Override
    public YearAnalysis getAnalysis(String target, int position) {
        var currentYear = LocalDate.now()
                .getYear();
        if (position > currentYear) {
            throw new IllegalArgumentException("Invalid year");
        }
        final Optional<YearAnalysis> yearAnalysis =
                yearAnalysisRepository.findFirstByTargetAndYear(target, position);
        return yearAnalysis.orElseGet(() -> createAnalysis(target));
    }

    @Override
    public YearAnalysis updateAnalysis(String target, long inMoney, long outMoney) {
        final YearAnalysis analysis = getAnalysis(target, LocalDate.now()
                .getYear());

        analysis.updateAnalysis(inMoney, outMoney);
        return yearAnalysisRepository.save(analysis);
    }

    public void saveAnalysis(YearAnalysis analysis) {
        yearAnalysisRepository.save(analysis);
    }

    private YearAnalysis createAnalysis(String target) {
        YearAnalysis analysis = YearAnalysis.create(target);
        return yearAnalysisRepository.save(analysis);
    }

}
