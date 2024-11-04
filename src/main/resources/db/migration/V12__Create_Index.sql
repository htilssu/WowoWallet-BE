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
 *  * Created: 4-11-2024
 *  ******************************************************
 */

CREATE INDEX atm_card_bank_id_index ON atm_card (bank_id);

CREATE INDEX atm_card_owner_id_index ON atm_card (owner_id);

CREATE INDEX fund_member_group_id_index ON fund_member (group_id);

CREATE INDEX search_unique_user ON "user" (id, username, email);

CREATE INDEX wallet_owner_id_index ON wallet (owner_id);

CREATE INDEX wallet_owner_id_owner_type_index ON wallet (owner_id, owner_type);

CREATE INDEX wallet_transaction_receiver_wallet_id_index ON wallet_transaction (receiver_wallet);

CREATE INDEX wallet_transaction_sender_wallet_id_index ON wallet_transaction (sender_wallet);

CREATE INDEX user_email_index ON "user" (email);

CREATE INDEX user_username_index ON "user" (username);