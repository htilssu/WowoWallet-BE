CREATE TABLE verify
(
    id          BIGINT       NOT NULL,
    customer_id VARCHAR(255),
    type        VARCHAR(255) NOT NULL,
    number_card BIGINT       NOT NULL,
    open_day    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    close_day   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    font_image  VARCHAR(255) NOT NULL,
    behind_image VARCHAR(255) NOT NULL,
    user_image  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_verify PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS verify_id_seq START WITH 1 INCREMENT BY 50;

ALTER TABLE verify
    ADD CONSTRAINT FK_VERIFY_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES "user" (id);