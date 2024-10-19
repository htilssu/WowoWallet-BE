ALTER TABLE transaction
    ADD amount BIGINT;

ALTER TABLE transaction
    ALTER COLUMN amount SET NOT NULL;

ALTER TABLE transaction
DROP
COLUMN money;