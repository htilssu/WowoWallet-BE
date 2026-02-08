package com.wowo.wowo.contexts.groupfund.domain

import com.wowo.wowo.shared.domain.*

class GroupFund(
    override val id: GroupId,
    val name: String,
    val description: String?,
    val ownerId: String,
    val members: List<FundMember>
) : AggregateRoot<GroupId>() {

    fun addMember(newMember: FundMember) {
        if (members.any { it.id == newMember.id }) {
            throw IllegalArgumentException("Member with ID ${newMember.id} already exists in the fund.")
        }
        members.toMutableList().apply { add(newMember) } //TODO: publish domain event for member addition
    }

    fun removeMember(memberId: String) { // Check if the member exists
        if (members.none { it.id == memberId }) {
            throw IllegalArgumentException("Member with ID $memberId does not exist in the fund.")
        }
        val removedMember = members.first { it.id == memberId }
        val updatedMembers = members.filterNot {
            it.id == memberId
        } // Update the state of the aggregate //TODO: publish domain event for member removal
    }
}

class FundMember(
    val id: String, val name: String, val avatarUrl: String?, val role: String, val status: String
) {}
