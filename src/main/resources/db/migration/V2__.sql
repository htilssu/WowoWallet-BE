ALTER TABLE "user"
    ADD email VARCHAR(255);

ALTER TABLE "user"
    ADD username VARCHAR(255);

ALTER TABLE "user"
    ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE "user"
    ADD CONSTRAINT uc_user_username UNIQUE (username);