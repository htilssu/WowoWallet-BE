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
 *  * Created: 15-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.service;

import com.wowo.wowo.constant.Constant;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.repository.ConstantRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConstantService {

    private final ConstantRepository constantRepository;

    public @NotNull Double getOrderMaxLifeTime() {
        final com.wowo.wowo.model.Constant orderTimeoutConstant =
                constantRepository.findById(Constant.ORDER_TIMEOUT)
                        .orElseThrow(() -> new IllegalStateException(
                                "Order timeout constant not found"));

        return orderTimeoutConstant.getValue();

    }

    public com.wowo.wowo.model.Constant findByKey(String constantKey) {

        return constantRepository.findById(constantKey)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy biến giới hạn"));
    }
}
