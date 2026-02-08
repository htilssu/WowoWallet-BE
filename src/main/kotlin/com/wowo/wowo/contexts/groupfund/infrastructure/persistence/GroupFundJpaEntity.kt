package com.wowo.wowo.contexts.groupfund.infrastructure.persistence

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "group_funds")
class GroupFundJpaEntity(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false, length = 255)
    var name: String,

    @Column(length = 500)
    var description: String? = null,

    @Column(nullable = false, length = 255)
    var ownerId: String,

    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(
        mappedBy = "groupFund",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    var members: MutableSet<GroupFundMemberJpaEntity> = mutableSetOf()
)
