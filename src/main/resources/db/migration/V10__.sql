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
 *  * Created: 24-11-2024
 *  ******************************************************
 */

ALTER TABLE application_api_key
DROP
CONSTRAINT fk_application_api_key_on_application;

ALTER TABLE application
    ADD secret VARCHAR(255);

DROP TABLE application_api_key CASCADE;

DROP SEQUENCE application_api_key_id_seq CASCADE;