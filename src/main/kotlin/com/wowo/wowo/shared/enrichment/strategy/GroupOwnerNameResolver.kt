package com.wowo.wowo.shared.enrichment.strategy

import com.wowo.wowo.shared.domain.OwnerType
import com.wowo.wowo.shared.enrichment.OwnerNameResolverStrategy
import org.springframework.stereotype.Component

/**
 * Resolves owner names for GROUP type.
 * 
 * TODO: Inject GroupRepository when Group entity is implemented.
 */
@Component
class GroupOwnerNameResolver : OwnerNameResolverStrategy {
    
    override val supportedType: OwnerType = OwnerType.GROUP
    
    override fun resolveNames(ownerIds: Set<String>): Map<String, String?> {
        // TODO: Implement when GroupRepository is available
        // return groupRepository.findByIds(ownerIds.map { GroupId.fromString(it) })
        //     .associate { it.id.value to it.name.value }
        //     .let { resolved -> ownerIds.associateWith { resolved[it] } }
        
        return ownerIds.associateWith { null }
    }
}
