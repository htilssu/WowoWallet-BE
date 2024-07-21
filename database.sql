-- CREATE
--     DATABASE ewallet;
-- open the database

-- ENUM TYPE
DROP TYPE IF EXISTS owner_type CASCADE;

CREATE TYPE owner_type AS ENUM ('user', 'partner');

DROP TYPE IF EXISTS transaction_type CASCADE;

CREATE TYPE transaction_type AS ENUM ('deposit', 'withdrawal', 'transfer');

DROP TYPE IF EXISTS transaction_target CASCADE;

CREATE TYPE transaction_target AS ENUM ('wallet', 'group_fund');

DROP TYPE IF EXISTS transaction_status CASCADE;

CREATE TYPE transaction_status AS ENUM ('pending', 'completed', 'failed', 'funded');

DROP TYPE IF EXISTS payment_system_type CASCADE;

CREATE TYPE payment_system_type AS ENUM ('internal', 'paypal', 'stripe', 'other');

DROP TABLE IF EXISTS wallet CASCADE;

CREATE TABLE wallet
(
    id         serial PRIMARY KEY,
    owner_type owner_type NOT NULL DEFAULT 'user',
    currency   VARCHAR(3) NOT NULL DEFAULT 'VND',
    owner_id   char(10),
    balance    numeric    NOT NULL DEFAULT 0,
    CONSTRAINT uk_wallet_owner UNIQUE (owner_type, owner_id));

CREATE OR REPLACE FUNCTION check_wallet_owner()
    RETURNS trigger
AS
$$
BEGIN
    IF new.owner_type = 'user' THEN
        IF NOT EXISTS ( SELECT 1 FROM "user" WHERE id = new.owner_id ) THEN
            RAISE EXCEPTION 'User không tồn tại';
            END IF;
        END IF;

    IF new.owner_type = 'partner' THEN
        IF NOT EXISTS ( SELECT 1 FROM partner WHERE id = new.owner_id ) THEN
            RAISE EXCEPTION 'Partner không tồn tại';
            END IF;
        END IF;

    RETURN new;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER wallet_insert_trigger
    BEFORE INSERT
    ON wallet
    FOR EACH ROW
EXECUTE FUNCTION check_wallet_owner();

CREATE OR REPLACE FUNCTION create_wallet()
    RETURNS trigger
AS
$$
BEGIN
    INSERT
        INTO wallet (owner_type, owner_id, currency, balance)
        VALUES (tg_argv[0]::owner_type, new.id, 'VND', 0);

    RETURN new;
END
$$ LANGUAGE plpgsql;


DROP TABLE IF EXISTS service CASCADE;

CREATE TABLE service
(
    id           int PRIMARY KEY,
    name         varchar(255) NOT NULL,
    service_type varchar(255) NOT NULL,
    api_key      varchar(255) NOT NULL);

-- Partner
DROP SEQUENCE IF EXISTS partner_id_seq;

CREATE SEQUENCE partner_id_seq START 1;

CREATE OR REPLACE FUNCTION generate_partner_id() RETURNS char(10)
AS
$$
BEGIN
    RETURN LPAD(CAST(1000000000 + NEXTVAL('partner_id_seq') AS TEXT), 10, '0');
END;
$$ LANGUAGE plpgsql;


DROP TABLE IF EXISTS partner CASCADE;

CREATE TABLE partner
(
    id           char(10) PRIMARY KEY  DEFAULT generate_partner_id(),
    name         varchar(255) NOT NULL,
    description  text,
    email        VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    api_base_url VARCHAR(255) NOT NULL,
    api_key      VARCHAR(255) NOT NULL,
    balance      numeric      NOT NULL,
    created      date         NOT NULL DEFAULT CURRENT_DATE,
    UNIQUE (email),
    UNIQUE (api_key));

CREATE OR REPLACE TRIGGER partner_create_wallet_trigger
    AFTER INSERT
    ON partner
    FOR EACH ROW
EXECUTE FUNCTION create_wallet('partner');



-- User

-- user id generation
DROP SEQUENCE IF EXISTS user_id_seq;

CREATE SEQUENCE user_id_seq START 1;
-- drop sequence user_id_seq;

DROP FUNCTION IF EXISTS generate_user_id CASCADE;

CREATE OR REPLACE FUNCTION generate_user_id()
    RETURNS char(10)
AS
$$
BEGIN
    RETURN LPAD(CAST(1000000000 + NEXTVAL('user_id_seq') AS TEXT), 10, '0');
END;
$$ LANGUAGE plpgsql;


DROP TABLE IF EXISTS "user" CASCADE;

