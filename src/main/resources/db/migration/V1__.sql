CREATE SEQUENCE IF NOT EXISTS atm_card_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS atm_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS wallet_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE atm
(
    id   INTEGER      NOT NULL,
    name VARCHAR(255) NOT NULL,
    icon VARCHAR(255),
    CONSTRAINT pk_atm PRIMARY KEY (id)
);

CREATE TABLE atm_card
(
    id          INTEGER                   NOT NULL,
    atm_id      INTEGER                   NOT NULL,
    card_number VARCHAR(16)               NOT NULL,
    ccv         VARCHAR(3),
    holder_name VARCHAR(255)              NOT NULL,
    owner_id    VARCHAR(10),
    expired     VARCHAR(255)              NOT NULL,
    created     date DEFAULT CURRENT_DATE NOT NULL,
    CONSTRAINT pk_atm_card PRIMARY KEY (id)
);

CREATE TABLE constant
(
    id      VARCHAR(50)      NOT NULL,
    name    VARCHAR(100)     NOT NULL,
    value   DOUBLE PRECISION NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_constant PRIMARY KEY (id)
);

CREATE TABLE employee
(
    id      VARCHAR(10)    NOT NULL,
    salary  DECIMAL(10, 2) NOT NULL,
    ssn     VARCHAR(15)    NOT NULL,
    role_id INTEGER,
    CONSTRAINT pk_employee PRIMARY KEY (id)
);

CREATE TABLE financial_statistic
(
    id          INTEGER                   NOT NULL,
    creator     VARCHAR(10),
    profit      DECIMAL                   NOT NULL,
    income      DECIMAL                   NOT NULL,
    outcome     DECIMAL                   NOT NULL,
    total_money DECIMAL                   NOT NULL,
    created     date DEFAULT CURRENT_DATE NOT NULL,
    CONSTRAINT pk_financial_statistic PRIMARY KEY (id)
);

CREATE TABLE fund_member
(
    money     DECIMAL DEFAULT 0 NOT NULL,
    group_id  INTEGER           NOT NULL,
    member_id VARCHAR(10)       NOT NULL,
    CONSTRAINT pk_fund_member PRIMARY KEY (group_id, member_id)
);

CREATE TABLE group_fund
(
    id          INTEGER        NOT NULL,
    name        VARCHAR(255)   NOT NULL,
    description VARCHAR(255),
    balance     DECIMAL(10, 2) NOT NULL,
    target      DECIMAL(10, 2) NOT NULL,
    owner_id    VARCHAR(10),
    CONSTRAINT pk_group_fund PRIMARY KEY (id)
);

CREATE TABLE group_fund_transaction
(
    transaction_id VARCHAR(15)               NOT NULL,
    group_id       INTEGER,
    member_id      VARCHAR(10),
    money          DECIMAL                   NOT NULL,
    created        date DEFAULT CURRENT_DATE NOT NULL,
    CONSTRAINT pk_group_fund_transaction PRIMARY KEY (transaction_id)
);

CREATE TABLE "order"
(
    id                      VARCHAR(15)                   NOT NULL,
    partner_id              VARCHAR(10),
    money                   DECIMAL(10, 2)                NOT NULL,
    status                  VARCHAR(50) DEFAULT 'PENDING' NOT NULL,
    transaction_id          VARCHAR(15),
    voucher_id              VARCHAR(50),
    voucher_name            VARCHAR(100),
    voucher_code            VARCHAR(100),
    order_id                VARCHAR(50),
    return_url              VARCHAR(300),
    success_url             VARCHAR(300),
    voucher_discount        DECIMAL(10, 2),
    external_transaction_id VARCHAR(50),
    created                 TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()     NOT NULL,
    updated                 TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()     NOT NULL,
    service_name            VARCHAR(100),
    CONSTRAINT pk_order PRIMARY KEY (id)
);

