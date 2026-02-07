# Enrichment Pattern Documentation

## Overview

The Enrichment Pattern is used to efficiently enrich entities with additional data from external sources (like owner names, related entity details, etc.) using batch operations to optimize performance.

## Architecture

### Core Components

1. **EntityEnricher** - Interface defining enrichment strategy for entities
2. **EnrichmentContext** - Base interface for enrichment data contexts
3. **EnrichmentService** - Service managing batch lookups and applying enrichment
4. **OwnerNameEnricher** - Abstract base class for owner name enrichment

### Key Benefits

- **Batch Operations**: Reduces database queries by loading data in batches
- **Reusable**: Easy to add enrichment for new entity types
- **Extensible**: Supports single or multiple enrichment steps
- **Type-Safe**: Compile-time type checking for context and entities

## Usage Examples

### 1. Simple Enrichment (Single Lookup)

Enrich transactions with wallet owner names:

```kotlin
@Service
class GetTransactionHistoryUseCase(
    private val enrichmentService: EnrichmentService,
    private val transactionOwnerEnricher: TransactionOwnerEnricher
) {
    fun execute(): List<TransactionDTO> {
        val dtos = getTransactions()
        val walletIds = extractWalletIds(dtos)

        return enrichmentService.enrich(
            entities = dtos,
            ids = walletIds,
            lookup = { ids -> getWalletOwnerNames(ids) },
            enricher = transactionOwnerEnricher
        )
    }
}
```

### 2. Multi-Step Enrichment (Composite Context)

Enrich entities with data from multiple sources:

```kotlin
@Service
class GetOrdersUseCase(
    private val enrichmentService: EnrichmentService,
    private val orderEnricher: OrderEnricher
) {
    fun execute(): List<OrderDTO> {
        val dtos = getOrders()

        return enrichmentService.enrichMulti(
            entities = dtos,
            lookups = listOf(
                { getOwnerNames() },      // Owner names
                { getProductNames() },    // Product names
                { getShippingInfo() }     // Shipping info
            ),
            enricher = orderEnricher
        )
    }
}
```

### 3. Creating a New Enricher

For entities with ownerId (e.g., Orders, Invoices):

```kotlin
@Component
class OrderOwnerEnricher(
    private val orderRepository: OrderRepository
) : OwnerNameEnricher<OrderDTO>(
    ownerIdExtractor = { it.orderId }
) {
    override fun enrichEntity(entity: OrderDTO, context: IdNameContext): OrderDTO {
        return entity.copy(
            ownerName = getOwnerName(entity, context)
        )
    }
}
```

For custom enrichment logic:

```kotlin
@Component
class ProductEnricher : EntityEnricher<ProductDTO, ProductEnrichmentContext> {

    override fun enrich(entities: List<ProductDTO>, context: ProductEnrichmentContext): List<ProductDTO> {
        return entities.map { dto ->
            dto.copy(
                category = context.categories[dto.categoryId],
                manufacturer = context.manufacturers[dto.manufacturerId]
            )
        }
    }
}
```

## Adding Enrichment for New Entities

### Step 1: Create Enrichment Context

```kotlin
data class CustomEnrichmentContext(
    val someData: Map<String, String?>
) : EnrichmentContext
```

### Step 2: Implement Enricher

```kotlin
@Component
class CustomEnricher : EntityEnricher<CustomDTO, CustomEnrichmentContext> {

    override fun enrich(entities: List<CustomDTO>, context: CustomEnrichmentContext): List<CustomDTO> {
        return entities.map { dto ->
            dto.copy(
                enrichedField = context.someData[dto.id]
            )
        }
    }
}
```

### Step 3: Use in Use Case

```kotlin
@Service
class GetCustomUseCase(
    private val enrichmentService: EnrichmentService,
    private val customEnricher: CustomEnricher
) {
    fun execute(): List<CustomDTO> {
        val dtos = getCustomEntities()
        val ids = dtos.map { it.id }.toSet()

        return enrichmentService.enrich(
            entities = dtos,
            ids = ids,
            lookup = { ids -> CustomEnrichmentContext(loadSomeData(ids)) },
            enricher = customEnricher
        )
    }
}
```

## Pattern Variations

### 1. Owner-Name Enrichment

For any entity that has an `ownerId` field and needs owner username:

```kotlin
// Extend OwnerNameEnricher
class EntityOwnerEnricher : OwnerNameEnricher<EntityDTO>(
    ownerIdExtractor = { it.ownerId }
) {
    override fun enrichEntity(entity: EntityDTO, context: IdNameContext): EntityDTO {
        return entity.copy(
            ownerName = getOwnerName(entity, context)
        )
    }
}
```

### 2. Multi-Field Enrichment

For entities that need enrichment of multiple fields:

```kotlin
@Component
class EntityEnricher : EntityEnricher<EntityDTO, CompositeEnrichmentContext> {

    override fun enrich(entities: List<EntityDTO>, context: CompositeEnrichmentContext): List<EntityDTO> {
        val ownerContext = context.getContext(IdNameContext::class.java)
        val categoryContext = context.getContext(CategoryContext::class.java)

        return entities.map { dto ->
            dto.copy(
                ownerName = ownerContext?.idNames?.get(dto.ownerId),
                categoryName = categoryContext?.categoryNames?.get(dto.categoryId)
            )
        }
    }
}
```

## Performance Considerations

1. **Batch Size**: Default implementation processes all IDs in one batch. Consider pagination for very large datasets.

2. **Caching**: For frequently accessed data, consider adding caching at the ACL or service level.

3. **Lazy Loading**: For optional enrichment, consider lazy loading only when needed.

4. **Selective Enrichment**: Only enrich fields that are actually needed in the response.

## Future Extensions

### 1. Cache Support

```kotlin
interface CacheableEnrichment {
    fun getCacheKey(): String
    fun shouldCache(): Boolean
}
```

### 2. Async Enrichment

```kotlin
suspend fun <T, C : EnrichmentContext> enrichAsync(
    entities: List<T>,
    ids: Set<String>,
    lookup: suspend (Set<String>) -> C,
    enricher: EntityEnricher<T, C>
): List<T>
```

### 3. Validation Support

```kotlin
interface ValidationEnrichment {
    fun validate(context: EnrichmentContext): ValidationResult
}
```

## Testing

Test enrichers independently:

```kotlin
@Test
fun `should enrich transaction with owner names`() {
    val enricher = TransactionOwnerEnricher()
    val context = IdNameContext(
        mapOf(
            "wallet1" to "John Doe",
            "wallet2" to "Jane Smith"
        )
    )

    val dtos = listOf(
        TransactionDTO(id = "1", fromWalletId = "wallet1", toWalletId = "wallet2", ...),
        TransactionDTO(id = "2", fromWalletId = "wallet2", toWalletId = "wallet1", ...)
    )

    val enriched = enricher.enrich(dtos, context)

    assertEquals("John Doe", enriched[0].fromWalletName)
    assertEquals("Jane Smith", enriched[0].toWalletName)
}
```
