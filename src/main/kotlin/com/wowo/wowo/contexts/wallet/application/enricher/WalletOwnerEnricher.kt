package com.wowo.wowo.contexts.wallet.application.enricher

import com.wowo.wowo.contexts.wallet.application.dto.WalletDTO
import com.wowo.wowo.shared.enrichment.OwnerNameContext
import com.wowo.wowo.shared.enrichment.OwnerNameResolver
import com.wowo.wowo.shared.enrichment.SingleOwnerEnricher
import org.springframework.stereotype.Component
import com.wowo.wowo.shared.domain.OwnerType

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
        entity.ownerType
    
    override fun applyOwnerName(entity: WalletDTO, ownerName: String?): WalletDTO {
        return entity.copy(ownerName = ownerName)
    }
    
}
