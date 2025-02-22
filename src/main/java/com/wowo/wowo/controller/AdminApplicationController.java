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

import com.wowo.wowo.annotation.authorized.IsAdmin;
import com.wowo.wowo.annotation.authorized.IsAuthenticated;
import com.wowo.wowo.data.dto.ApplicationDTO;
import com.wowo.wowo.data.mapper.ApplicationMapper;
import com.wowo.wowo.service.ApplicationServiceImpl;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/admin/v1/application")
@RestController
@AllArgsConstructor
@IsAuthenticated
public class AdminApplicationController {

    private final ApplicationServiceImpl applicationServiceImpl;
    private final ApplicationMapper applicationMapper;

    @GetMapping
    public @NotNull List<ApplicationDTO> getApplications() {
        return applicationServiceImpl.getAllApplications()
                .stream()
                .map(applicationMapper::toDto)
                .collect(Collectors.toList());
    }
}
