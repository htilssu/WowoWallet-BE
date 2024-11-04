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

package com.wowo.wowo.mongo.documents;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Data
@Builder
public class TopUpRequest {

    @Id
    private ObjectId id;
    private String walletId;
    private Long amount;
    private String orderId;

    @CreatedDate
    private Instant created;

    @LastModifiedDate
    private Instant updated;
}
