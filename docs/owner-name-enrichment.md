# Owner Name Enrichment Pattern

## Overview

The Owner Name Enrichment pattern provides a flexible, type-safe way to resolve and enrich owner names across different entity types (User, Group, FundGroup) in batch operations.

## Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           ENRICHMENT ARCHITECTURE                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌─────────────┐     ┌──────────────────┐     ┌──────────────────────────┐  │
│  │   UseCase   │────▶│    Enricher      │────▶│   OwnerNameResolver      │  │
│  └─────────────┘     └──────────────────┘     └──────────────────────────┘  │
│                               │                           │                  │
│                               ▼                           ▼                  │
│                    ┌──────────────────┐      ┌────────────────────────────┐ │
│                    │  OwnerNameContext │     │ OwnerNameResolverStrategy  │ │
│                    └──────────────────┘     └────────────────────────────┘ │
│                                                          │                  │
│                            ┌─────────────────────────────┼──────────────┐   │
│                            ▼                             ▼              ▼   │
│                    ┌───────────────┐      ┌───────────────┐  ┌────────────┐│
│                    │UserOwnerName  │      │GroupOwnerName │  │FundGroup   ││
│                    │Resolver       │      │Resolver       │  │NameResolver││
│                    └───────────────┘      └───────────────┘  └────────────┘│
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

## Core Components

### 1. EnrichmentContext
Base interface for all enrichment contexts.

```kotlin
interface EnrichmentContext
```

### 2. EntityEnricher<T, C>
Interface for enriching entities with additional data.

```kotlin
interface EntityEnricher<T, C : EnrichmentContext> {
    fun enrich(entities: List<T>, context: C): List<T>
    fun enrichSingle(entity: T, context: C): T
}
```

### 3. OwnerInfo
Value object representing owner information.

```kotlin
data class OwnerInfo(
    val ownerId: String,
    val ownerType: OwnerType,
    val ownerName: String? = null
)
```

### 4. OwnerNameContext
Context for owner name resolution with multi-type support.

```kotlin
data class OwnerNameContext(
    private val ownerInfoMap: Map<OwnerKey, OwnerInfo>
) : EnrichmentContext {
    fun getOwnerName(ownerId: String?, ownerType: OwnerType?): String?
    fun getOwnerInfo(ownerId: String?, ownerType: OwnerType?): OwnerInfo?
}
```

### 5. OwnerNameResolver
Coordinates owner name resolution across different owner types.

```kotlin
@Component
class OwnerNameResolver(strategies: List<OwnerNameResolverStrategy>) {
    fun resolveNames(ownersByType: Map<OwnerType, Set<String>>): OwnerNameContext
    fun resolveNames(ownerInfos: Collection<OwnerInfo>): OwnerNameContext
}
```

### 6. OwnerNameResolverStrategy
Strategy interface for type-specific name resolution.

```kotlin
interface OwnerNameResolverStrategy {
    val supportedType: OwnerType
    fun resolveNames(ownerIds: Set<String>): Map<String, String?>
}
```

## Usage Examples

### 1. Transaction Enrichment (Multi-Owner)

For transactions with sender and receiver:

```kotlin
@Component
class TransactionOwnerEnricher(
    private val walletACL: WalletACL,
    private val ownerNameResolver: OwnerNameResolver
) : EntityEnricher<TransactionDTO, TransactionOwnerContext> {
    
    fun enrichWithLookup(dtos: List<TransactionDTO>): List<TransactionDTO> {
        val context = buildContext(dtos)
        return enrich(dtos, context)
    }
}

// Usage in UseCase
@Service
class GetTransactionHistoryUseCase(
    private val transactionOwnerEnricher: TransactionOwnerEnricher
) {
    fun execute(criteria: TransactionSearchCriteria): PagedResult<TransactionDTO> {
        val dtos = transactionMapper.toDTOs(transactions)
        val enrichedDtos = transactionOwnerEnricher.enrichWithLookup(dtos)
        return PagedResult(items = enrichedDtos, ...)
    }
}
```

### 2. Wallet Enrichment (Single-Owner)

For entities with a single owner:

```kotlin
@Component
class WalletOwnerEnricher(
    ownerNameResolver: OwnerNameResolver
) : SingleOwnerEnricher<WalletDTO>(ownerNameResolver) {
    
    override fun extractOwnerId(entity: WalletDTO): String = entity.ownerId
    override fun extractOwnerType(entity: WalletDTO): OwnerType = entity.ownerType
    override fun applyOwnerName(entity: WalletDTO, ownerName: String?): WalletDTO {
        return entity.copy(ownerName = ownerName)
    }
}
```

### 3. Adding a New Owner Type

To add support for a new owner type (e.g., ORGANIZATION):

1. Add to OwnerType enum:
```kotlin
enum class OwnerType {
    USER, GROUP, FUND_GROUP, ORGANIZATION
}
```

2. Create resolver strategy:
```kotlin
@Component
class OrganizationOwnerNameResolver(
    private val organizationRepository: OrganizationRepository
) : OwnerNameResolverStrategy {
    
    override val supportedType = OwnerType.ORGANIZATION
    
    override fun resolveNames(ownerIds: Set<String>): Map<String, String?> {
        return organizationRepository.findByIds(ownerIds)
            .associate { it.id to it.name }
    }
}
```

That's it! The `OwnerNameResolver` will automatically pick up the new strategy.

## Transaction DTO Structure

