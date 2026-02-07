ALTER TABLE user_permissions
DROP
CONSTRAINT user_permissions_permission_id_fkey;

ALTER TABLE user_permissions
DROP
CONSTRAINT user_permissions_user_id_fkey;

ALTER TABLE user_roles
DROP
CONSTRAINT user_roles_role_id_fkey;

ALTER TABLE user_roles
DROP
CONSTRAINT user_roles_user_id_fkey;

ALTER TABLE wallets
    ADD owner_id VARCHAR(255);

ALTER TABLE wallets
    ADD owner_type VARCHAR(255);

ALTER TABLE wallets
    ALTER COLUMN owner_id SET NOT NULL;

ALTER TABLE wallets
    ALTER COLUMN owner_type SET NOT NULL;

CREATE INDEX idx_wallets_owner_id_currency ON wallets (owner_id, currency);

ALTER TABLE wallets
DROP
COLUMN user_id;

ALTER TABLE users
ALTER
COLUMN phone_number TYPE VARCHAR(255) USING (phone_number::VARCHAR(255));

CREATE INDEX idx_users_email ON users (email);

CREATE INDEX idx_users_username ON users (username);