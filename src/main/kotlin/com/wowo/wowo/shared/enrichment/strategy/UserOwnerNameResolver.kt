package com.wowo.wowo.shared.enrichment.strategy

import com.wowo.wowo.contexts.user.domain.repository.UserRepository
import com.wowo.wowo.contexts.user.domain.valueobject.UserId
import com.wowo.wowo.shared.domain.OwnerType
import com.wowo.wowo.shared.enrichment.OwnerNameResolverStrategy
import org.springframework.stereotype.Component

/**
 * Resolves owner names for USER type by looking up usernames.
 */
@Component
class UserOwnerNameResolver(
    private val userRepository: UserRepository
) : OwnerNameResolverStrategy {
    
    override val supportedType: OwnerType = OwnerType.USER
    
    override fun resolveNames(ownerIds: Set<String>): Map<String, String?> {
        return userRepository.findByIds(ownerIds.map { UserId.fromString(it) })
            .associate { it.id.value to it.username.value }
            .let { resolved ->
                // Ensure all requested IDs are in the result (with null for not found)
                ownerIds.associateWith { resolved[it] }
            }
    }
}
