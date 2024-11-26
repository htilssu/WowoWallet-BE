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

package com.wowo.wowo.data.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for {@link Application}
 */
@Data
@AllArgsConstructor
public class ApplicationUserCreationDTO {

    @NotEmpty(message = "Name is required")
    private String name;
    @Null(message = "Owner Id must be null")
    private String ownerId;
}
