package com.wowo.wowo.contexts.user.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

/**
 * JPA Repository for Permission entity
 */
@Repository
interface PermissionJpaRepository : JpaRepository<PermissionJpaEntity, UUID> {
    
    fun findByName(name: String): PermissionJpaEntity?
    
    fun findByResource(resource: String): List<PermissionJpaEntity>
    
    fun findByResourceAndAction(resource: String, action: String): PermissionJpaEntity?
    
    fun existsByName(name: String): Boolean
    
    @Query("""
        SELECT DISTINCT p FROM PermissionJpaEntity p
        LEFT JOIN p.roles r
        LEFT JOIN UserRoleJpaEntity ur ON ur.id.roleId = r.id
        LEFT JOIN UserPermissionJpaEntity up ON up.id.permissionId = p.id
        WHERE ur.id.userId = :userId OR up.id.userId = :userId
    """)
    fun findAllByUserId(userId: UUID): Set<PermissionJpaEntity>
}
