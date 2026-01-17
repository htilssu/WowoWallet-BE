package com.wowo.wowo.contexts.transaction.domain.acl

interface UserACL {
    fun getUserName(userId: String): String?
}
