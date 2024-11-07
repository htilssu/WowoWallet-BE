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
 *  * Created: 4-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.mongo.repositories;

import com.wowo.wowo.mongo.documents.TopUpRequest;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TopUpRequestRepository extends MongoRepository<TopUpRequest, String> {

    Optional<TopUpRequest> findByOrderId(String id);
}