package com.wowo.wowo.contexts.user.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

/**
 * JPA Repository for Role entity
 */
@Repository
interface RoleJpaRepository : JpaRepository<RoleJpaEntity, UUID> {
    
    fun findByName(name: String): RoleJpaEntity?
    
    fun existsByName(name: String): Boolean
    
    @Query("""
        SELECT r FROM RoleJpaEntity r
        JOIN UserRoleJpaEntity ur ON ur.id.roleId = r.id
        WHERE ur.id.userId = :userId
    """)
    fun findAllByUserId(userId: UUID): Set<RoleJpaEntity>
}
