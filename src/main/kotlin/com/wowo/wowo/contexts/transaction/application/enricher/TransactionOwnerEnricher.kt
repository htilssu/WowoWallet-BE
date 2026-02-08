package com.wowo.wowo.contexts.transaction.application.enricher

import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.domain.acl.WalletACL
import com.wowo.wowo.shared.enrichment.EntityEnricher
import com.wowo.wowo.shared.enrichment.OwnerInfo
import com.wowo.wowo.shared.enrichment.OwnerNameContext
import com.wowo.wowo.shared.enrichment.OwnerNameResolver
import org.springframework.stereotype.Component

/**
 * Context for transaction owner enrichment.
 * Contains both wallet -> owner info mapping and resolved owner names.
 */
data class TransactionOwnerContext(
    val walletToOwnerInfo: Map<String, OwnerInfo?>,
    val ownerNameContext: OwnerNameContext
) : com.wowo.wowo.shared.enrichment.EnrichmentContext

/**
 * Enricher for Transaction DTOs.
 * 
 * Handles the two-step enrichment process:
 * 1. Wallet ID -> Owner Info (via WalletACL)
 * 2. Owner Info -> Owner Name (via OwnerNameResolver, based on OwnerType)
 * 
 * Supports sender (from) and receiver (to) owner names.
 */
@Component
class TransactionOwnerEnricher(
    private val walletACL: WalletACL,
    private val ownerNameResolver: OwnerNameResolver
) : EntityEnricher<TransactionDTO, TransactionOwnerContext> {
    
    /**
     * Enrich transactions with owner information.
     * This is a two-phase enrichment:
     * 1. Collect wallet IDs -> fetch owner info from WalletACL
     * 2. Collect owner IDs by type -> resolve names from respective sources
     */
    override fun enrich(entities: List<TransactionDTO>, context: TransactionOwnerContext): List<TransactionDTO> {
        return entities.map { dto ->
            val fromOwnerInfo = dto.fromWalletId?.let { context.walletToOwnerInfo[it] }
            val toOwnerInfo = dto.toWalletId?.let { context.walletToOwnerInfo[it] }
            
            dto.copy(
                fromOwnerId = fromOwnerInfo?.ownerId,
                fromOwnerType = fromOwnerInfo?.ownerType,
                fromOwnerName = context.ownerNameContext.getOwnerName(
                    fromOwnerInfo?.ownerId, 
                    fromOwnerInfo?.ownerType
                ),
                toOwnerId = toOwnerInfo?.ownerId,
                toOwnerType = toOwnerInfo?.ownerType,
                toOwnerName = context.ownerNameContext.getOwnerName(
                    toOwnerInfo?.ownerId, 
                    toOwnerInfo?.ownerType
                )
            )
        }
    }
    
    /**
     * Build enrichment context from a list of transactions.
     * Performs batch lookups for optimal performance.
     */
    fun buildContext(dtos: List<TransactionDTO>): TransactionOwnerContext {
        // Step 1: Collect all wallet IDs
        val walletIds = dtos.flatMap { listOfNotNull(it.fromWalletId, it.toWalletId) }.toSet()
        
        if (walletIds.isEmpty()) {
            return TransactionOwnerContext(
                walletToOwnerInfo = emptyMap(),
                ownerNameContext = OwnerNameContext.empty()
            )
        }
        
        // Step 2: Fetch owner info from wallets
        val walletToOwnerInfo = walletACL.getWalletOwnerInfos(walletIds)
        
        // Step 3: Collect owner infos and resolve names by type
        val ownerInfos = walletToOwnerInfo.values.filterNotNull()
        val ownerNameContext = ownerNameResolver.resolveNames(ownerInfos)
        
        return TransactionOwnerContext(
            walletToOwnerInfo = walletToOwnerInfo,
            ownerNameContext = ownerNameContext
        )
    }
    
    /**
     * Convenience method: build context and enrich in one call.
     */
    fun enrichWithLookup(dtos: List<TransactionDTO>): List<TransactionDTO> {
        val context = buildContext(dtos)
        return enrich(dtos, context)
    }
}
