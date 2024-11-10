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

package com.wowo.wowo.models;

import com.wowo.wowo.annotations.jpa.IndexAndHash;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "order_item")
@Data
public class OrderItem {

    @Id
    ObjectId id;
    @IndexAndHash(name = "idx_order-item_order_id")
    Long orderId;
    @NotNull
    String name;
    @Min(1)
    Long amount;
    @Min(0)
    Long unitPrice;
}
