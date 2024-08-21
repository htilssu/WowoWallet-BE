-- liquibase formatted sql

-- changeset htilssu:1723942121824-2
ALTER TABLE group_fund_transaction
    ADD "create" date DEFAULT CURRENT_DATE;

-- changeset htilssu:1723942121824-3
ALTER TABLE group_fund_transaction
    ALTER COLUMN "create" SET NOT NULL;

-- changeset htilssu:1723942121824-10
ALTER TABLE group_fund_transaction DROP COLUMN created;

-- changeset htilssu:1723942121824-1
ALTER TABLE "user"
    ALTER COLUMN partner_id SET DEFAULT NULL::bpchar;

