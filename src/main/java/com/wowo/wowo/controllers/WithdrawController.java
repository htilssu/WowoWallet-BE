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
 *  * Created: 5-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.controllers;

import com.wowo.wowo.data.dto.ResponseMessage;
import com.wowo.wowo.data.dto.WithdrawDto;
import com.wowo.wowo.services.WithdrawService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/withdraw")
@AllArgsConstructor
public class WithdrawController {

    private final WithdrawService withdrawService;

    @PostMapping
    public ResponseEntity<?> withdraw(@RequestBody WithdrawDto withdrawDto) {

        withdrawService.withdraw(withdrawDto);
        return ResponseEntity.ok(new ResponseMessage("Rút tiền thành công"));
    }
}
