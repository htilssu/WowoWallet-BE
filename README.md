<h1 style="text-align: center">EWallet Application</h1>

## Description

This is an e-wallet application that allows users to create an account, deposit money, withdraw money, transfer money to
other users, and view their transaction history. The application is built using **Clean Architecture** principles with **SOLID design** and modern **Design Patterns**.

## 🎉 Architecture Refactoring (November 2024)

This project has been refactored following **Clean Architecture** with:
- ✅ **SOLID Principles** implementation
- ✅ **10+ Design Patterns** (Strategy, Factory, Command, Observer, etc.)
- ✅ **Hexagonal Architecture** (Ports & Adapters)
- ✅ **Event-Driven Architecture**
- ✅ **Rich Domain Models** with Value Objects

### 📚 Documentation

**Start here:**
- 📖 **[QUICK START GUIDE](QUICK_START_GUIDE.md)** - Practical guide for developers
- 📊 **[PROJECT SUMMARY](PROJECT_SUMMARY.md)** - Overview & statistics
- 🏗️ **[ARCHITECTURE DIAGRAMS](ARCHITECTURE_DIAGRAMS.md)** - Visual architecture
- 📋 **[REFACTORING GUIDE](REFACTORING_GUIDE.md)** - Detailed architecture guide
- 📝 **[IMPLEMENTATION SUMMARY](IMPLEMENTATION_SUMMARY.md)** - Implementation status

### Technologies used:

**Core:**
- Spring Boot 3.3.0
- Java 22
- Spring Cloud
- Spring Data JPA
- Spring Security

**Architecture:**
- Clean Architecture
- Hexagonal Architecture
- Domain-Driven Design (DDD)
- Event-Driven Architecture

**Databases:**
- PostgreSQL (AWS)
- MongoDB
- DynamoDB

**Message Broker:**
- Apache Kafka

**Development:**
- Docker & Docker Compose
- Swagger/OpenAPI
- Git & GitHub

**Design Patterns:**
- Strategy Pattern
- Factory Pattern
- Command Pattern
- Chain of Responsibility
- Observer Pattern
- Facade Pattern
- Adapter Pattern
- Repository Pattern
- Value Object Pattern
- Result Pattern

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
