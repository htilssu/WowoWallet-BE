package com.wowo.wowo.contexts.user.infrastructure.persistence

import com.wowo.wowo.contexts.user.domain.repository.UserAuthorizationRepository
import com.wowo.wowo.contexts.user.domain.valueobject.PermissionId
import com.wowo.wowo.contexts.user.domain.valueobject.RoleId
import com.wowo.wowo.contexts.user.domain.valueobject.UserId
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * JPA implementation of UserAuthorizationRepository
 */
@Repository
@Transactional
class UserAuthorizationRepositoryAdapter(
    private val userRoleJpaRepository: UserRoleJpaRepository,
    private val userPermissionJpaRepository: UserPermissionJpaRepository
) : UserAuthorizationRepository {

    // User-Role operations

    override fun assignRoleToUser(userId: UserId, roleId: RoleId) {
        if (!userRoleJpaRepository.existsByUserIdAndRoleId(userId.value, roleId.value)) {
            userRoleJpaRepository.save(UserRoleJpaEntity(userId.value, roleId.value))
        }
    }

    override fun removeRoleFromUser(userId: UserId, roleId: RoleId) {
        userRoleJpaRepository.deleteById(UserRoleId(userId.value, roleId.value))
    }

    override fun getRoleIdsByUserId(userId: UserId): Set<RoleId> {
        return userRoleJpaRepository.findByUserId(userId.value)
            .map { RoleId(it.roleId) }
            .toSet()
    }

    override fun userHasRole(userId: UserId, roleId: RoleId): Boolean {
        return userRoleJpaRepository.existsByUserIdAndRoleId(userId.value, roleId.value)
    }

    override fun removeAllRolesFromUser(userId: UserId) {
        userRoleJpaRepository.deleteByUserId(userId.value)
    }

    // User-Permission operations

    override fun assignPermissionToUser(userId: UserId, permissionId: PermissionId) {
        if (!userPermissionJpaRepository.existsByUserIdAndPermissionId(userId.value, permissionId.value)) {
            userPermissionJpaRepository.save(UserPermissionJpaEntity(userId.value, permissionId.value))
        }
    }

    override fun removePermissionFromUser(userId: UserId, permissionId: PermissionId) {
        userPermissionJpaRepository.deleteById(UserPermissionId(userId.value, permissionId.value))
    }

    override fun getDirectPermissionIdsByUserId(userId: UserId): Set<PermissionId> {
        return userPermissionJpaRepository.findByUserId(userId.value)
            .map { PermissionId(it.permissionId) }
            .toSet()
    }

    override fun userHasDirectPermission(userId: UserId, permissionId: PermissionId): Boolean {
        return userPermissionJpaRepository.existsByUserIdAndPermissionId(userId.value, permissionId.value)
    }

    override fun removeAllPermissionsFromUser(userId: UserId) {
        userPermissionJpaRepository.deleteByUserId(userId.value)
    }
}
