package com.wowo.wowo.infrastructure.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val jwt = getJwtFromRequest(request)
            
            if (jwt != null && jwtTokenProvider.validateToken(jwt) && !jwtTokenProvider.isRefreshToken(jwt)) {
                val userId = jwtTokenProvider.getUserIdFromToken(jwt)
                val roles = jwtTokenProvider.getRolesFromToken(jwt)
                
                val authorities = roles.map { SimpleGrantedAuthority("ROLE_$it") }
                
                val authentication = UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    authorities
                )
                
                // Store additional info
                authentication.details = JwtAuthenticationDetails(
                    userId = userId,
                    email = jwtTokenProvider.getEmailFromToken(jwt),
                    roles = roles
                )
                
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (ex: Exception) {
            logger.error("Could not set user authentication in security context", ex)
        }
        
        filterChain.doFilter(request, response)
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else {
            null
        }
    }
}

data class JwtAuthenticationDetails(
    val userId: String,
    val email: String?,
    val roles: Set<String>
)
