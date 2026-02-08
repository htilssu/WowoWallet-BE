package com.wowo.wowo.contexts.groupfund.infrastructure.persistence

import com.wowo.wowo.contexts.groupfund.domain.FundMember
import com.wowo.wowo.contexts.groupfund.domain.GroupFund
import com.wowo.wowo.contexts.groupfund.domain.GroupId
import com.wowo.wowo.contexts.groupfund.domain.repository.GroupFundRepository
import org.springframework.stereotype.Repository

@Repository
class GroupFundRepositoryAdapter(
    private val groupFundJpaRepository: GroupFundJpaRepository,
    private val groupFundMemberJpaRepository: GroupFundMemberJpaRepository
) : GroupFundRepository {
    override fun findById(id: GroupId): GroupFund? {
        return groupFundJpaRepository.findById(id.value)
            .map { toDomainEntity(it) }
            .orElse(null)
    }

    override fun existsMember(groupId: GroupId, memberId: String): Boolean {
        return groupFundMemberJpaRepository.existsByGroupFund_IdAndMemberId(groupId.value, memberId)
    }

    private fun toDomainEntity(jpaEntity: GroupFundJpaEntity): GroupFund {
        return GroupFund(
            id = GroupId(jpaEntity.id),
            name = jpaEntity.name,
            description = jpaEntity.description,
            ownerId = jpaEntity.ownerId,
            members = jpaEntity.members.map { member ->
                FundMember(
                    id = member.memberId,
                    name = member.memberName,
                    avatarUrl = member.avatarUrl,
                    role = member.role,
                    status = member.status
                )
            }
        )
    }
}
