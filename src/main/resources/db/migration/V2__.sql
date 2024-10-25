ALTER TABLE atm_card
DROP
CONSTRAINT fk_atm_card_on_bank;

ALTER TABLE atm_card
    ADD CONSTRAINT FK_ATM_CARD_ON_ATM FOREIGN KEY (atm_id) REFERENCES atm (id);

ALTER TABLE atm_card
DROP
COLUMN bank_id;

CREATE SEQUENCE IF NOT EXISTS atm_card_id_seq;
ALTER TABLE atm_card
    ALTER COLUMN id SET NOT NULL;
ALTER TABLE atm_card
    ALTER COLUMN id SET DEFAULT nextval('atm_card_id_seq');

ALTER SEQUENCE atm_card_id_seq OWNED BY atm_card.id;