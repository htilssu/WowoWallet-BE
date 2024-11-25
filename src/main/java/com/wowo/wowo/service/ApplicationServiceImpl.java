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

import com.wowo.wowo.data.dto.ApplicationUserCreationDTO;
import com.wowo.wowo.data.mapper.ApplicationMapper;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.Application;
import com.wowo.wowo.model.ApplicationPartnerWallet;
import com.wowo.wowo.repository.ApplicationRepository;
import com.wowo.wowo.util.ApiKeyUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final UserService userService;
    private final WalletService walletService;

    @Override
    public Application createApplication(ApplicationUserCreationDTO applicationUserCreationDTO) {
        final Application application = applicationMapper.toEntity(applicationUserCreationDTO);
        var user = userService.getUserByIdOrElseThrow(applicationUserCreationDTO.getOwnerId());
        application.setOwner(user);
        application.setSecret(ApiKeyUtil.generateApiKey());

        return applicationRepository.save(application);
    }

    @Override
    public void deleteApplication(String applicationId) {

    }

    @Override
    public Optional<Application> getApplication(Long applicationId) {
        return applicationRepository.findById(applicationId);
    }

    @Override
    public Application getApplicationByUserId(String userId) {
        return null;
    }

    @Override
    public Application getApplicationOrElseThrow(Long applicationId) {

        return getApplication(applicationId).orElseThrow(
                () -> {
                    log.error("Application with id {} not found", applicationId);
                    return new NotFoundException("Application not found");
                });
    }

    @Override
    public ApplicationPartnerWallet createWallet(Long applicationId) {
        final Application application = getApplicationOrElseThrow(applicationId);
        final ApplicationPartnerWallet applicationPartnerWallet = new ApplicationPartnerWallet();
        application.getPartnerWallets()
                .add(applicationPartnerWallet);

        return applicationPartnerWallet;
    }

    @Override
    public Optional<Application> getApplicationBySecretKey(String apiKey) {
       return applicationRepository.findFirstBySecret(apiKey);
    }
}
