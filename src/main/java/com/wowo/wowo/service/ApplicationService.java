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
import com.wowo.wowo.model.Application;

import java.util.Optional;

public interface ApplicationService {

    void createApplication(ApplicationCreateDto applicationCreateDto);
    void deleteApplication(String applicationId);
    Optional<Application> getApplication(String applicationId);
    Application getApplicationByUserId(String userId);
    Application getApplicationOrElseThrow(String applicationId);
}
