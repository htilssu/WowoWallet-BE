<h1 style="text-align: center">EWallet Application</h1>

## Description

[//]: # (application using spring boot, micro service architecture, and docker to create an e-userWallet application.)

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
