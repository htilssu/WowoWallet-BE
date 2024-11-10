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
    ADD other_name VARCHAR(255);

ALTER TABLE group_fund_transaction
    ADD type VARCHAR(255);

ALTER TABLE group_fund_transaction
    ALTER COLUMN type SET NOT NULL;

ALTER TABLE wallet_transaction
    ADD type SMALLINT;

ALTER TABLE group_fund_transaction
DROP
COLUMN transaction_type;

ALTER TABLE group_fund
    ALTER COLUMN is_locked SET NOT NULL;