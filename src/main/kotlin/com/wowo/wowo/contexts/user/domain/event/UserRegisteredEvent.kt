package com.wowo.wowo.contexts.user.domain.event

import com.wowo.wowo.shared.domain.BaseDomainEvent

/**
 * Domain event fired when a user is registered
 */
class UserRegisteredEvent(
    aggregateId: String,
    val username: String,
    val email: String
) : BaseDomainEvent(aggregateId)

