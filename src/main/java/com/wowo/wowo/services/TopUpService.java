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
 *  * Created: 30-10-2024
 *  ******************************************************
 */

package com.wowo.wowo.services;

import com.paypal.sdk.PaypalServerSDKClient;
import com.paypal.sdk.exceptions.ApiException;
import com.paypal.sdk.models.Order;
import com.wowo.wowo.data.dto.TopUpDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class TopUpService {

    private final PaypalService paypalService;

    public Order topUp(TopUpDto topUpDto) throws IOException, ApiException {
        return paypalService.createTopUpOrder(topUpDto);
    }

}