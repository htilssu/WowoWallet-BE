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
 *  * Created: 23-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.service;

import com.wowo.wowo.data.dto.ApplicationCreateDto;
import com.wowo.wowo.data.mapper.ApplicationMapper;
import com.wowo.wowo.model.Application;
import com.wowo.wowo.repository.ApplicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;

    @Override
    public void createApplication(ApplicationCreateDto applicationCreateDto) {
        final Application application = applicationMapper.toEntity(applicationCreateDto);
    }

    @Override
    public void deleteApplication(String applicationId) {

    }

    @Override
    public Optional<Application> getApplication(String applicationId) {
        return Optional.empty();
    }

    @Override
    public Application getApplicationByUserId(String userId) {
        return null;
    }

    @Override
    public Application getApplicationOrElseThrow(String applicationId) {
        return null;
    }
}
