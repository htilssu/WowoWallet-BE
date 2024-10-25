CREATE SEQUENCE IF NOT EXISTS group_fund_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS order_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS payment_system_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS role_id_seq START WITH 1 INCREMENT BY 1;

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
    id        VARCHAR(50)      NOT NULL,
    name      VARCHAR(100)     NOT NULL,
    col_value DOUBLE PRECISION NOT NULL,
    created   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_constant PRIMARY KEY (id)
);

CREATE TABLE employee
(
    id      VARCHAR(255) NOT NULL,
    salary  BIGINT       NOT NULL,
    ssn     VARCHAR(15)  NOT NULL,
    role_id INTEGER,
    CONSTRAINT pk_employee PRIMARY KEY (id)
);

CREATE TABLE fund_member
(
    money     BIGINT DEFAULT 0 NOT NULL,
    group_id  BIGINT           NOT NULL,
    member_id VARCHAR(255)      NOT NULL,
    CONSTRAINT pk_fund_member PRIMARY KEY (group_id, member_id)
);

CREATE TABLE group_fund
(
    id           BIGINT       NOT NULL,
    name         VARCHAR(255) NOT NULL,
    image        VARCHAR(256),
    type         VARCHAR(100),
    description  VARCHAR(255),
    balance      BIGINT       NOT NULL,
    target       BIGINT       NOT NULL,
    owner_id     VARCHAR(255),
    wallet_id    BIGINT,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    target_date  date         NOT NULL,
    CONSTRAINT pk_group_fund PRIMARY KEY (id)
);

CREATE TABLE group_fund_invitation
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    group_id     BIGINT                                  NOT NULL,
    sender_id    VARCHAR(255)                            NOT NULL,
    recipient_id VARCHAR(255)                            NOT NULL,
    status       VARCHAR(255),
    created_at   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_group_fund_invitation PRIMARY KEY (id)
);

CREATE TABLE group_fund_transaction
(
    transaction_id   VARCHAR(255) NOT NULL,
    group_id         BIGINT       NOT NULL,
    member_id        VARCHAR(255) NOT NULL,
    transaction_type VARCHAR(255) NOT NULL,
    transaction_date date,
    CONSTRAINT pk_group_fund_transaction PRIMARY KEY (transaction_id)
);

CREATE TABLE "order"
(
    id             BIGINT   NOT NULL,
    partner_id     VARCHAR(32),
    money          BIGINT   NOT NULL,
    status         SMALLINT NOT NULL,
    transaction_id VARCHAR(40),
    return_url     VARCHAR(300),
    success_url    VARCHAR(300),
    created        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    service_name   VARCHAR(100),
    CONSTRAINT pk_order PRIMARY KEY (id)
);

CREATE TABLE partner
(
    id          VARCHAR(32)  NOT NULL,
    description TEXT,
    email       VARCHAR(255) NOT NULL,
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
    id          VARCHAR(40) NOT NULL,
    amount      BIGINT      NOT NULL,
    status      SMALLINT    NOT NULL,
    type        SMALLINT    NOT NULL,
    variant     SMALLINT    NOT NULL,
    description VARCHAR(300),
    created     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_transaction PRIMARY KEY (id)
);

CREATE TABLE "user"
(
    id          VARCHAR(255)          NOT NULL,
    username    VARCHAR(255),
    email       VARCHAR(255),
    is_active   BOOLEAN DEFAULT TRUE  NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE NOT NULL,
    total_money BIGINT  DEFAULT 0     NOT NULL,
    job         VARCHAR(255),
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE wallet
(
    id         BIGINT                     NOT NULL,
    owner_type VARCHAR(20) DEFAULT 'user' NOT NULL,
    currency   VARCHAR(5)  DEFAULT 'VND'  NOT NULL,
    owner_id   VARCHAR(32),
    balance    BIGINT      DEFAULT 0      NOT NULL,
    version    BIGINT,
    CONSTRAINT pk_wallet PRIMARY KEY (id)
);

CREATE TABLE wallet_transaction
(
    id              VARCHAR(40) NOT NULL,
    sender_wallet   BIGINT      NOT NULL,
    receiver_wallet BIGINT      NOT NULL,
    CONSTRAINT pk_wallet_transaction PRIMARY KEY (id)
);

ALTER TABLE group_fund
    ADD CONSTRAINT uc_group_fund_wallet UNIQUE (wallet_id);

ALTER TABLE "order"
    ADD CONSTRAINT uc_order_transaction UNIQUE (transaction_id);

ALTER TABLE "user"
    ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE "user"
    ADD CONSTRAINT uc_user_username UNIQUE (username);

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

ALTER TABLE group_fund
    ADD CONSTRAINT FK_GROUP_FUND_ON_WALLET FOREIGN KEY (wallet_id) REFERENCES wallet (id);

ALTER TABLE group_fund_transaction
    ADD CONSTRAINT FK_GROUP_FUND_TRANSACTION_ON_GROUP FOREIGN KEY (group_id) REFERENCES group_fund (id);

ALTER TABLE group_fund_transaction
    ADD CONSTRAINT FK_GROUP_FUND_TRANSACTION_ON_MEMBER FOREIGN KEY (member_id) REFERENCES "user" (id);

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

ALTER TABLE group_fund_invitation
    ADD CONSTRAINT fk_group_fund_invitation_group
        FOREIGN KEY (group_id) REFERENCES group_fund (id);

ALTER TABLE group_fund_invitation
    ADD CONSTRAINT fk_group_fund_invitation_sender
        FOREIGN KEY (sender_id) REFERENCES "user" (id);

ALTER TABLE group_fund_invitation
    ADD CONSTRAINT fk_group_fund_invitation_recipient
        FOREIGN KEY (recipient_id) REFERENCES "user" (id);