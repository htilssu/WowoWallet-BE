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
 *  * Created: 1-11-2024
 *  ******************************************************
 */

CREATE SEQUENCE IF NOT EXISTS support_ticket_seq START WITH 1 INCREMENT BY 50;

ALTER TABLE support_ticket
DROP
COLUMN id;

ALTER TABLE support_ticket
    ADD id DECIMAL NOT NULL PRIMARY KEY;