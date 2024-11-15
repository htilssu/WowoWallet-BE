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
 *  * Created: 15-11-2024
 *  ******************************************************
 */

ALTER TABLE "order"
DROP
COLUMN status;

ALTER TABLE "order"
    ADD status VARCHAR(255) NOT NULL;

ALTER TABLE partner
    ALTER COLUMN status SET NOT NULL;