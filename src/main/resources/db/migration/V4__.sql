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
 *  * Created: 28-10-2024
 *  ******************************************************
 */

ALTER TABLE transaction
DROP
COLUMN type;

ALTER TABLE transaction
DROP
COLUMN variant;

ALTER TABLE transaction
    ADD type VARCHAR(255) NOT NULL;

ALTER TABLE transaction
    ADD variant VARCHAR(255) NOT NULL;