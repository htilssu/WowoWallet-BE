ALTER TABLE financial_statistic
DROP
CONSTRAINT fk_financial_statistic_on_creator;

ALTER TABLE group_fund_transaction
DROP
CONSTRAINT fk_group_fund_transaction_on_group;

ALTER TABLE group_fund_transaction
DROP
CONSTRAINT fk_group_fund_transaction_on_member;

ALTER TABLE group_fund_transaction
DROP
CONSTRAINT fk_group_fund_transaction_on_transaction;

ALTER TABLE service_statistic
DROP
CONSTRAINT fk_service_statistic_on_service;

DROP TABLE financial_statistic CASCADE;

DROP TABLE group_fund_transaction CASCADE;

DROP TABLE service CASCADE;

DROP TABLE service_statistic CASCADE;

DROP TABLE user_partner CASCADE;

ALTER TABLE "user"
    ALTER COLUMN partner_id SET DEFAULT NULL::bpchar;