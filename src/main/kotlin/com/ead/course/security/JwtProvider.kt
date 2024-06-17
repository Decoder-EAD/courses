package com.ead.course.security

import com.ead.course.config.Log
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import javax.crypto.SecretKey


@Component
class JwtProvider(

    @Value("\${ead.auth.jwtSecret}")
    private val jwtSecret: String,

    ) {

    companion object : Log()

    fun getSubjectJwt(token: String): String {
        return Jwts.parser()
            .verifyWith(getSecretKey())
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }

    fun getClaimNameJwt(token: String, claimName: String): String {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).payload[claimName].toString()
    }

    fun validateJwt(authToken: String?): Boolean {
        try {
            Jwts.parser()
                .verifyWith(getSecretKey()).build()
                .parseSignedClaims(authToken)
            return true
        } catch (e: SecurityException) {
            log.error("Invalid JWT signature: {}", e.message)
        } catch (e: MalformedJwtException) {
            log.error("Invalid JWT token: {}", e.message)
        } catch (e: ExpiredJwtException) {
            log.error("JWT token is expired: {}", e.message)
        } catch (e: UnsupportedJwtException) {
            log.error("JWT token is unsupported: {}", e.message)
        } catch (e: IllegalArgumentException) {
            log.error("JWT claims string is empty: {}", e.message)
        }
        return false
    }

    private fun getSecretKey(): SecretKey? = Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8))

}