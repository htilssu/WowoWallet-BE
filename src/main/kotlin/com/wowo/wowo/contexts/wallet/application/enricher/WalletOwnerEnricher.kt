package com.wowo.wowo.contexts.wallet.application.enricher

import com.wowo.wowo.contexts.wallet.application.dto.WalletDTO
import com.wowo.wowo.shared.domain.OwnerType
import com.wowo.wowo.shared.enrichment.OwnerNameContext
import com.wowo.wowo.shared.enrichment.OwnerNameResolver
import com.wowo.wowo.shared.enrichment.SingleOwnerEnricher
import org.springframework.stereotype.Component
import com.wowo.wowo.contexts.wallet.domain.valueobject.OwnerType as WalletOwnerType

/**
 * Enricher for Wallet DTOs.
 * 
 * Resolves the ownerName based on ownerId and ownerType.
 * Demonstrates usage of SingleOwnerEnricher for entities with one owner.
 */
@Component
class WalletOwnerEnricher(
    ownerNameResolver: OwnerNameResolver
) : SingleOwnerEnricher<WalletDTO>(ownerNameResolver) {
    
    override fun extractOwnerId(entity: WalletDTO): String = entity.ownerId
    
    override fun extractOwnerType(entity: WalletDTO): OwnerType = 
        entity.ownerType.toSharedOwnerType()
    
    override fun applyOwnerName(entity: WalletDTO, ownerName: String?): WalletDTO {
        return entity.copy(ownerName = ownerName)
    }
    
    /**
     * Convert wallet's OwnerType to shared domain OwnerType.
     */
    private fun WalletOwnerType.toSharedOwnerType(): OwnerType = when (this) {
        WalletOwnerType.USER -> OwnerType.USER
        WalletOwnerType.GROUP -> OwnerType.GROUP
        WalletOwnerType.FUND_GROUP -> OwnerType.FUND_GROUP
    }
}
