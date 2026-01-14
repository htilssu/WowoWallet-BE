package com.wowo.wowo.infrastructure.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val jwtSecret: String,
    @Value("\${jwt.expiration-ms:86400000}") private val jwtExpirationMs: Long, // Default 24 hours
    @Value("\${jwt.refresh-expiration-ms:604800000}") private val refreshExpirationMs: Long // Default 7 days
) {

    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    fun generateAccessToken(userId: String, email: String, roles: Set<String> = emptySet()): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationMs)

        return Jwts.builder()
            .subject(userId)
            .claim("email", email)
            .claim("roles", roles.toList())
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact()
    }

    fun generateRefreshToken(userId: String): String {
        val now = Date()
        val expiryDate = Date(now.time + refreshExpirationMs)

        return Jwts.builder()
            .subject(userId)
            .claim("type", "refresh")
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
            true
        } catch (ex: Exception) {
            false
        }
    }

    fun getUserIdFromToken(token: String): String {
        val claims = getClaims(token)
        return claims.subject
    }

    fun getEmailFromToken(token: String): String? {
        val claims = getClaims(token)
        return claims["email"] as? String
    }

    @Suppress("UNCHECKED_CAST")
    fun getRolesFromToken(token: String): Set<String> {
        val claims = getClaims(token)
        val roles = claims["roles"] as? List<String> ?: emptyList()
        return roles.toSet()
    }

    fun isRefreshToken(token: String): Boolean {
        val claims = getClaims(token)
        return claims["type"] == "refresh"
    }

    fun getExpirationFromToken(token: String): Date {
        val claims = getClaims(token)
        return claims.expiration
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }
}
