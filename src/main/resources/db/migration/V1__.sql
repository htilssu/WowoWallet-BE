CREATE TABLE group_fund_members
(
    id            UUID         NOT NULL,
    group_fund_id UUID         NOT NULL,
    member_id     VARCHAR(255) NOT NULL,
    member_name   VARCHAR(255) NOT NULL,
    avatar_url    VARCHAR(500),
    role          VARCHAR(50)  NOT NULL,
    status        VARCHAR(50)  NOT NULL,
    created_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_group_fund_members PRIMARY KEY (id)
);

CREATE TABLE group_funds
(
    id          UUID         NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    owner_id    VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_group_funds PRIMARY KEY (id)
);

CREATE TABLE permissions
(
    id          UUID         NOT NULL,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    resource    VARCHAR(50)  NOT NULL,
    action      VARCHAR(50)  NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_permissions PRIMARY KEY (id)
);

CREATE TABLE role_permissions
(
    permission_id UUID NOT NULL,
    role_id       UUID NOT NULL,
    CONSTRAINT pk_role_permissions PRIMARY KEY (permission_id, role_id)
);

CREATE TABLE roles
(
    id          UUID        NOT NULL,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE transactions
(
    id               UUID           NOT NULL,
    source_wallet_id VARCHAR(255),
    target_wallet_id VARCHAR(255),
    amount           DECIMAL(19, 2) NOT NULL,
    currency         VARCHAR(3)     NOT NULL,
    type             VARCHAR(20)    NOT NULL,
    status           VARCHAR(20)    NOT NULL,
    description      VARCHAR(500),
    reference        VARCHAR(255),
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_transactions PRIMARY KEY (id)
);

CREATE TABLE user_permissions
(
    user_id       UUID NOT NULL,
    permission_id UUID NOT NULL,
    CONSTRAINT pk_user_permissions PRIMARY KEY (user_id, permission_id)
);

CREATE TABLE user_roles
(
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id)
);

CREATE TABLE users
(
    id           UUID         NOT NULL,
    username     VARCHAR(50)  NOT NULL,
    password     VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255),
    is_verified  BOOLEAN      NOT NULL,
    is_active    BOOLEAN      NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE wallets
(
    id         UUID           NOT NULL,
    owner_id   VARCHAR(255)   NOT NULL,
    owner_type VARCHAR(255)   NOT NULL,
    balance    DECIMAL(19, 2) NOT NULL,
    currency   VARCHAR(3)     NOT NULL,
    is_active  BOOLEAN        NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_wallets PRIMARY KEY (id)
);

ALTER TABLE group_fund_members
    ADD CONSTRAINT uc_8e54834b3a7769c67d58ba26e UNIQUE (group_fund_id, member_id);

ALTER TABLE permissions
    ADD CONSTRAINT uc_permissions_name UNIQUE (name);

ALTER TABLE roles
    ADD CONSTRAINT uc_roles_name UNIQUE (name);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

CREATE INDEX idx_users_email ON users (email);

CREATE INDEX idx_users_username ON users (username);

CREATE INDEX idx_wallets_owner_id_currency ON wallets (owner_id, currency);

ALTER TABLE group_fund_members
    ADD CONSTRAINT FK_GROUP_FUND_MEMBERS_ON_GROUP_FUND FOREIGN KEY (group_fund_id) REFERENCES group_funds (id);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_permission_jpa_entity FOREIGN KEY (permission_id) REFERENCES permissions (id);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_role_jpa_entity FOREIGN KEY (role_id) REFERENCES roles (id);