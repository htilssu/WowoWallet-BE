CREATE TABLE partner_api_key
(
    id         VARCHAR(255) NOT NULL,
    partner_id VARCHAR(32)  NOT NULL,
    CONSTRAINT pk_partner_api_key PRIMARY KEY (id)
);

ALTER TABLE partner_api_key
    ADD CONSTRAINT FK_PARTNER_API_KEY_ON_PARTNER FOREIGN KEY (partner_id) REFERENCES partner (id);