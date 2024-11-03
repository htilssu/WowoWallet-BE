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
 *  * Created: 31-10-2024
 *  ******************************************************
 */

ALTER TABLE atm_card
    ADD month INTEGER;

ALTER TABLE atm_card
    ADD year INTEGER;

ALTER TABLE atm_card
    ALTER COLUMN month SET NOT NULL;

ALTER TABLE atm_card
    ALTER COLUMN year SET NOT NULL;

ALTER TABLE atm_card
DROP
COLUMN expired;