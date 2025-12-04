package com.wowo.wowo.contexts.user.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

/**
 * JPA Repository for UserRole entity
 */
@Repository
interface UserRoleJpaRepository : JpaRepository<UserRoleJpaEntity, UserRoleId> {
    
    @Query("SELECT ur FROM UserRoleJpaEntity ur WHERE ur.id.userId = :userId")
    fun findByUserId(userId: UUID): List<UserRoleJpaEntity>
    
    @Query("SELECT ur FROM UserRoleJpaEntity ur WHERE ur.id.roleId = :roleId")
    fun findByRoleId(roleId: UUID): List<UserRoleJpaEntity>
    
    @Modifying
    @Query("DELETE FROM UserRoleJpaEntity ur WHERE ur.id.userId = :userId")
    fun deleteByUserId(userId: UUID)
    
    @Query("SELECT CASE WHEN COUNT(ur) > 0 THEN true ELSE false END FROM UserRoleJpaEntity ur WHERE ur.id.userId = :userId AND ur.id.roleId = :roleId")
    fun existsByUserIdAndRoleId(userId: UUID, roleId: UUID): Boolean
}
