package com.wowo.wowo.contexts.groupfund.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface GroupFundJpaRepository : JpaRepository<GroupFundJpaEntity, UUID>
