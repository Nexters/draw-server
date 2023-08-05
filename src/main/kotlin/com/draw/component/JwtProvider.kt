package com.draw.component

import com.draw.domain.user.User
import com.draw.infra.persistence.user.UserRepository
import com.draw.properties.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtProvider(
    private val userRepository: UserRepository,
    private val jwtProperties: JwtProperties,
) {
    private val secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secretKey))

    fun getId(token: String) = getUser(token)["id"] as String

    fun getUser(token: String) =
        Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).body!!

    fun authenticate(token: String): Authentication {
        val user = userRepository.findByIdOrNull(getId(token).toLong())
            ?: throw RuntimeException("authentication user not found")
        return UsernamePasswordAuthenticationToken(user, javaClass, listOf())
    }

    fun generateAccessToken(
        user: User,
        lifeTime: Long? = null,
    ): String {
        val expire = lifeTime ?: jwtProperties.accessTokenExpireMs
        val claims = Jwts.claims().setSubject("draw-accessToken").also {
            it["token_type"] = ACCESS_TOKEN
            it["id"] = user.id.toString()
        }
        val now = Date()
        return createToken(claims, now, Date(now.time + expire))
    }

    fun generateRefreshToken(
        user: User,
        lifeTime: Long? = null,
    ): String {
        val expire = lifeTime ?: jwtProperties.refreshTokenExpireMs
        val claims = Jwts.claims().setSubject("draw-token").also {
            it["token_type"] = REFRESH_TOKEN
            it["id"] = user.id.toString()
        }
        val now = Date()
        val createdToken = createToken(claims, now, Date(now.time + expire))
        user.refreshToken = createdToken
        userRepository.save(user)
        return createdToken
    }

    private fun createToken(claims: Claims, issuedAt: Date, expiredAt: Date): String {
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(issuedAt)
            .setExpiration(expiredAt)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validate(user: User, token: String) {
        val claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
        val tokenType = claims.body["token_type"]!!

        val expiration = claims.body.expiration
        val isExpired = expiration.before(Date())

        if (tokenType == REFRESH_TOKEN) {
            if (isExpired) {
                user.refreshToken = null
                userRepository.save(user)
                throw RuntimeException("RefreshToken expired")
            }
        }
        if (isExpired) {
            throw RuntimeException("AccessToken expired")
        }
    }

    companion object {
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val REFRESH_TOKEN = "REFRESH_TOKEN"
    }
}
