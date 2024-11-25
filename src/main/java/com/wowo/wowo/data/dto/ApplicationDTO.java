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
 *  * Created: 24-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.data.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.wowo.wowo.model.Application}
 */
public record ApplicationDTO(Long id, String name, String secret, String createdAt,
                             String updatedAt, Long balance, Set<WalletDTO> partnerWallets)
        implements Serializable {

}