package com.wowo.wowo.shared.infrastructure.security.aspect

import com.wowo.wowo.shared.infrastructure.security.Role
import com.wowo.wowo.shared.infrastructure.security.annotations.*
import com.wowo.wowo.shared.infrastructure.security.exception.AccessDeniedException
import com.wowo.wowo.shared.infrastructure.security.exception.UnauthorizedException
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.annotation.Order
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

/**
 * Aspect handling authorization annotations
 */
@Aspect
@Component
@Order(1)
class AuthorizationAspect {

    /**
     * Check authentication for @RequireAuthenticated annotation
     */
    @Before("@annotation(requireAuthenticated)")
    fun checkAuthenticated(joinPoint: JoinPoint, requireAuthenticated: RequireAuthenticated) {
        val authentication = getAuthentication()
        if (!isAuthenticated(authentication)) {
            throw UnauthorizedException()
        }
    }

    /**
     * Check role for @RequireRole annotation
     */
    @Before("@annotation(requireRole)")
    fun checkRole(joinPoint: JoinPoint, requireRole: RequireRole) {
        val authentication = getAuthentication()
        if (!isAuthenticated(authentication)) {
            throw UnauthorizedException()
        }

        val requiredRoles = requireRole.roles.map { it.value }.toSet()
        val userRoles = getUserRoles(authentication)

        if (!userRoles.any { it in requiredRoles }) {
            throw AccessDeniedException("You do not have the required role to access this resource")
        }
    }

    /**
     * Check permission for @RequirePermission annotation
     */
    @Before("@annotation(requirePermission)")
    fun checkPermission(joinPoint: JoinPoint, requirePermission: RequirePermission) {
        val authentication = getAuthentication()
        if (!isAuthenticated(authentication)) {
            throw UnauthorizedException()
        }

        val requiredPermissions = requirePermission.permissions.map { it.value }.toSet()
        val userPermissions = getUserPermissions(authentication)

        val hasPermission = if (requirePermission.requireAll) {
            userPermissions.containsAll(requiredPermissions)
        } else {
            userPermissions.any { it in requiredPermissions }
        }

        if (!hasPermission) {
            throw AccessDeniedException("You do not have permission to perform this action")
        }
    }

    /**
     * Check owner or admin permission for @RequireOwnerOrAdmin annotation
     */
    @Before("@annotation(requireOwnerOrAdmin)")
    fun checkOwnerOrAdmin(joinPoint: JoinPoint, requireOwnerOrAdmin: RequireOwnerOrAdmin) {
        val authentication = getAuthentication()
        if (!isAuthenticated(authentication)) {
            throw UnauthorizedException()
        }

        val currentUserId = getCurrentUserId(authentication)
        val userRoles = getUserRoles(authentication)

        // Check if user is Admin or Super Admin
        val isAdmin = userRoles.any { 
            it == Role.ADMIN.value || it == Role.SUPER_ADMIN.value 
        }
        
        if (isAdmin) {
            return // Admin is allowed to access
        }

        // Get owner ID from parameter
        val ownerId = extractOwnerId(joinPoint, requireOwnerOrAdmin.ownerIdParam)
        
        if (currentUserId != ownerId) {
            throw AccessDeniedException("You can only access your own resources")
        }
    }

    /**
     * Check class-level @RequireRole annotation
     */
    @Before("@within(requireRole)")
    fun checkClassLevelRole(joinPoint: JoinPoint, requireRole: RequireRole) {
        // Skip if method has @Public annotation
        if (hasPublicAnnotation(joinPoint)) {
            return
        }
        checkRole(joinPoint, requireRole)
    }

    /**
     * Check class-level @RequireAuthenticated annotation
     */
    @Before("@within(requireAuthenticated)")
    fun checkClassLevelAuthenticated(joinPoint: JoinPoint, requireAuthenticated: RequireAuthenticated) {
        // Skip if method has @Public annotation
        if (hasPublicAnnotation(joinPoint)) {
            return
        }
        checkAuthenticated(joinPoint, requireAuthenticated)
    }

    private fun getAuthentication(): Authentication? {
        return SecurityContextHolder.getContext().authentication
    }

    private fun isAuthenticated(authentication: Authentication?): Boolean {
        return authentication != null && 
               authentication.isAuthenticated && 
               authentication.principal != "anonymousUser"
    }

    private fun getUserRoles(authentication: Authentication?): Set<String> {
        return authentication?.authorities
            ?.map { it.authority }
            ?.filter { it.startsWith("ROLE_") }
            ?.toSet() ?: emptySet()
    }

    private fun getUserPermissions(authentication: Authentication?): Set<String> {
        return authentication?.authorities
            ?.map { it.authority }
            ?.filter { !it.startsWith("ROLE_") }
            ?.toSet() ?: emptySet()
    }

    private fun getCurrentUserId(authentication: Authentication?): String? {
        return authentication?.name
    }

    private fun extractOwnerId(joinPoint: JoinPoint, paramExpression: String): String? {
        val methodSignature = joinPoint.signature as MethodSignature
        val parameterNames = methodSignature.parameterNames
        val args = joinPoint.args

        // Handle simple parameter name (without SpEL)
        if (!paramExpression.startsWith("#")) {
            val paramIndex = parameterNames.indexOf(paramExpression)
            return if (paramIndex >= 0) args[paramIndex]?.toString() else null
        }

        // Handle basic SpEL expression
        val expression = paramExpression.removePrefix("#")
        val parts = expression.split(".")
        
        val rootParamName = parts[0]
        val paramIndex = parameterNames.indexOf(rootParamName)
        
        if (paramIndex < 0) return null
        
        var value: Any? = args[paramIndex]
        
        // Navigate through nested properties
        for (i in 1 until parts.size) {
            if (value == null) break
            val propertyName = parts[i]
            value = getPropertyValue(value, propertyName)
        }
        
        return value?.toString()
    }

    private fun getPropertyValue(obj: Any, propertyName: String): Any? {
        return try {
            val getter = obj.javaClass.getMethod("get${propertyName.replaceFirstChar { it.uppercase() }}")
            getter.invoke(obj)
        } catch (e: Exception) {
            try {
                val field = obj.javaClass.getDeclaredField(propertyName)
                field.isAccessible = true
                field.get(obj)
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun hasPublicAnnotation(joinPoint: JoinPoint): Boolean {
        val methodSignature = joinPoint.signature as MethodSignature
        return methodSignature.method.isAnnotationPresent(Public::class.java)
    }
}
