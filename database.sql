CREATE TYPE payment_system_type AS ENUM ('INTERNAL', 'PAYPAL', 'STRIPE', 'OTHER');
create type payment_status as enum ('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED');

CREATE TABLE wallet
(
    id         serial PRIMARY KEY,
    owner_type varchar(20) NOT NULL DEFAULT 'USER',
    owner_id   char(10),
    balance    numeric     NOT NULL DEFAULT 0,
    CONSTRAINT uk_wallet_owner UNIQUE (owner_id, owner_type)
);

DROP TABLE IF EXISTS "user" CASCADE;

CREATE TABLE "user"
(
    id           char(10) PRIMARY KEY,
    first_name   varchar(50)  NOT NULL,
    last_name    varchar(50)  NOT NULL,
    email        varchar(255) NOT NULL,
    user_name    varchar(50),
    avatar       varchar(255) NULL,
    password     varchar(255) NOT NULL,
    dob          date         NOT NULL,
    is_active    boolean      NOT NULL DEFAULT TRUE,
    is_verified  boolean      NOT NULL DEFAULT FALSE,
    gender       boolean,
    created      date         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    address      varchar(255),
    phone_number varchar(10),
    job          varchar(255),
    CHECK ( phone_number ~ '^[0-9]{10}$' ),
    CHECK ( email ~ '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$' ),
    CHECK ( user_name ~ '^[a-zA-Z0-9._%+-]{6,}$' ),
    CHECK ( dob <= CURRENT_DATE)
);

CREATE INDEX user_index ON "user" (email, user_name, phone_number);

CREATE INDEX user_email_index ON "user" (email);

CREATE INDEX user_un_index ON "user" (user_name);

CREATE INDEX user_pn_index ON "user" (phone_number);

DROP TABLE IF EXISTS role CASCADE;

CREATE TABLE role
(
    id   int PRIMARY KEY,
    name varchar(50) NOT NULL
);

-- Employee
DROP TABLE IF EXISTS employee CASCADE;

CREATE TABLE employee
(
    id      char(10) PRIMARY KEY REFERENCES "user" (id),
    salary  decimal(10, 2) NOT NULL,
    ssn     varchar(15)    NOT NULL UNIQUE,
    role_id int REFERENCES role (id)
);


DROP SEQUENCE IF EXISTS transaction_id_seq;


CREATE SEQUENCE transaction_id_seq START 100000000000001;


CREATE TABLE payment_system
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(50)         NOT NULL,
    type       payment_system_type NOT NULL,
    api_key    VARCHAR(255),
    api_secret VARCHAR(255),
    is_active  BOOLEAN DEFAULT true
);
DROP TABLE IF EXISTS transaction CASCADE;




create table constant
(
    id    varchar(50) PRIMARY KEY,
    name  varchar(100)     NOT NULL,
    value double precision NOT NULL
);


-- Order table
create sequence order_id_seq start 100000000000000;



DROP TABLE IF EXISTS "order" CASCADE;

CREATE TABLE "order"
(
    partner_id              varchar(32)    NOT NULL,
    id                      varchar(15) PRIMARY KEY default nextval('order_id_seq'),
    money                   decimal(10, 2) NOT NULL,
    status                  varchar(50)    NOT NULL default 'PENDING',
--     invoice_id              varchar(50)    NULL UNIQUE,
    transaction_id          varchar(15)    NULL REFERENCES transaction (id),
    voucher_id              varchar(50)    NULL,
    order_id                varchar(50)    NULL,
    success_url             varchar(300)   NULL,
    return_url              varchar(300)   NULL,
    external_transaction_id varchar(50)    NULL,
    created                 timestamp      NOT NULL DEFAULT current_timestamp,
    updated                 timestamp      NOT NULL DEFAULT current_timestamp,
    unique (external_transaction_id),
    unique (transaction_id)
);

-- Support Ticket
DROP TABLE IF EXISTS support_ticket CASCADE;

CREATE TABLE support_ticket
(
    id          varchar(15) PRIMARY KEY,
    customer_id char(10) REFERENCES "user" (id),
    title       varchar(255) NOT NULL,
    content     text         NOT NULL,
    status      varchar(50)  NOT NULL
);