```kotlin
data class TransactionDTO(
    val id: String,
    
    // Sender (from) wallet information
    val fromWalletId: String?,
    val fromOwnerId: String?,
    val fromOwnerType: OwnerType?,
    val fromOwnerName: String?,    // Enriched
    
    // Receiver (to) wallet information  
    val toWalletId: String?,
    val toOwnerId: String?,
    val toOwnerType: OwnerType?,
    val toOwnerName: String?,      // Enriched
    
    // Transaction details
    val amount: BigDecimal,
    val currency: String,
    ...
)
```

## Enrichment Flow for Transactions

```
1. UseCase receives list of Transaction entities
         │
         ▼
2. TransactionMapper converts to DTOs (unenriched)
         │
         ▼
3. TransactionOwnerEnricher.buildContext()
         │
         ├── Extract wallet IDs (from + to)
         │
         ├── WalletACL.getWalletOwnerInfos() 
         │   └── Returns Map<WalletId, OwnerInfo(ownerId, ownerType)>
         │
         ├── Group owner IDs by type
         │
         └── OwnerNameResolver.resolveNames()
             ├── UserOwnerNameResolver.resolveNames()
             ├── GroupOwnerNameResolver.resolveNames()
             └── FundGroupOwnerNameResolver.resolveNames()
         │
         ▼
4. TransactionOwnerEnricher.enrich()
         │
         ├── For each DTO:
         │   ├── Look up fromOwnerInfo from context
         │   ├── Look up toOwnerInfo from context
         │   └── Copy with resolved names
         │
         ▼
5. Return enriched DTOs
```

## Base Classes for Common Patterns

### SingleOwnerEnricher
For entities with one owner field:

```kotlin
abstract class SingleOwnerEnricher<T>(
    private val ownerNameResolver: OwnerNameResolver
) : EntityEnricher<T, OwnerNameContext> {
    
    abstract fun extractOwnerId(entity: T): String?
    abstract fun extractOwnerType(entity: T): OwnerType?
    abstract fun applyOwnerName(entity: T, ownerName: String?): T
    
    fun enrichWithLookup(entities: List<T>): List<T>
}
```

### MultiOwnerEnricher
For entities with multiple owner fields:

```kotlin
abstract class MultiOwnerEnricher<T>(
    private val ownerNameResolver: OwnerNameResolver
) : EntityEnricher<T, OwnerNameContext> {
    
    abstract fun extractOwnerReferences(entity: T): List<OwnerReference>
    abstract fun applyOwnerNames(entity: T, resolvedNames: Map<String, String?>): T
    
    fun enrichWithLookup(entities: List<T>): List<T>
}
```

## File Locations

| Component | Path |
|-----------|------|
| EnrichmentContext | `shared/enrichment/EnrichmentContext.kt` |
| EntityEnricher | `shared/enrichment/EntityEnricher.kt` |
| OwnerInfo | `shared/enrichment/OwnerInfo.kt` |
| OwnerNameContext | `shared/enrichment/OwnerNameContext.kt` |
| OwnerNameResolver | `shared/enrichment/OwnerNameResolver.kt` |
| OwnerNameResolverStrategy | `shared/enrichment/OwnerNameResolverStrategy.kt` |
| SingleOwnerEnricher | `shared/enrichment/SingleOwnerEnricher.kt` |
| MultiOwnerEnricher | `shared/enrichment/MultiOwnerEnricher.kt` |
| UserOwnerNameResolver | `shared/enrichment/strategy/UserOwnerNameResolver.kt` |
| GroupOwnerNameResolver | `shared/enrichment/strategy/GroupOwnerNameResolver.kt` |
| FundGroupOwnerNameResolver | `shared/enrichment/strategy/FundGroupOwnerNameResolver.kt` |
| TransactionOwnerEnricher | `contexts/transaction/application/enricher/TransactionOwnerEnricher.kt` |
| WalletOwnerEnricher | `contexts/wallet/application/enricher/WalletOwnerEnricher.kt` |

## Performance Considerations

1. **Batch Operations**: All lookups are done in batch to minimize database queries
2. **Type Grouping**: Owner IDs are grouped by type for efficient per-type queries
3. **Single Pass**: Enrichment is done in a single pass over the entity list
4. **Lazy Resolution**: Names are only resolved for IDs that exist in the entities

## Testing

```kotlin
@Test
fun `should enrich transaction with sender and receiver names`() {
    val context = TransactionOwnerContext(
        walletToOwnerInfo = mapOf(
            "wallet1" to OwnerInfo("user1", OwnerType.USER),
            "wallet2" to OwnerInfo("group1", OwnerType.GROUP)
        ),
        ownerNameContext = OwnerNameContext.fromGroupedNames(
            mapOf(
                OwnerType.USER to mapOf("user1" to "Alice"),
                OwnerType.GROUP to mapOf("group1" to "Team Alpha")
            )
        )
    )
    
    val dto = TransactionDTO.unenriched(
        id = "tx1",
        fromWalletId = "wallet1",
        toWalletId = "wallet2",
        ...
    )
    
    val enriched = enricher.enrich(listOf(dto), context).first()
    
    assertEquals("Alice", enriched.fromOwnerName)
    assertEquals(OwnerType.USER, enriched.fromOwnerType)
    assertEquals("Team Alpha", enriched.toOwnerName)
    assertEquals(OwnerType.GROUP, enriched.toOwnerType)
}
```
