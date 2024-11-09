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
 *  * Created: 8-11-2024
 *  ******************************************************
 */

ALTER TABLE transaction
    ADD receiver_name VARCHAR(255);

ALTER TABLE transaction
    ADD sender_name VARCHAR(255);

ALTER TABLE transaction
DROP
COLUMN other_name;