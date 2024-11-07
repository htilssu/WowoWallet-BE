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

package com.wowo.wowo.controllers;

import com.paypal.sdk.exceptions.ApiException;
import com.paypal.sdk.models.Order;
import com.wowo.wowo.data.dto.TopUpDto;
import com.wowo.wowo.data.dto.TopUpRequestDto;
import com.wowo.wowo.exceptions.BadRequest;
import com.wowo.wowo.services.PaypalService;
import com.wowo.wowo.services.TopUpService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/v1/top-up")
@AllArgsConstructor
public class TopUpController {

    private final PaypalService paypalService;
    private final TopUpService topUpService;

    @PostMapping
    public ResponseEntity<TopUpDto> topUp(@RequestBody @Validated TopUpRequestDto topUpRequestDto) throws
                                                                                                   IOException,
                                                                                                   ApiException {
        switch (topUpRequestDto.getMethod()) {
            case ATM_CARD -> {
                topUpService.topUp(topUpRequestDto.getTo(), topUpRequestDto.getAmount());
            }
            case PAYPAL -> {
                final Order order = paypalService.createTopUpOrder(topUpRequestDto);
                return ResponseEntity.ok(TopUpDto.builder()
                        .redirectTo(order.getLinks().stream().filter(linkDescription ->
                                        linkDescription.getRel().equals("approve")).findFirst()
                                .orElseThrow(() -> new BadRequest("Bad request"))
                                .getHref())
                        .build());
            }
        }
        return ResponseEntity.ok().build();
    }
}
