<h1 style="text-align: center">EWallet Application</h1>

## Description

[//]: # "application using spring boot, micro service architecture, and docker to create an e-userWallet application."

This is an e-userWallet application that allows users to create an account, deposit money, withdraw money, transfer money to
other users,
and view their transaction history. Linking service can using app. The application is built using Spring Boot,
microservice architecture, and Docker.

Technologies used:

- Spring Boot
- Spring Cloud
- Spring Data JPA
- Spring Security
- Docker
- Postgres SQL (AWS)
- Swagger
- Version Control
  - Git
  - GitHub
- API Documentation
  - Swagger
  - OpenAPI
  - Postman

## How to run the application

```bash
# Clone the repository
$ git clone https://github.com/htilssu/EWallet.git
```

#### You need to have create an env.properties file in the resources folder of each service and add the following properties:

```properties
#replace <server>, <port>, <database>, <username>, <password> with your own values
spring.datasource.url=jdbc:sqlserver://<server>:<port>;databaseName=<database>;encrypt=true;trustServerCertificate=false;loginTimeout=30;
# <username> is the username of the database
spring.datasource.username=<username>
# <password> is the password of the database
spring.datasource.password=<password>
```

### Using Docker

```bash
# Build the docker image
$ docker buildx build -t userWallet-service .
# Run the docker image
$ docker run -p 8080:8080 userWallet-service --network=host
```

# WowoWallet Backend

## Tính năng OTP

Hệ thống WowoWallet hỗ trợ gửi OTP qua hai kênh: Email và SMS.

### API Gửi OTP

#### 1. Gửi OTP qua Email (mặc định)

```
POST /api/otp/send
```

Body Request:

```json
{
  "userId": "user-123",
  "otpType": "PASSWORD_RESET",
  "transactionId": "tx-123" // Chỉ cần thiết cho OTP liên quan giao dịch
}
```

#### 2. Gửi OTP qua kênh chỉ định (Email hoặc SMS)

```
POST /api/otp/send/{channel}
```

Trong đó `{channel}` có thể là `EMAIL` hoặc `SMS`.

Body Request:

```json
{
  "userId": "user-123",
  "otpType": "PASSWORD_RESET",
  "transactionId": "tx-123" // Chỉ cần thiết cho OTP liên quan giao dịch
}
```

#### Các loại OTP hỗ trợ:

- `PASSWORD_RESET`: Đặt lại mật khẩu
- `EMAIL_VERIFICATION`: Xác minh email
- `ACCOUNT_VERIFICATION`: Xác minh tài khoản
- `TRANSACTION_CONFIRMATION`: Xác nhận giao dịch (cần transactionId)
- `WITHDRAW_CONFIRMATION`: Xác nhận rút tiền (cần transactionId)

### API Xác minh OTP

```
POST /api/otp/verify
```

Query Params:

- `userId`: ID của người dùng
- `otpCode`: Mã OTP cần xác minh
- `otpTypeStr`: Loại OTP (cùng loại với khi gửi)
- `transactionId`: ID giao dịch (chỉ cần thiết cho OTP liên quan giao dịch)

### Cấu hình SMS

Để sử dụng tính năng SMS OTP, cần cấu hình trong `application.properties`:

```properties
# SMS Configuration
sms.api.url=https://api.yoursmsgateway.com/sms
sms.api.key=your-sms-api-key
```
