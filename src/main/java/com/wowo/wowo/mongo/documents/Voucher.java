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
 *  * Created: 5-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.mongo.documents;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Voucher {

    @Id
    private String id;
    private String name;
    private String discount;
    private Long orderId;
}
