package com.wowo.wowo.contexts.groupfund.domain

import java.util.*

class GroupId(fromString: UUID) {
    val value: UUID = fromString
    override fun toString(): String {
        return value.toString()
    }

    companion object {
        fun fromString(id: String): GroupId = GroupId(UUID.fromString(id))
    }
}
