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
 *  * Created: 9-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.repository;

import com.wowo.wowo.model.Voucher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;

public interface VoucherRepository extends MongoRepository<Voucher, String> {

    Collection<Voucher> findByOrderId(Long orderId);
}
