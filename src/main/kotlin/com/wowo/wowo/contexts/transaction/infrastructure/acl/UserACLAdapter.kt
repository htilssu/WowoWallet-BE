package com.wowo.wowo.contexts.transaction.infrastructure.acl

import com.wowo.wowo.contexts.transaction.domain.acl.UserACL
import com.wowo.wowo.contexts.user.domain.repository.UserRepository
import com.wowo.wowo.contexts.user.domain.valueobject.UserId
import org.springframework.stereotype.Component

@Component
class UserACLAdapter(
    private val userRepository: UserRepository
) : UserACL {
    override fun getUserName(userId: String): String? {
        val user = userRepository.findById(UserId.fromString(userId))
        return user?.username?.value
    }
}