CREATE TABLE partner
(
    id           VARCHAR(10)      NOT NULL,
    name         VARCHAR(255)     NOT NULL,
    description  TEXT,
    email        VARCHAR(255)     NOT NULL,
    partner_type VARCHAR(100)     NOT NULL,
    avatar       VARCHAR(255),
    password     VARCHAR(255)     NOT NULL,
    api_base_url VARCHAR(255)     NOT NULL,
    api_key      VARCHAR(255)     NOT NULL,
    balance      DOUBLE PRECISION NOT NULL,
    created      date DEFAULT CURRENT_DATE,
    CONSTRAINT pk_partner PRIMARY KEY (id)
);

CREATE TABLE payment_method
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(50)                              NOT NULL,
    description VARCHAR(255),
    CONSTRAINT pk_payment_method PRIMARY KEY (id)
);

CREATE TABLE role
(
    id   INTEGER     NOT NULL,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE service
(
    id           INTEGER      NOT NULL,
    name         VARCHAR(255) NOT NULL,
    service_type VARCHAR(255) NOT NULL,
    api_key      VARCHAR(255) NOT NULL,
    CONSTRAINT pk_service PRIMARY KEY (id)
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

CREATE TABLE support_ticket
(
    id          VARCHAR(15)  NOT NULL,
    customer_id VARCHAR(10),
    title       VARCHAR(255) NOT NULL,
    content     TEXT         NOT NULL,
    status      VARCHAR(50)  NOT NULL,
    CONSTRAINT pk_support_ticket PRIMARY KEY (id)
);

CREATE TABLE transaction
(
    id                 VARCHAR(15)                    NOT NULL,
    money              DECIMAL(10, 2)                 NOT NULL,
    currency           VARCHAR(3)  DEFAULT 'VND'      NOT NULL,
    transaction_type   VARCHAR(20) DEFAULT 'transfer' NOT NULL,
    transaction_target VARCHAR(20) DEFAULT 'wallet'   NOT NULL,
    status             VARCHAR(50) DEFAULT 'PENDING'  NOT NULL,
    created            TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()      NOT NULL,
    updated            TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()      NOT NULL,
    sender_id          VARCHAR(10)                    NOT NULL,
    receiver_id        VARCHAR(10)                    NOT NULL,
    sender_type        VARCHAR(20) DEFAULT 'user'     NOT NULL,
    receiver_type      VARCHAR(20) DEFAULT 'user'     NOT NULL,
    CONSTRAINT pk_transaction PRIMARY KEY (id)
);

CREATE TABLE "user"
(
    id           VARCHAR(10)                      NOT NULL,
    first_name   VARCHAR(50)                      NOT NULL,
    last_name    VARCHAR(50)                      NOT NULL,
    email        VARCHAR(255)                     NOT NULL,
    user_name    VARCHAR(50),
    avatar       VARCHAR(255),
    password     VARCHAR(255)                     NOT NULL,
    dob          date                             NOT NULL,
    is_active    BOOLEAN     DEFAULT TRUE         NOT NULL,
    is_verified  BOOLEAN     DEFAULT FALSE        NOT NULL,
    gender       BOOLEAN,
    created      date        DEFAULT CURRENT_DATE NOT NULL,
    partner_id   VARCHAR(10) DEFAULT NULL::bpchar,
    address      VARCHAR(255),
    phone_number VARCHAR(10),
    job          VARCHAR(255),
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE user_partner
(
    user_id    VARCHAR(10) NOT NULL,
    partner_id VARCHAR(10) NOT NULL,
    CONSTRAINT pk_user_partner PRIMARY KEY (user_id, partner_id)
);

CREATE TABLE wallet
(
    id         INTEGER                         NOT NULL,
    owner_type VARCHAR(20)      DEFAULT 'user' NOT NULL,
    currency   VARCHAR(3)       DEFAULT 'VND'  NOT NULL,
    owner_id   VARCHAR(10),
    balance    DOUBLE PRECISION DEFAULT 0      NOT NULL,
    CONSTRAINT pk_wallet PRIMARY KEY (id)
);

CREATE TABLE wallet_transaction
(
    id              VARCHAR(15) NOT NULL,
    sender_wallet   INTEGER     NOT NULL,
    receiver_wallet INTEGER     NOT NULL,
    CONSTRAINT pk_wallettransaction PRIMARY KEY (id)
);

ALTER TABLE atm_card
    ADD CONSTRAINT atm_card_card_number_key UNIQUE (card_number);

ALTER TABLE employee
    ADD CONSTRAINT employee_ssn_key UNIQUE (ssn);

ALTER TABLE "order"
    ADD CONSTRAINT order_external_transaction_id_key UNIQUE (external_transaction_id);

ALTER TABLE "order"
    ADD CONSTRAINT order_id_partner_id_key UNIQUE (id, partner_id);

ALTER TABLE "order"
    ADD CONSTRAINT order_transaction_id_key UNIQUE (transaction_id);

ALTER TABLE wallet
    ADD CONSTRAINT uk_wallet_owner UNIQUE (owner_id, owner_type);

ALTER TABLE atm_card
    ADD CONSTRAINT FK_ATM_CARD_ON_OWNER FOREIGN KEY (owner_id) REFERENCES "user" (id);

ALTER TABLE employee
    ADD CONSTRAINT FK_EMPLOYEE_ON_ID FOREIGN KEY (id) REFERENCES "user" (id);

ALTER TABLE employee
    ADD CONSTRAINT FK_EMPLOYEE_ON_ROLE FOREIGN KEY (role_id) REFERENCES role (id);

ALTER TABLE financial_statistic
    ADD CONSTRAINT FK_FINANCIAL_STATISTIC_ON_CREATOR FOREIGN KEY (creator) REFERENCES employee (id);

ALTER TABLE fund_member
    ADD CONSTRAINT FK_FUND_MEMBER_ON_GROUP FOREIGN KEY (group_id) REFERENCES group_fund (id);

ALTER TABLE fund_member
    ADD CONSTRAINT FK_FUND_MEMBER_ON_MEMBER FOREIGN KEY (member_id) REFERENCES "user" (id);

ALTER TABLE group_fund
    ADD CONSTRAINT FK_GROUP_FUND_ON_OWNER FOREIGN KEY (owner_id) REFERENCES "user" (id);

ALTER TABLE group_fund_transaction
    ADD CONSTRAINT FK_GROUP_FUND_TRANSACTION_ON_GROUP FOREIGN KEY (group_id) REFERENCES group_fund (id);

ALTER TABLE group_fund_transaction
    ADD CONSTRAINT FK_GROUP_FUND_TRANSACTION_ON_MEMBER FOREIGN KEY (member_id) REFERENCES "user" (id);

ALTER TABLE group_fund_transaction
    ADD CONSTRAINT FK_GROUP_FUND_TRANSACTION_ON_TRANSACTION FOREIGN KEY (transaction_id) REFERENCES transaction (id);

ALTER TABLE "order"
    ADD CONSTRAINT FK_ORDER_ON_PARTNER FOREIGN KEY (partner_id) REFERENCES partner (id);

ALTER TABLE "order"
    ADD CONSTRAINT FK_ORDER_ON_TRANSACTION FOREIGN KEY (transaction_id) REFERENCES transaction (id);

ALTER TABLE service_statistic
    ADD CONSTRAINT FK_SERVICE_STATISTIC_ON_SERVICE FOREIGN KEY (service) REFERENCES service (id);

ALTER TABLE support_ticket
    ADD CONSTRAINT FK_SUPPORT_TICKET_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES "user" (id);

ALTER TABLE "user"
    ADD CONSTRAINT FK_USER_ON_PARTNER FOREIGN KEY (partner_id) REFERENCES partner (id);

ALTER TABLE wallet_transaction
    ADD CONSTRAINT FK_WALLETTRANSACTION_ON_ID FOREIGN KEY (id) REFERENCES transaction (id);

ALTER TABLE wallet_transaction
    ADD CONSTRAINT FK_WALLETTRANSACTION_ON_RECEIVER_WALLET FOREIGN KEY (receiver_wallet) REFERENCES wallet (id);

ALTER TABLE wallet_transaction
    ADD CONSTRAINT FK_WALLETTRANSACTION_ON_SENDER_WALLET FOREIGN KEY (sender_wallet) REFERENCES wallet (id);