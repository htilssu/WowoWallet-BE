CREATE SEQUENCE IF NOT EXISTS group_fund_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS payment_system_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS role_id_seq START WITH 1 INCREMENT BY 1;

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
    owner_id    VARCHAR(255),
    expired     VARCHAR(255)              NOT NULL,
    created     date DEFAULT CURRENT_DATE NOT NULL,
    CONSTRAINT pk_atm_card PRIMARY KEY (id)
);

CREATE TABLE banks
(
    id                 BIGINT NOT NULL,
    name               TEXT,
    code               TEXT,
    bin                TEXT,
    short_name         TEXT,
    logo               TEXT,
    transfer_supported BIGINT,
    lookup_supported   BIGINT,
    support            BIGINT,
    is_transfer        BIGINT,
    swift_code         TEXT,
    CONSTRAINT pk_banks PRIMARY KEY (id)
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
    id      BIGINT         NOT NULL,
    salary  DECIMAL(10, 2) NOT NULL,
    ssn     VARCHAR(15)    NOT NULL,
    role_id INTEGER,
    CONSTRAINT pk_employee PRIMARY KEY (id)
);

CREATE TABLE fund_member
(
    money     DECIMAL DEFAULT 0 NOT NULL,
    group_id  INTEGER           NOT NULL,
    member_id BIGINT            NOT NULL,
    CONSTRAINT pk_fund_member PRIMARY KEY (group_id, member_id)
);

CREATE TABLE group_fund
(
    id          BIGINT       NOT NULL,
    name        VARCHAR(255) NOT NULL,
    image       VARCHAR(256),
    description VARCHAR(255),
    balance     BIGINT       NOT NULL,
    target      BIGINT       NOT NULL,
    owner_id    VARCHAR(255),
    CONSTRAINT pk_group_fund PRIMARY KEY (id)
);

CREATE TABLE group_fund_transaction
(
    transaction_id BIGINT NOT NULL,
    group_id       BIGINT,
    member_id      VARCHAR(255),
    CONSTRAINT pk_group_fund_transaction PRIMARY KEY (transaction_id)
);

CREATE TABLE "order"
(
    id             VARCHAR(50)                               NOT NULL,
    partner_id     VARCHAR(32),
    money          BIGINT                                    NOT NULL,
    status         SMALLINT                                  NOT NULL,
    transaction_id VARCHAR(40),
    return_url     VARCHAR(300),
    success_url    VARCHAR(300),
    created        TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated        TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    service_name   VARCHAR(100),
    CONSTRAINT pk_order PRIMARY KEY (id)
);

CREATE TABLE partner
(
    id          VARCHAR(32)  NOT NULL,
    description TEXT,
    api_key     VARCHAR(255) NOT NULL,
    CONSTRAINT pk_partner PRIMARY KEY (id)
);

CREATE TABLE payment_method
(
    id          INTEGER     NOT NULL,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    CONSTRAINT pk_payment_method PRIMARY KEY (id)
);

CREATE TABLE payment_system
(
    id         INTEGER     NOT NULL,
    name       VARCHAR(50) NOT NULL,
    api_key    VARCHAR(255),
    api_secret VARCHAR(255),
    is_active  BOOLEAN DEFAULT TRUE,
    CONSTRAINT pk_payment_system PRIMARY KEY (id)
);

CREATE TABLE role
(
    id   INTEGER     NOT NULL,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE support_ticket
(
    id          VARCHAR(15)  NOT NULL,
    customer_id VARCHAR(255),
    title       VARCHAR(255) NOT NULL,
    content     TEXT         NOT NULL,
    status      SMALLINT     NOT NULL,
    CONSTRAINT pk_support_ticket PRIMARY KEY (id)
);

CREATE TABLE transaction
(
    id          VARCHAR(40)                 NOT NULL,
    money       BIGINT                      NOT NULL,
    status      SMALLINT                    NOT NULL,
    type        SMALLINT                    NOT NULL,
    variant     SMALLINT                    NOT NULL,
    description VARCHAR(300),
    created     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_transaction PRIMARY KEY (id)
);

CREATE TABLE "user"
(
    id          VARCHAR(255)          NOT NULL,
    is_active   BOOLEAN DEFAULT TRUE  NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE NOT NULL,
    job         VARCHAR(255),
    CONSTRAINT pk_user PRIMARY KEY (id)
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
    CONSTRAINT pk_wallet_transaction PRIMARY KEY (id)
);

ALTER TABLE "order"
    ADD CONSTRAINT uc_order_transaction UNIQUE (transaction_id);

ALTER TABLE atm_card
    ADD CONSTRAINT FK_ATM_CARD_ON_OWNER FOREIGN KEY (owner_id) REFERENCES "user" (id);

ALTER TABLE employee
    ADD CONSTRAINT FK_EMPLOYEE_ON_ID FOREIGN KEY (id) REFERENCES "user" (id);

ALTER TABLE employee
    ADD CONSTRAINT FK_EMPLOYEE_ON_ROLE FOREIGN KEY (role_id) REFERENCES role (id);

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

ALTER TABLE support_ticket
    ADD CONSTRAINT FK_SUPPORT_TICKET_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES "user" (id);

ALTER TABLE wallet_transaction
    ADD CONSTRAINT FK_WALLET_TRANSACTION_ON_ID FOREIGN KEY (id) REFERENCES transaction (id);

ALTER TABLE wallet_transaction
    ADD CONSTRAINT FK_WALLET_TRANSACTION_ON_RECEIVER_WALLET FOREIGN KEY (receiver_wallet) REFERENCES wallet (id);

ALTER TABLE wallet_transaction
    ADD CONSTRAINT FK_WALLET_TRANSACTION_ON_SENDER_WALLET FOREIGN KEY (sender_wallet) REFERENCES wallet (id);