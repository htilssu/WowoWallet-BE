package com.wowo.wowo.contexts.groupfund.infrastructure.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(
    name = "group_fund_members",
    uniqueConstraints = [UniqueConstraint(columnNames = ["group_fund_id", "member_id"])]
)
class GroupFundMemberJpaEntity(
    @Id
    var id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_fund_id", nullable = false)
    var groupFund: GroupFundJpaEntity,

    @Column(nullable = false, length = 255)
    var memberId: String,

    @Column(nullable = false, length = 255)
    var memberName: String,

    @Column(length = 500)
    var avatarUrl: String? = null,

    @Column(nullable = false, length = 50)
    var role: String,

    @Column(nullable = false, length = 50)
    var status: String,

    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
