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

package com.wowo.wowo.data.mapper;

import com.wowo.wowo.data.dto.ApplicationUserCreationDTO;
import com.wowo.wowo.data.dto.ApplicationDTO;
import com.wowo.wowo.model.Application;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApplicationMapper {

    Application toEntity(ApplicationUserCreationDTO applicationUserCreationDTO);
    @Mapping(source = "balance", target = "wallet.balance")
    Application toEntity(ApplicationDTO applicationDTO);
    @Mapping(source = "wallet.balance", target = "balance")
    ApplicationDTO toDto(Application application);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Application partialUpdate(
            ApplicationDTO applicationDTO,
            @MappingTarget Application application);
}