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

package com.wowo.wowo.service;

import com.wowo.wowo.model.Analysis;

public interface AnalysisService<T extends Analysis> {

    T getAnalysis(String target, int position);
    T updateAnalysis(String target, long inMoney, long outMoney);
}