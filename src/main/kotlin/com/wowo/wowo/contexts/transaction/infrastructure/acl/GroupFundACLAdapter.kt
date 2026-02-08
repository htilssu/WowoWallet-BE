package com.wowo.wowo.contexts.transaction.infrastructure.acl

import com.wowo.wowo.contexts.groupfund.domain.GroupId
import com.wowo.wowo.contexts.groupfund.domain.repository.GroupFundRepository
import com.wowo.wowo.contexts.transaction.domain.acl.GroupFundACL
import org.springframework.stereotype.Component

@Component
class GroupFundACLAdapter(
    private val groupFundRepository: GroupFundRepository
) : GroupFundACL {
    override fun canAccessFundWallet(groupFundId: String, userId: String): Boolean {
        val groupId = GroupId.fromString(groupFundId)
        val groupFund = groupFundRepository.findById(groupId) ?: return false
        if (groupFund.ownerId == userId) {
            return true
        }
        return groupFundRepository.existsMember(groupId, userId)
    }
}
