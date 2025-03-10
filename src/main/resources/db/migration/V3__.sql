ALTER TABLE "order"
    ADD refund_transaction_id VARCHAR(40);

ALTER TABLE "order"
    ADD CONSTRAINT FK_ORDER_ON_REFUNDTRANSACTION FOREIGN KEY (refund_transaction_id) REFERENCES transaction (id);

ALTER TABLE transaction
ALTER
COLUMN type TYPE VARCHAR(31) USING (type::VARCHAR(31));