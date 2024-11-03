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
 *  * Created: 2-11-2024
 *  ******************************************************
 */

ALTER TABLE support_ticket
DROP
COLUMN id;

ALTER TABLE support_ticket
    ADD id BIGINT NOT NULL PRIMARY KEY;

ALTER TABLE wallet
ALTER
COLUMN owner_type TYPE VARCHAR(30) USING (owner_type::VARCHAR(30));