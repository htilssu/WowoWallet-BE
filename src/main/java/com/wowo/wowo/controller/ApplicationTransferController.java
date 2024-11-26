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
 *  * Created: 27-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.controller;

import com.wowo.wowo.annotation.authorized.IsApplication;
import com.wowo.wowo.data.dto.ApplicationTransferDTO;
import com.wowo.wowo.service.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/application")
@IsApplication
@AllArgsConstructor
public class ApplicationTransferController {

    private final TransferService transferService;

    @PostMapping("/transfer")
    public void transfer(ApplicationTransferDTO transferDTO, Authentication authentication) {
        transferService.transfer(transferDTO, authentication);
    }

    @PostMapping("withdraw")
    public void withdraw(ApplicationTransferDTO transferDTO, Authentication authentication) {
        transferService.withdraw(transferDTO, authentication);
    }
}