CREATE TABLE "user"
(
    id           char(10) PRIMARY KEY,
    first_name   varchar(50)  NOT NULL,
    last_name    varchar(50)  NOT NULL,
    email        varchar(255) NOT NULL,
    user_name    varchar(50),
    avatar       varchar(255) NULL,
    password     varchar(255) NOT NULL,
    dob          date         NOT NULL,
    is_active    boolean      NOT NULL            DEFAULT TRUE,
    is_verified  boolean      NOT NULL            DEFAULT FALSE,
    gender       boolean,
    created      date         NOT NULL            DEFAULT CURRENT_DATE,
    partner_id   char(10) REFERENCES partner (id) DEFAULT NULL,
    address      varchar(255),
    phone_number varchar(10),
    job          varchar(255),
    CHECK ( phone_number ~ '^[0-9]{10}$' ),
    CHECK ( email ~ '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$' ),
    CHECK ( user_name ~ '^[a-zA-Z0-9._%+-]{6,}$' ),
    CHECK ( dob <= CURRENT_DATE));

CREATE INDEX user_index ON "user" (email, user_name, phone_number);

CREATE INDEX user_email_index ON "user" (email);

CREATE INDEX user_un_index ON "user" (user_name);

CREATE INDEX user_pn_index ON "user" (phone_number);


CREATE OR REPLACE FUNCTION check_user_unique()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN

    --     raise exception 'partner_id %', NEW.partner_id;

    IF NEW.partner_id ISNULL THEN
        IF EXISTS ( SELECT 1 FROM "user" WHERE partner_id ISNULL AND email = NEW.email ) THEN
            RAISE EXCEPTION 'Email đã tồn tại';
            END IF;
        IF EXISTS ( SELECT 1 FROM "user" WHERE partner_id IS NULL AND phone_number = NEW.phone_number ) THEN
            RAISE EXCEPTION 'Số điện thoại đã tồn tại';
            END IF;
        IF EXISTS ( SELECT 1 FROM "user" WHERE partner_id IS NULL AND user_name = NEW.user_name ) THEN
            RAISE EXCEPTION 'Tên đăng nhập đã tồn tại';
            END IF;
        END IF;

    IF NEW.partner_id IS NOT NULL THEN
        IF EXISTS ( SELECT 1 FROM "user" WHERE partner_id = new.partner_id AND email = NEW.email ) THEN
            RAISE EXCEPTION 'Email đã tồn tại';
            END IF;
        IF EXISTS ( SELECT 1 FROM "user" WHERE partner_id = new.partner_id AND phone_number = NEW.phone_number ) THEN
            RAISE EXCEPTION 'Số điện thoại đã tồn tại';
            END IF;
        IF EXISTS ( SELECT 1 FROM "user" WHERE partner_id = new.partner_id AND user_name = NEW.user_name ) THEN
            RAISE EXCEPTION 'Tên đăng nhập đã tồn tại';
            END IF;
        END IF;

    new.id := generate_user_id();

    RETURN new;
END ;
$$;



CREATE OR REPLACE TRIGGER user_unique_trigger
    BEFORE INSERT
    ON "user"
    FOR EACH ROW
EXECUTE FUNCTION check_user_unique();

CREATE OR REPLACE TRIGGER user_create_wallet_trigger
    AFTER INSERT
    ON "user"
    FOR EACH ROW
EXECUTE FUNCTION create_wallet('user');



DROP TABLE IF EXISTS role CASCADE;

CREATE TABLE role
(
    id   int PRIMARY KEY,
    name varchar(50) NOT NULL);

-- Employee
DROP TABLE IF EXISTS employee CASCADE;

CREATE TABLE employee
(
    id      char(10) PRIMARY KEY REFERENCES "user" (id),
    salary  decimal(10, 2) NOT NULL,
    ssn     varchar(15)    NOT NULL UNIQUE,
    role_id int REFERENCES role (id));

DROP TABLE IF EXISTS transaction CASCADE;
DROP SEQUENCE IF EXISTS transaction_id_seq;

CREATE SEQUENCE transaction_id_seq START 100000000000001;

drop FUNCTION IF EXISTS generate_transaction_id CASCADE;
CREATE OR REPLACE FUNCTION generate_transaction_id()
    RETURNS char(15)
    LANGUAGE plpgsql
AS
$$
BEGIN
    return LPAD(NEXTVAL('transaction_id_seq')::text, 15, '0');
END;
$$;


CREATE TABLE transaction
(
    id                      char(15) PRIMARY KEY DEFAULT generate_transaction_id(),
    money                   decimal(10, 2)     NOT NULL,
    currency                VARCHAR(3)         NOT NULL,
    transaction_type        transaction_type   NOT NULL,
    transaction_target      transaction_target NOT NULL,
    status                  transaction_status NOT NULL DEFAULT 'pending',
    timestamp               TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP);



-- Order table
DROP TABLE IF EXISTS "order" CASCADE;

