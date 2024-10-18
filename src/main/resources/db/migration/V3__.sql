ALTER TABLE "user"
    ADD total_money BIGINT DEFAULT 0;

ALTER TABLE "user"
    ALTER COLUMN total_money SET NOT NULL;