CREATE TABLE transaction
(
    id                 varchar(17) PRIMARY KEY DEFAULT nextval('transaction_id_seq') NOT NULL,
    money              decimal(10, 2) NOT NULL,
    receiver_wallet_id serial         NOT NULL,
    sender_wallet_id   serial         NOT NULL,
    status             payment_status NOT NULL DEFAULT 'PENDING',
    created            TIMESTAMP(3)   NOT NULL DEFAULT current_timestamp(3),
    updated            TIMESTAMP(3)   NOT NULL DEFAULT current_timestamp(3)
);
-- Group Fund
--lưu thông tin về từng quỹ nhóm
DROP TABLE IF EXISTS group_fund CASCADE;

CREATE TABLE group_fund
(
    id          bigserial PRIMARY KEY,
    name        varchar(255)   NOT NULL,
    description varchar(255),
    balance    numeric     NOT NULL DEFAULT 0,
    target      decimal(10, 2) NOT NULL,
    owner_id    char(10) REFERENCES "user" (id)
);

DROP SEQUENCE IF EXISTS group_fund_id_seq;

--lưu các thông tin thành viên trong quỹ nhóm đó
DROP TABLE IF EXISTS fund_member CASCADE;
CREATE TABLE fund_member
(
    group_id  int REFERENCES group_fund (id) ON DELETE CASCADE, -- Xoá các thành viên khi quỹ bị xóa
    member_id varchar(36) REFERENCES "user" (id),
    money     numeric NOT NULL DEFAULT 0,
    joined_at TIMESTAMP DEFAULT current_timestamp,
    PRIMARY KEY (group_id, member_id)
);

--lưu các giao dịch trong quỹ nhóm đó
drop table if exists group_fund_transaction cascade;
-- Tạo kiểu ENUM cho transaction_type
CREATE TYPE transaction_type AS ENUM ('WITHDRAW', 'CONTRIBUTE');

CREATE TABLE group_fund_transaction
(
    transaction_id varchar(17) PRIMARY KEY REFERENCES transaction (id),
    group_id       int REFERENCES group_fund (id) ON DELETE CASCADE,
    member_id      varchar(36) REFERENCES "user" (id),
    transaction_type transaction_type NOT NULL, -- Chỉ có hai loại: 'WITHDRAW' và 'CONTRIBUTE'
    amount         decimal(10, 2) NOT NULL, -- Số tiền của giao dịch
    created_at     TIMESTAMP DEFAULT current_timestamp
);

create table wallet_transaction
(
    transaction_id  char(15) primary key references transaction (id),
    sender_wallet   int references wallet (id),
    receiver_wallet int references wallet (id)
);

CREATE TABLE banks
(
    "id"                bigint PRIMARY KEY,
    "name"              text,
    "code"              text,
    "bin"               text,
    "shortName"         text,
    "logo"              text,
    "transferSupported" bigint,
    "lookupSupported"   bigint,
    "short_name"        text,
    "support"           bigint,
    "isTransfer"        bigint,
    "swift_code"        text NULL
);
create table atm_card
(
    id          serial primary key,
    atm_id      int /*references atm (id)*/,
    card_number varchar(16)  not null,
    ccv         varchar(3),
    holder_name varchar(255) not null,
    owner_id    char(10) references "user" (id),
    expired     varchar(200) not null,
    created     date         not null default current_date,
    unique (card_number)
);

create index wallet_transaction_sd_idx on wallet_transaction (sender_wallet);
create index wallet_transaction_rc_idx on wallet_transaction (receiver_wallet);

insert into constant
values ('MIN_TRANSFER', 'Số tiền tối thiểu cho mỗi giao dịch', 100),
       ('MAX_TRANSFER', 'Số tiền tối đa cho mỗi giao dịch', 1000000000),
       ('MIN_WITHDRAW', 'Số tiền tối thiểu cho mỗi giao dịch', 10000),
       ('MAX_WITHDRAW', 'Số tiền tối đa cho mỗi giao dịch', 1000000000);