CREATE TABLE "order"
(
    id                      varchar(15) PRIMARY KEY,
    partner_id              char(10) REFERENCES partner (id),
    money                   decimal(10, 2) NOT NULL,
    status                  varchar(50)    NOT NULL,
    invoice_id              varchar(50)    NULL UNIQUE,
    transaction_id          varchar(15)    NULL REFERENCES transaction (id),
    external_transaction_id SERIAL REFERENCES payment_system (id),
    created                 date           NOT NULL DEFAULT CURRENT_DATE,
    updated                 date           NOT NULL DEFAULT CURRENT_DATE);


DROP SEQUENCE IF EXISTS order_id_seq;

CREATE SEQUENCE order_id_seq START 100000000000001;

CREATE OR REPLACE FUNCTION generate_order_id()
    RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    NEW.id := LPAD(NEXTVAL('order_id_seq')::text, 15, '0');
    RETURN NEW;
END;
$$;

CREATE OR REPLACE TRIGGER order_id_trigger
    BEFORE INSERT
    ON "order"
    FOR EACH ROW
EXECUTE FUNCTION generate_order_id();

/*
CREATE OR REPLACE FUNCTION check_order_transaction()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$

BEGIN
    IF NEW.status <> OLD.status THEN
        IF NEW.status = 'SUCCESS' THEN
--     check count transaction with order if
            IF (new.transaction_id IS NOT NULL) THEN
                RETURN new;
                ELSE
                    RAISE EXCEPTION 'Đơn hàng chưa được thanh toán, không thể chuyển trạng thái thành công';
                END IF;
            END IF;
        END IF;
    RETURN NEW;
END;
$$;*/

/*CREATE OR REPLACE TRIGGER check_order_transaction
    BEFORE UPDATE OR INSERT
    ON "order"
    FOR EACH ROW
EXECUTE FUNCTION check_order_transaction();*/

-- Transaction table

-- Support Ticket
DROP TABLE IF EXISTS support_ticket CASCADE;

CREATE TABLE support_ticket
(
    id          varchar(15) PRIMARY KEY,
    customer_id char(10) REFERENCES "user" (id),
    title       varchar(255) NOT NULL,
    content     text         NOT NULL,
    status      varchar(50)  NOT NULL);

-- Group Fund
DROP TABLE IF EXISTS group_fund CASCADE;

CREATE TABLE group_fund
(
    id          int PRIMARY KEY,
    name        varchar(255)   NOT NULL,
    description varchar(255),
    balance     decimal(10, 2) NOT NULL,
    target      decimal(10, 2) NOT NULL,
    owner_id    char(10) REFERENCES "user" (id));

DROP SEQUENCE IF EXISTS group_fund_id_seq;

DROP TABLE IF EXISTS fund_member CASCADE;

CREATE TABLE fund_member
(
    group_id  int REFERENCES group_fund (id),
    member_id char(10) REFERENCES "user" (id),
    money     numeric NOT NULL DEFAULT 0,
    PRIMARY KEY (group_id, member_id));


DROP TABLE IF EXISTS financial_statistic CASCADE;

CREATE TABLE financial_statistic

(
    id          INT PRIMARY KEY,
    creator     CHAR(10) REFERENCES "employee" (id),
    profit      numeric NOT NULL,
    income      numeric NOT NULL,
    outcome     numeric NOT NULL,
    total_money numeric NOT NULL,
    created     DATE    NOT NULL DEFAULT CURRENT_DATE);

/*drop table if exists customer_statistic cascade;
CREATE TABLE customer_statistic
(
    month    int                               NOT NULL,
    year     int                               NOT NULL,
    customer char(10) REFERENCES customer (id) NOT NULL,
    income   numeric                           NOT NULL,
    outcome  numeric                           NOT NULL,
    PRIMARY KEY (month, year, customer)
);*/

DROP TABLE IF EXISTS service_statistic CASCADE;

CREATE TABLE service_statistic
(
    month   int                         NOT NULL,
    year    int                         NOT NULL,
    service int REFERENCES service (id) NOT NULL,
    income  numeric                     NOT NULL,
    outcome numeric                     NOT NULL,
    PRIMARY KEY (month, year, service));

DROP TABLE IF EXISTS payment_system CASCADE;

CREATE TABLE payment_system
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(50)         NOT NULL,
    type       payment_system_type NOT NULL,
    api_key    VARCHAR(255),
    api_secret VARCHAR(255),
    is_active  BOOLEAN DEFAULT TRUE);


drop table if exists group_fund_transaction cascade;
CREATE TABLE group_fund_transaction

(
    transaction_id char(15) PRIMARY KEY REFERENCES transaction (id),
    group_id       int REFERENCES group_fund (id),
    member_id      char(10) REFERENCES "user" (id),
    money          numeric NOT NULL,
    created        date    NOT NULL DEFAULT CURRENT_DATE);
