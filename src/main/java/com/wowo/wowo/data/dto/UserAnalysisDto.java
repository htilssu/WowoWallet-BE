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

package com.wowo.wowo.data.dto;

import java.util.Collection;

public record UserAnalysisDto(
    Collection<MonthAnalysisDto> monthAnalysis,
    int totalTransactions,
    double totalInMoney,
    double totalOutMoney
) {
}