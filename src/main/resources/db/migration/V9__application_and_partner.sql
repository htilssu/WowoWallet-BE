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
 *  * Created: 23-11-2024
 *  ******************************************************
 */

ALTER TABLE partner_api_key
DROP
CONSTRAINT fk_partner_api_key_on_partner;

CREATE SEQUENCE IF NOT EXISTS application_api_key_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS application_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE application
(
    id       BIGINT       NOT NULL,
    name     VARCHAR(255),
    owner_id VARCHAR(255) NOT NULL,
    CONSTRAINT pk_application PRIMARY KEY (id)
);

CREATE TABLE application_api_key
(
    id             BIGINT NOT NULL,
    key            VARCHAR(255),
    secret         VARCHAR(255),
    application_id BIGINT NOT NULL,
    CONSTRAINT pk_application_api_key PRIMARY KEY (id)
);

ALTER TABLE application_api_key
    ADD CONSTRAINT FK_APPLICATION_API_KEY_ON_APPLICATION FOREIGN KEY (application_id) REFERENCES application (id);

ALTER TABLE application
    ADD CONSTRAINT FK_APPLICATION_ON_OWNER FOREIGN KEY (owner_id) REFERENCES "user" (id);

DROP TABLE partner_api_key CASCADE;

DROP TABLE payment_system CASCADE;

DROP SEQUENCE payment_system_id_seq CASCADE;