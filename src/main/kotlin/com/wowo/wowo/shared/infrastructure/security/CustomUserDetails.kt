package com.wowo.wowo.shared.infrastructure.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * Custom UserDetails implementation with Role and Permission support
 */
class CustomUserDetails(
    private val userId: String,
    private val username: String,
    private val password: String,
    private val roles: Set<Role>,
    private val permissions: Set<Permission>,
    private val isEnabled: Boolean = true,
    private val isAccountNonExpired: Boolean = true,
    private val isAccountNonLocked: Boolean = true,
    private val isCredentialsNonExpired: Boolean = true
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val authorities = mutableListOf<GrantedAuthority>()
        
        // Add roles
        roles.forEach { role ->
            authorities.add(SimpleGrantedAuthority(role.value))
        }
        
        // Add permissions
        permissions.forEach { permission ->
            authorities.add(SimpleGrantedAuthority(permission.value))
        }
        
        return authorities
    }

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isEnabled(): Boolean = isEnabled

    override fun isAccountNonExpired(): Boolean = isAccountNonExpired

    override fun isAccountNonLocked(): Boolean = isAccountNonLocked

    override fun isCredentialsNonExpired(): Boolean = isCredentialsNonExpired

    fun getUserId(): String = userId

    fun getRoles(): Set<Role> = roles

    fun getPermissions(): Set<Permission> = permissions

    fun hasRole(role: Role): Boolean = roles.contains(role)

    fun hasPermission(permission: Permission): Boolean = permissions.contains(permission)

    fun hasAnyRole(vararg checkRoles: Role): Boolean = checkRoles.any { roles.contains(it) }

    fun hasAllPermissions(vararg checkPermissions: Permission): Boolean = 
        checkPermissions.all { permissions.contains(it) }

    fun hasAnyPermission(vararg checkPermissions: Permission): Boolean = 
        checkPermissions.any { permissions.contains(it) }
}
