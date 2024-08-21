-- liquibase formatted sql

-- changeset htilssu:1723942185381-2
ALTER TABLE group_fund_transaction
    ADD created date DEFAULT CURRENT_DATE;

-- changeset htilssu:1723942185381-3
ALTER TABLE group_fund_transaction
    ALTER COLUMN created SET NOT NULL;

-- changeset htilssu:1723942185381-10
ALTER TABLE group_fund_transaction DROP COLUMN "create";

-- changeset htilssu:1723942185381-1
ALTER TABLE "user"
    ALTER COLUMN partner_id SET DEFAULT NULL::bpchar;

