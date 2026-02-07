package com.wowo.wowo.contexts.transaction.domain.acl

interface UserACL {
    fun getUserName(userId: String): String?

    /**
     * Get usernames for multiple users
     * @return Map of userId -> username (username is null if user not found)
     */
    fun getUserNames(userIds: Set<String>): Map<String, String?>
}

