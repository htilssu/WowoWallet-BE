ALTER TABLE group_fund_transaction
    ADD CONSTRAINT FK_GROUP_FUND_TRANSACTION_ON_TRANSACTION FOREIGN KEY (transaction_id) REFERENCES transaction (id);

ALTER TABLE group_fund
DROP
COLUMN balance;

ALTER TABLE group_fund_transaction
DROP
COLUMN transaction_date;

ALTER TABLE fund_member
ALTER
COLUMN member_id TYPE VARCHAR(32) USING (member_id::VARCHAR(32));