INSERT INTO banks ("id", "name", "code", "bin", "shortName", "logo", "transferSupported", "lookupSupported",
                   "short_name", "support", "isTransfer", "swift_code")
VALUES (17, 'Ngân hàng TMCP Công thương Việt Nam', 'ICB', '970415', 'VietinBank', 'https://api.vietqr.io/img/ICB.png',
        1, 1, 'VietinBank', 3, 1, 'ICBVVNVX'),
       (43, 'Ngân hàng TMCP Ngoại Thương Việt Nam', 'VCB', '970436', 'Vietcombank',
        'https://api.vietqr.io/img/VCB.png', 1, 1, 'Vietcombank', 3, 1, 'BFTVVNVX'),
       (4, 'Ngân hàng TMCP Đầu tư và Phát triển Việt Nam', 'BIDV', '970418', 'BIDV',
        'https://api.vietqr.io/img/BIDV.png', 1, 1, 'BIDV', 3, 1, 'BIDVVNVX'),
       (42, 'Ngân hàng Nông nghiệp và Phát triển Nông thôn Việt Nam', 'VBA', '970405', 'Agribank',
        'https://api.vietqr.io/img/VBA.png', 1, 1, 'Agribank', 3, 1, 'VBAAVNVX'),
       (26, 'Ngân hàng TMCP Phương Đông', 'OCB', '970448', 'OCB', 'https://api.vietqr.io/img/OCB.png', 1, 1, 'OCB', 3,
        1, 'ORCOVNVX'),
       (21, 'Ngân hàng TMCP Quân đội', 'MB', '970422', 'MBBank', 'https://api.vietqr.io/img/MB.png', 1, 1, 'MBBank', 3,
        1, 'MSCBVNVX'),
       (38, 'Ngân hàng TMCP Kỹ thương Việt Nam', 'TCB', '970407', 'Techcombank', 'https://api.vietqr.io/img/TCB.png', 1,
        1, 'Techcombank', 3, 1, 'VTCBVNVX'),
       (2, 'Ngân hàng TMCP Á Châu', 'ACB', '970416', 'ACB', 'https://api.vietqr.io/img/ACB.png', 1, 1, 'ACB', 3, 1,
        'ASCBVNVX'),
       (47, 'Ngân hàng TMCP Việt Nam Thịnh Vượng', 'VPB', '970432', 'VPBank', 'https://api.vietqr.io/img/VPB.png', 1, 1,
        'VPBank', 3, 1, 'VPBKVNVX'),
       (39, 'Ngân hàng TMCP Tiên Phong', 'TPB', '970423', 'TPBank', 'https://api.vietqr.io/img/TPB.png', 1, 1, 'TPBank',
        3, 1, 'TPBVVNVX'),
       (36, 'Ngân hàng TMCP Sài Gòn Thương Tín', 'STB', '970403', 'Sacombank', 'https://api.vietqr.io/img/STB.png', 1,
        1, 'Sacombank', 3, 1, 'SGTTVNVX'),
       (12, 'Ngân hàng TMCP Phát triển Thành phố Hồ Chí Minh', 'HDB', '970437', 'HDBank',
        'https://api.vietqr.io/img/HDB.png', 1, 1, 'HDBank', 3, 1, 'HDBCVNVX'),
       (44, 'Ngân hàng TMCP Bản Việt', 'VCCB', '970454', 'VietCapitalBank', 'https://api.vietqr.io/img/VCCB.png', 1,
        1, 'VietCapitalBank', 3, 1, 'VCBCVNVX'),
       (31, 'Ngân hàng TMCP Sài Gòn', 'SCB', '970429', 'SCB', 'https://api.vietqr.io/img/SCB.png', 1, 1, 'SCB', 3, 1,
        'SACLVNVX'),
       (45, 'Ngân hàng TMCP Quốc tế Việt Nam', 'VIB', '970441', 'VIB', 'https://api.vietqr.io/img/VIB.png', 1, 1,
        'VIB', 3, 1, 'VNIBVNVX'),
       (35, 'Ngân hàng TMCP Sài Gòn - Hà Nội', 'SHB', '970443', 'SHB', 'https://api.vietqr.io/img/SHB.png', 1, 1, 'SHB',
        3, 1, 'SHBAVNVX'),
       (10, 'Ngân hàng TMCP Xuất Nhập khẩu Việt Nam', 'EIB', '970431', 'Eximbank', 'https://api.vietqr.io/img/EIB.png',
        1, 1, 'Eximbank', 3, 1, 'EBVIVNVX'),
       (22, 'Ngân hàng TMCP Hàng Hải', 'MSB', '970426', 'MSB', 'https://api.vietqr.io/img/MSB.png', 1, 1, 'MSB', 3,
        1, 'MCOBVNVX'),
       (53, 'TMCP Việt Nam Thịnh Vượng - Ngân hàng số CAKE by VPBank', 'CAKE', '546034', 'CAKE',
        'https://api.vietqr.io/img/CAKE.png', 1, 1, 'CAKE', 3, 1, NULL),
       (54, 'TMCP Việt Nam Thịnh Vượng - Ngân hàng số Ubank by VPBank', 'Ubank', '546035', 'Ubank',
        'https://api.vietqr.io/img/UBANK.png', 1, 1, 'Ubank', 3, 1, NULL),
       (58, 'Ngân hàng số Timo by Ban Viet Bank (Timo by Ban Viet Bank)', 'TIMO', '963388', 'Timo',
        'https://vietqr.net/portal-service/resources/icons/TIMO.png', 1, 0, 'Timo', 0, 1, NULL),
       (57, 'Tổng Công ty Dịch vụ số Viettel - Chi nhánh tập đoàn công nghiệp viễn thông Quân Đội', 'VTLMONEY',
        '971005', 'ViettelMoney', 'https://api.vietqr.io/img/VIETTELMONEY.png', 0, 1, 'ViettelMoney', 0, 0, NULL),
       (56, 'VNPT Money', 'VNPTMONEY', '971011', 'VNPTMoney', 'https://api.vietqr.io/img/VNPTMONEY.png', 0, 1,
        'VNPTMoney', 0, 0, NULL),
       (34, 'Ngân hàng TMCP Sài Gòn Công Thương', 'SGICB', '970400', 'SaigonBank',
        'https://api.vietqr.io/img/SGICB.png', 1, 1, 'SaigonBank', 3, 1, 'SBITVNVX'),
       (3, 'Ngân hàng TMCP Bắc Á', 'BAB', '970409', 'BacABank', 'https://api.vietqr.io/img/BAB.png', 1, 1, 'BacABank',
        3, 1, 'NASCVNVX'),
       (30, 'Ngân hàng TMCP Đại Chúng Việt Nam', 'PVCB', '970412', 'PVcomBank',
        'https://api.vietqr.io/img/PVCB.png', 1, 1, 'PVcomBank', 3, 1, 'WBVNVNVX'),
       (27, 'Ngân hàng Thương mại TNHH MTV Đại Dương', 'Oceanbank', '970414', 'Oceanbank',
        'https://api.vietqr.io/img/OCEANBANK.png', 1, 1, 'Oceanbank', 3, 1, 'OCBKUS3M'),
       (24, 'Ngân hàng TMCP Quốc Dân', 'NCB', '970419', 'NCB', 'https://api.vietqr.io/img/NCB.png', 1, 1, 'NCB', 3, 1,
        'NVBAVNVX'),
       (37, 'Ngân hàng TNHH MTV Shinhan Việt Nam', 'SHBVN', '970424', 'ShinhanBank',
        'https://api.vietqr.io/img/SHBVN.png', 1, 1, 'ShinhanBank', 3, 1, 'SHBKVNVX'),
       (1, 'Ngân hàng TMCP An Bình', 'ABB', '970425', 'ABBANK', 'https://api.vietqr.io/img/ABB.png', 1, 1, 'ABBANK', 3,
        1, 'ABBKVNVX'),
       (41, 'Ngân hàng TMCP Việt Á', 'VAB', '970427', 'VietABank', 'https://api.vietqr.io/img/VAB.png', 1, 1,
        'VietABank', 3, 1, 'VNACVNVX'),
       (23, 'Ngân hàng TMCP Nam Á', 'NAB', '970428', 'NamABank', 'https://api.vietqr.io/img/NAB.png', 1, 1, 'NamABank',
        3, 1, 'NAMAVNVX'),
       (29, 'Ngân hàng TMCP Xăng dầu Petrolimex', 'PGB', '970430', 'PGBank', 'https://api.vietqr.io/img/PGB.png', 1, 1,
        'PGBank', 3, 1, 'PGBLVNVX'),
       (46, 'Ngân hàng TMCP Việt Nam Thương Tín', 'VIETBANK', '970433', 'VietBank',
        'https://api.vietqr.io/img/VIETBANK.png', 1, 1, 'VietBank', 3, 1, 'VNTTVNVX'),
       (5, 'Ngân hàng TMCP Bảo Việt', 'BVB', '970438', 'BaoVietBank', 'https://api.vietqr.io/img/BVB.png', 1, 1,
        'BaoVietBank', 3, 1, 'BVBVVNVX'),
       (33, 'Ngân hàng TMCP Đông Nam Á', 'SEAB', '970440', 'SeABank', 'https://api.vietqr.io/img/SEAB.png', 1, 1,
        'SeABank', 3, 1, 'SEAVVNVX'),
       (52, 'Ngân hàng Hợp tác xã Việt Nam', 'COOPBANK', '970446', 'COOPBANK', 'https://api.vietqr.io/img/COOPBANK.png',
        1, 1, 'COOPBANK', 3, 1, NULL),
       (20, 'Ngân hàng TMCP Lộc Phát Việt Nam', 'LPB', '970449', 'LPBank', 'https://api.vietqr.io/img/LPB.png', 1, 1,
        'LPBank', 3, 1, 'LVBKVNVX'),
       (19, 'Ngân hàng TMCP Kiên Long', 'KLB', '970452', 'KienLongBank', 'https://api.vietqr.io/img/KLB.png', 1, 1,
        'KienLongBank', 3, 1, 'KLBKVNVX'),
       (55, 'Ngân hàng Đại chúng TNHH Kasikornbank', 'KBank', '668888', 'KBank', 'https://api.vietqr.io/img/KBANK.png',
        1, 1, 'KBank', 3, 1, 'KASIVNVX'),
       (50, 'Ngân hàng Kookmin - Chi nhánh Hà Nội', 'KBHN', '970462', 'KookminHN', 'https://api.vietqr.io/img/KBHN.png',
        0, 0, 'KookminHN', 0, 0, NULL),
       (60, 'Ngân hàng KEB Hana – Chi nhánh Thành phố Hồ Chí Minh', 'KEBHANAHCM', '970466', 'KEBHanaHCM',
        'https://api.vietqr.io/img/KEBHANAHCM.png', 0, 0, 'KEBHanaHCM', 0, 0, NULL),
       (61, 'Ngân hàng KEB Hana – Chi nhánh Hà Nội', 'KEBHANAHN', '970467', 'KEBHANAHN',
        'https://api.vietqr.io/img/KEBHANAHN.png', 0, 0, 'KEBHANAHN', 0, 0, NULL),
       (62, 'Công ty Tài chính TNHH MTV Mirae Asset (Việt Nam) ', 'MAFC', '977777', 'MAFC',
        'https://api.vietqr.io/img/MAFC.png', 0, 0, 'MAFC', 0, 0, NULL),
       (59, 'Ngân hàng Citibank, N.A. - Chi nhánh Hà Nội', 'CITIBANK', '533948', 'Citibank',
        'https://api.vietqr.io/img/CITIBANK.png', 0, 0, 'Citibank', 0, 0, NULL),
       (51, 'Ngân hàng Kookmin - Chi nhánh Thành phố Hồ Chí Minh', 'KBHCM', '970463', 'KookminHCM',
        'https://api.vietqr.io/img/KBHCM.png', 0, 0, 'KookminHCM', 0, 0, NULL),
       (63, 'Ngân hàng Chính sách Xã hội', 'VBSP', '999888', 'VBSP', 'https://api.vietqr.io/img/VBSP.png', 0, 0, 'VBSP',
        0, 0, NULL),
       (49, 'Ngân hàng TNHH MTV Woori Việt Nam', 'WVN', '970457', 'Woori', 'https://api.vietqr.io/img/WVN.png', 1, 1,
        'Woori', 0, 1, NULL),
       (48, 'Ngân hàng Liên doanh Việt - Nga', 'VRB', '970421', 'VRB', 'https://api.vietqr.io/img/VRB.png', 0, 1, 'VRB',
        0, 0, NULL),
       (40, 'Ngân hàng United Overseas - Chi nhánh TP. Hồ Chí Minh', 'UOB', '970458', 'UnitedOverseas',
        'https://api.vietqr.io/img/UOB.png', 0, 1, 'UnitedOverseas', 0, 0, NULL),
       (32, 'Ngân hàng TNHH MTV Standard Chartered Bank Việt Nam', 'SCVN', '970410', 'StandardChartered',
        'https://api.vietqr.io/img/SCVN.png', 0, 1, 'StandardChartered', 0, 0, 'SCBLVNVX'),
       (28, 'Ngân hàng TNHH MTV Public Việt Nam', 'PBVN', '970439', 'PublicBank', 'https://api.vietqr.io/img/PBVN.png',
        0, 1, 'PublicBank', 0, 0, 'VIDPVNVX'),
       (25, 'Ngân hàng Nonghyup - Chi nhánh Hà Nội', 'NHB HN', '801011', 'Nonghyup',
        'https://api.vietqr.io/img/NHB.png', 0, 0, 'Nonghyup', 0, 0, NULL),
       (18, 'Ngân hàng TNHH Indovina', 'IVB', '970434', 'IndovinaBank', 'https://api.vietqr.io/img/IVB.png', 0, 1,
        'IndovinaBank', 0, 0, NULL),
       (16, 'Ngân hàng Công nghiệp Hàn Quốc - Chi nhánh TP. Hồ Chí Minh', 'IBK - HCM', '970456', 'IBKHCM',
        'https://api.vietqr.io/img/IBK.png', 0, 0, 'IBKHCM', 0, 0, NULL),
       (15, 'Ngân hàng Công nghiệp Hàn Quốc - Chi nhánh Hà Nội', 'IBK - HN', '970455', 'IBKHN',
        'https://api.vietqr.io/img/IBK.png', 0, 0, 'IBKHN', 0, 0, NULL),
       (14, 'Ngân hàng TNHH MTV HSBC (Việt Nam)', 'HSBC', '458761', 'HSBC', 'https://api.vietqr.io/img/HSBC.png', 0, 1,
        'HSBC', 0, 0, 'HSBCVNVX'),
       (13, 'Ngân hàng TNHH MTV Hong Leong Việt Nam', 'HLBVN', '970442', 'HongLeong',
        'https://api.vietqr.io/img/HLBVN.png', 0, 1, 'HongLeong', 0, 0, 'HLBBVNVX'),
       (11, 'Ngân hàng Thương mại TNHH MTV Dầu Khí Toàn Cầu', 'GPB', '970408', 'GPBank',
        'https://api.vietqr.io/img/GPB.png', 0, 1, 'GPBank', 0, 0, 'GBNKVNVX'),
       (9, 'Ngân hàng TMCP Đông Á', 'DOB', '970406', 'DongABank', 'https://api.vietqr.io/img/DOB.png', 0, 1,
        'DongABank', 0, 0, 'EACBVNVX'),
       (8, 'DBS Bank Ltd - Chi nhánh Thành phố Hồ Chí Minh', 'DBS', '796500', 'DBSBank',
        'https://api.vietqr.io/img/DBS.png', 0, 0, 'DBSBank', 0, 0, 'DBSSVNVX'),
       (7, 'Ngân hàng TNHH MTV CIMB Việt Nam', 'CIMB', '422589', 'CIMB', 'https://api.vietqr.io/img/CIMB.png', 1, 1,
        'CIMB', 0, 1, 'CIBBVNVN'),
       (6, 'Ngân hàng Thương mại TNHH MTV Xây dựng Việt Nam', 'CBB', '970444', 'CBBank',
        'https://api.vietqr.io/img/CBB.png', 0, 1, 'CBBank', 0, 0, 'GTBAVNVX');