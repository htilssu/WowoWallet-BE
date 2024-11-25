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

package com.wowo.wowo.controller;

import com.paypal.sdk.exceptions.ApiException;
import com.paypal.sdk.models.Order;
import com.wowo.wowo.data.dto.TopUpDTO;
import com.wowo.wowo.data.dto.TopUpRequestDTO;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.exception.InsufficientBalanceException;
import com.wowo.wowo.service.PaypalService;
import com.wowo.wowo.service.TopUpService;
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
    public ResponseEntity<TopUpDTO> topUp(@RequestBody @Validated TopUpRequestDTO topUpRequestDTO) throws
                                                                                                   IOException,
                                                                                                   ApiException {
       try
       {
           switch (topUpRequestDTO.getMethod()) {
               case ATM_CARD -> {
                   //TODO: validate card in db
                   topUpService.topUpWithLimit(topUpRequestDTO.getTo(), topUpRequestDTO.getAmount());
               }
               case PAYPAL -> {
                   final Order order = paypalService.createTopUpOrder(topUpRequestDTO);
                   return ResponseEntity.ok(TopUpDTO.builder()
                           .redirectTo(order.getLinks().stream().filter(linkDescription ->
                                           linkDescription.getRel().equals("approve")).findFirst()
                                   .orElseThrow(() -> new BadRequest("Bad request"))
                                   .getHref())
                           .build());
               }
           }
       }catch (InsufficientBalanceException e){
              throw new BadRequest("Có lỗi khi nạp tiền vào tài khoản, vì giới hạn số tiền có trong app");
       }
        return ResponseEntity.ok().build();
    }
}
