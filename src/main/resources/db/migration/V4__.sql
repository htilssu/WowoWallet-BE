ALTER TABLE atm_card
DROP
CONSTRAINT fk_atm_card_on_atm;

ALTER TABLE atm_card
    ADD bank_id INTEGER;

ALTER TABLE atm_card
    ALTER COLUMN bank_id SET NOT NULL;

ALTER TABLE atm_card
    ADD CONSTRAINT FK_ATM_CARD_ON_BANK FOREIGN KEY (bank_id) REFERENCES banks (id);

ALTER TABLE atm_card
DROP
COLUMN atm_id;