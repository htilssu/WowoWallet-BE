# DDD Architecture Migration Guide

This document describes the Domain-Driven Design (DDD) architecture implementation in the WowoWallet-BE project.

## Architecture Overview

The project has been migrated from a traditional layered architecture to Domain-Driven Design following these principles:

### Package Structure

```
com.wowo.wowo/
├── domain/                    # Domain Layer - Core business logic
│   ├── shared/               # Shared kernel for common concepts
│   │   ├── AggregateRoot     # Base interface for aggregates
│   │   ├── DomainEvent       # Domain events interface
│   │   └── valueobjects/     # Shared value objects like Money
│   ├── wallet/               # Wallet bounded context
│   │   ├── entity/           # Aggregate roots (WalletAggregate)
│   │   ├── valueobjects/     # Value objects (WalletId)
│   │   ├── events/           # Domain events
│   │   ├── repository/       # Domain repository interfaces
│   │   └── service/          # Domain services
│   └── transaction/          # Transaction bounded context
│       ├── entity/           # Aggregate roots (TransactionAggregate)
│       ├── valueobjects/     # Value objects (TransactionId, TransactionType)
│       ├── events/           # Domain events
│       └── service/          # Domain services
├── application/              # Application Layer - Use cases
│   ├── wallet/               # Application services
│   └── dto/                  # Command/Query DTOs
├── infrastructure/           # Infrastructure Layer - External concerns
│   ├── wallet/               # Repository implementations
│   └── transaction/          # Repository implementations
├── presentation/             # Presentation Layer - API controllers
│   └── wallet/               # REST controllers and DTOs
└── [legacy packages]/        # Existing code (controller, service, model, etc.)
```

## Key DDD Concepts Implemented

### 1. Bounded Contexts

- **Wallet Context**: Manages digital wallets and balance operations
- **Transaction Context**: Handles financial transactions and transfers
- **Shared Kernel**: Common concepts used across contexts

### 2. Aggregates and Aggregate Roots

#### WalletAggregate
- **Purpose**: Manages wallet balance and ensures business invariants
- **Business Rules**:
  - Cannot have negative balance
  - Currency must match for operations
  - All balance changes raise domain events

#### TransactionAggregate  
- **Purpose**: Records financial transactions
- **Business Rules**:
  - Amount must be positive
  - Source and target wallets must be valid
  - Transaction completion raises domain events

### 3. Value Objects

- **Money**: Represents monetary amounts with currency validation
- **WalletId**: Type-safe wallet identifier
- **TransactionId**: Type-safe transaction identifier

### 4. Domain Events

- **MoneyDepositedEvent**: Raised when money is added to a wallet
- **MoneyWithdrawnEvent**: Raised when money is removed from a wallet
- **TransactionCompletedEvent**: Raised when a transaction is completed

### 5. Domain Services

- **MoneyTransferDomainService**: Coordinates transfers between wallets (cross-aggregate operations)

## API Endpoints

### New DDD Endpoints

#### Wallet Operations
- `POST /api/v2/wallets` - Create new wallet
- `GET /api/v2/wallets/{id}` - Get wallet by ID
- `POST /api/v2/wallets/transfer` - Transfer money between wallets
- `POST /api/v2/wallets/{id}/deposit` - Deposit money to wallet
- `POST /api/v2/wallets/{id}/withdraw` - Withdraw money from wallet

## Migration Status

✅ **Migration Complete**: The wallet and transaction domains have been successfully migrated to DDD architecture.

## Migration History

The migration has been completed through these phases:

### Phase 1: Foundation ✅
- Implement core DDD infrastructure
- Create shared kernel with common concepts
- Establish new package structure

### Phase 2: Core Domains ✅
- Implement Wallet and Transaction aggregates
- Add domain services and repository patterns
- Create application services and API endpoints

### Phase 3: Cleanup ✅
- Remove legacy wallet/transfer controllers
- Clean up migration scaffolding and facades
- Finalize DDD implementation

## Example Usage

### Creating a Wallet (DDD Way)

```java
// Through application service
WalletAggregate wallet = walletApplicationService.createWallet("VND");

// Direct domain usage
WalletAggregate wallet = new WalletAggregate("VND");
Money deposit = Money.vnd(100000L);
wallet.deposit(deposit);
```

### Money Transfer (DDD Way)

```java
TransferMoneyCommand command = new TransferMoneyCommand(
    sourceWalletId, targetWalletId, amount, "VND", 
    senderName, receiverName, message
);
TransactionAggregate transaction = walletApplicationService.transferMoney(command);
```

### Hybrid Approach (Migration Phase)

## Benefits of DDD Implementation

1. **Clear Business Logic**: Domain entities encapsulate business rules
2. **Separation of Concerns**: Clean layered architecture
3. **Testability**: Domain logic isolated from infrastructure
4. **Maintainability**: Bounded contexts reduce coupling
5. **Scalability**: Each context can evolve independently
6. **Domain Events**: Loose coupling between contexts

## Future Enhancements

For continued evolution of the DDD architecture:

1. Migrate User domain to DDD structure
2. Implement domain event publishing infrastructure  
3. Create additional bounded contexts (Payment, Notification, etc.)
4. Add comprehensive testing for domain logic
5. Consider CQRS pattern for read/write optimization

## Testing

The DDD architecture facilitates testing by:
- Isolating domain logic from infrastructure
- Enabling unit testing of business rules
- Allowing integration testing of application services
- Supporting end-to-end testing through API controllers

## Documentation

- Domain models are self-documenting through expressive naming
- Business rules are enforced at the domain level
- API documentation available through Swagger UI
- Domain events provide audit trail of business operations