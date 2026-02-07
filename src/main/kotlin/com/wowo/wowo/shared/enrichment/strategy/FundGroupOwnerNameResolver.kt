package com.wowo.wowo.shared.enrichment.strategy

import com.wowo.wowo.shared.domain.OwnerType
import com.wowo.wowo.shared.enrichment.OwnerNameResolverStrategy
import org.springframework.stereotype.Component

/**
 * Resolves owner names for FUND_GROUP type.
 * 
 * TODO: Inject FundGroupRepository when FundGroup entity is implemented.
 */
@Component
class FundGroupOwnerNameResolver : OwnerNameResolverStrategy {
    
    override val supportedType: OwnerType = OwnerType.FUND_GROUP
    
    override fun resolveNames(ownerIds: Set<String>): Map<String, String?> {
        // TODO: Implement when FundGroupRepository is available
        // return fundGroupRepository.findByIds(ownerIds.map { FundGroupId.fromString(it) })
        //     .associate { it.id.value to it.name.value }
        //     .let { resolved -> ownerIds.associateWith { resolved[it] } }
        
        return ownerIds.associateWith { null }
    }
}
