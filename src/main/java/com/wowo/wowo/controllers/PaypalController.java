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
 *  * Created: 4-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.controllers;

import com.wowo.wowo.services.PaypalService;
import com.wowo.wowo.services.TopUpService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "v1/paypal", produces = "application/json; charset=utf-8")
public class PaypalController {

    private static final Logger logger = LoggerFactory.getLogger(PaypalController.class);
    private final PaypalService paypalService;
    private final TopUpService topUpService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload) {
        try {
            JSONObject data = new JSONObject(payload);

            // Kiểm tra loại sự kiện
            String eventType = data.getString("event_type");
            switch (eventType) {
                case "CHECKOUT.ORDER.APPROVED" -> {
                    var orderId = data.getJSONObject("resource").getString("id");
                    paypalService.captureOrder(orderId);
                }
                case "CHECKOUT.ORDER.COMPLETED" -> {
                    var orderId = data.getJSONObject("resource").getString("id");
                    topUpService.topUp(orderId);
                }
                default -> logger.info("Unknown event type: " + eventType);
            }
        } catch (Exception e) {
            logger.error("Error while handling webhook", e);
        }

        return ResponseEntity.ok().build();
    }
}
