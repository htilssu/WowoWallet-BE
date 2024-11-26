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
import com.wowo.wowo.model.Application;
import com.wowo.wowo.model.ApplicationPartnerWallet;

import java.util.Optional;

public interface ApplicationService {

    Application createApplication(ApplicationUserCreationDTO applicationUserCreationDTO);
    void deleteApplication(String applicationId);
    Optional<Application> getApplication(Long applicationId);
    Application getApplicationByUserId(String userId);
    Application getApplicationOrElseThrow(Long applicationId);
    ApplicationPartnerWallet createWallet(Long applicationId);
    Optional<Application> getApplicationBySecretKey(String apiKey);
}
