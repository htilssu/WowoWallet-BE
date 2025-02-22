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
 *  * Created: 25-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.repository;

import com.wowo.wowo.model.ApplicationPartnerWallet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationPartnerWalletRepository
        extends JpaRepository<ApplicationPartnerWallet, Long> {

    List<ApplicationPartnerWallet> findByApplication_Id(Long applicationId, Pageable pageable);
    List<ApplicationPartnerWallet> findByApplication_IdOrderByIdAsc(Long applicationId, Pageable pageable);
}