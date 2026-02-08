package com.wowo.wowo.contexts.transaction.domain.acl

interface GroupFundACL {
    fun canAccessFundWallet(groupFundId: String, userId: String): Boolean
}
