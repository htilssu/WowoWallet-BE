CREATE SEQUENCE IF NOT EXISTS atm_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE atm
(
    id   INTEGER      NOT NULL,
    name VARCHAR(255) NOT NULL,
    icon VARCHAR(255),
    CONSTRAINT pk_atm PRIMARY KEY (id)
);

CREATE TABLE service_statistic
(
    income  DECIMAL NOT NULL,
    outcome DECIMAL NOT NULL,
    month   INTEGER NOT NULL,
    year    INTEGER NOT NULL,
    service INTEGER NOT NULL,
    CONSTRAINT pk_service_statistic PRIMARY KEY (month, year, service)
);

ALTER TABLE constant
    ADD created TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE "order"
    ADD partner_id VARCHAR(10);

ALTER TABLE "order"
    ADD voucher_id VARCHAR(15);

ALTER TABLE "order"
    ADD CONSTRAINT order_external_transaction_id_key UNIQUE (external_transaction_id);

ALTER TABLE "order"
    ADD CONSTRAINT order_id_partner_id_key UNIQUE (id, partner_id);

ALTER TABLE "order"
    ADD CONSTRAINT order_transaction_id_key UNIQUE (transaction_id);

ALTER TABLE payment_request
    ADD CONSTRAINT FK_PAYMENT_REQUEST_ON_TRANSACTION FOREIGN KEY (transaction_id) REFERENCES transaction (id);

ALTER TABLE service_statistic
    ADD CONSTRAINT FK_SERVICE_STATISTIC_ON_SERVICE FOREIGN KEY (service) REFERENCES service (id);

ALTER TABLE constant
    DROP COLUMN date;

ALTER TABLE "order"
    DROP COLUMN invoice_id;

ALTER TABLE "order"
    DROP COLUMN service_id;

ALTER TABLE payment_system
    DROP COLUMN type;

DROP SEQUENCE order_id_seq CASCADE;

DROP SEQUENCE partner_id_seq CASCADE;

DROP SEQUENCE payment_request_id CASCADE;

DROP SEQUENCE transaction_id_seq CASCADE;

DROP SEQUENCE user_id_seq CASCADE;

ALTER TABLE atm_card
    ALTER COLUMN atm_id SET NOT NULL;

ALTER TABLE partner
    DROP COLUMN balance;

ALTER TABLE partner
    ADD balance DOUBLE PRECISION NOT NULL default 0;

ALTER TABLE wallet
    DROP COLUMN balance;

ALTER TABLE wallet
    ADD balance DOUBLE PRECISION DEFAULT 0 NOT NULL;

ALTER TABLE atm_card
    ALTER COLUMN expired TYPE VARCHAR(255) USING (expired::VARCHAR(255));

CREATE SEQUENCE IF NOT EXISTS payment_method_id_seq;
ALTER TABLE payment_method
    ALTER COLUMN id SET NOT NULL;
ALTER TABLE payment_method
    ALTER COLUMN id SET DEFAULT nextval('payment_method_id_seq');

ALTER SEQUENCE payment_method_id_seq OWNED BY payment_method.id;

ALTER TABLE "user"
    ALTER COLUMN partner_id SET DEFAULT NULL::bpchar;
ALTER TABLE group_fund_transaction
    ADD "create" date DEFAULT CURRENT_DATE;

ALTER TABLE group_fund_transaction
    ALTER COLUMN "create" SET NOT NULL;

ALTER TABLE group_fund_transaction
    DROP COLUMN created;

ALTER TABLE "user"
    ALTER COLUMN partner_id SET DEFAULT NULL::bpchar;