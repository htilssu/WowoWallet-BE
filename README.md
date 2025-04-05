# 🏦 WowoWallet: Advanced E-Wallet Backend System

## 📋 Project Overview

A robust fintech solution providing comprehensive financial services with advanced security features and modern architectural patterns.

## 🚀 Core Features

### 💰 Financial Transaction Management

- **Multi-channel Payment Processing**

  - Implemented payment gateway integrations using Strategy Pattern
  - Supported multiple payment methods (PayPal, ATM cards)
  - Built secure transaction workflows with validation stages

- **Secure Money Operations**
  - Developed transfer, withdrawal, and deposit functionalities
  - Implemented fraud detection and prevention mechanisms
  - Created transaction history with comprehensive audit trails
  - Designed automated refund processing and dispute resolution

### 🔐 Security Architecture

- **Advanced Authentication**

  - Built JWT-based authentication system
  - Implemented custom security filters (TokenFilter, ApiServiceFilter)
  - Created secure password management with salted encryption

- **Authorization Framework**
  - Designed role-based access control (Admin, User)
  - Implemented fine-grained API access permissions
  - Developed secure session management

### 📱 Multi-Channel OTP System

- **Bridge Pattern Implementation**

  - Separated OTP types from delivery channels for maximum flexibility
  - Supported both Email and SMS delivery methods
  - Enhanced system maintainability and extensibility

- **OTP Factory Pattern**

  - Created context-aware OTP generation for different use cases
  - Implemented specialized OTP types (Password Reset, Email Verification, Transaction)
  - Built verification workflows with expiration handling

- **SMS Integration**
  - Integrated eSMS API for reliable SMS delivery
  - Added support for Vietnamese-language messages
  - Implemented delivery status tracking and error handling

### 👥 Group Fund Management

- **Collaborative Financial Tools**

  - Designed shared fund pools for multiple users
  - Developed member invitation and management system
  - Created permission hierarchy for fund operations

- **Transaction Governance**
  - Implemented approval workflows for financial actions
  - Built transaction visibility controls based on permissions
  - Created audit and reporting tools for group activities

## 🏗️ Technical Implementation

### 📐 Design Patterns

- **Structural Patterns**

  - Bridge Pattern for OTP system separation of concerns
  - Proxy Pattern for controlled access to sensitive operations
  - Decorator Pattern for dynamic behavior extension

- **Behavioral Patterns**
  - Strategy Pattern for payment processing flexibility
  - Chain of Responsibility for transaction validation
  - Factory Pattern for object creation management

### 🔄 Microservice Architecture

- **Service Communication**

  - Integrated Apache Kafka for asynchronous messaging
  - Designed REST API interfaces for service interaction
  - Implemented resilient error handling and retry mechanisms

- **Data Management**
  - Used PostgreSQL for transactional data
  - Leveraged DynamoDB for high-throughput operations
  - Implemented data consistency patterns across services

### 📊 Analytics and Reporting

- **Financial Insights**

  - Developed transaction analysis and reporting tools
  - Created year/month-based financial statistics
  - Built visualization components for financial trends

- **User Behavior Tracking**
  - Implemented activity monitoring for security purposes
  - Developed usage analytics for service improvement
  - Created personalized financial insights

## 🛠️ Technologies

- **Backend Framework**: Spring Boot, Spring Security, Spring Data JPA
- **Databases**: PostgreSQL, Amazon DynamoDB
- **Authentication**: JWT, Custom Security Filters
- **Messaging**: Apache Kafka
- **Documentation**: Swagger/OpenAPI
- **External Integrations**: PayPal API, eSMS API, Pusher

## 🌟 Key Achievements

- Designed a flexible, secure financial platform with multiple payment options
- Implemented robust security measures for sensitive financial operations
- Created modular, maintainable codebase using modern design patterns
- Built scalable architecture ready for high transaction volumes
- Developed comprehensive API documentation for seamless integration

## 📈 Business Impact

- Enabled secure financial transactions with multi-factor verification
- Provided collaborative financial tools for group management
- Delivered real-time notifications across multiple channels
- Created comprehensive audit trails for regulatory compliance
- Supported international payments with proper currency handling
