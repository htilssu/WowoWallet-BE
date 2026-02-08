package com.wowo.wowo.contexts.groupfund.domain.repository

import com.wowo.wowo.contexts.groupfund.domain.GroupFund
import com.wowo.wowo.contexts.groupfund.domain.GroupId

interface GroupFundRepository {
    fun findById(id: GroupId): GroupFund?

    fun existsMember(groupId: GroupId, memberId: String): Boolean
}
