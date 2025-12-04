package com.wowo.wowo.contexts.user.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

/**
 * JPA Repository for UserPermission entity
 */
@Repository
interface UserPermissionJpaRepository : JpaRepository<UserPermissionJpaEntity, UserPermissionId> {
    
    @Query("SELECT up FROM UserPermissionJpaEntity up WHERE up.id.userId = :userId")
    fun findByUserId(userId: UUID): List<UserPermissionJpaEntity>
    
    @Query("SELECT up FROM UserPermissionJpaEntity up WHERE up.id.permissionId = :permissionId")
    fun findByPermissionId(permissionId: UUID): List<UserPermissionJpaEntity>
    
    @Modifying
    @Query("DELETE FROM UserPermissionJpaEntity up WHERE up.id.userId = :userId")
    fun deleteByUserId(userId: UUID)
    
    @Query("SELECT CASE WHEN COUNT(up) > 0 THEN true ELSE false END FROM UserPermissionJpaEntity up WHERE up.id.userId = :userId AND up.id.permissionId = :permissionId")
    fun existsByUserIdAndPermissionId(userId: UUID, permissionId: UUID): Boolean
}
