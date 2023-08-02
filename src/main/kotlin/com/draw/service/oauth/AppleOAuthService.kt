package com.draw.service.oauth

import com.draw.common.enums.OAuthProvider
import com.draw.component.JwtProvider
import com.draw.domain.user.User
import com.draw.infra.external.apple.AppleOauthClient
import com.draw.infra.external.apple.ApplePubKey
import com.draw.infra.persistence.user.UserRepository
import com.draw.properties.AppleOAuthProperties
import com.draw.service.oauth.dto.LoginResult
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec

@Service
class AppleOAuthService(
    private val appleOauthClient: AppleOauthClient,
    private val appleOauthProperties: AppleOAuthProperties,
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
    private val objectMapper: ObjectMapper,
) {
    fun registerOrLogin(idToken: String): LoginResult {
        val header =
            objectMapper.readValue(String(Base64.decodeBase64(idToken)), Map::class.java) as Map<String, String>
        val publicKey = genPubKey(header["kid"]!!, header["alg"]!!)
        val parsedToken = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(idToken)
        val iss = parsedToken.body["iss"]
        if (iss != appleOauthProperties.aud) {
            throw IllegalArgumentException("apple token issuer is invalid")
        }
        val appleId = parsedToken.body["sub"] as String
        val user = userRepository.findByAppleId(appleId)
        if (user != null) {
            return LoginResult.normal(jwtProvider.generateAccessToken(user), jwtProvider.generateRefreshToken(user))
        }
        val newUser = User(appleId = appleId, oauthProvider = OAuthProvider.APPLE)
        val accessToken = jwtProvider.generateAccessToken(newUser)
        newUser.refreshToken = jwtProvider.generateRefreshToken(newUser)
        userRepository.save(newUser)
        return LoginResult.newlyRegistered(accessToken, newUser.refreshToken!!)
    }

    private fun genPubKey(kid: String, alg: String): PublicKey {
        val applePubKey = getApplePubKey(kid, alg)
        val n = BigInteger(1, Base64.decodeBase64URLSafe(applePubKey.n))
        val e = BigInteger(1, Base64.decodeBase64URLSafe(applePubKey.e))
        val keySpec = RSAPublicKeySpec(n, e)
        val keyFactory = KeyFactory.getInstance(applePubKey.kty)
        return keyFactory.generatePublic(keySpec)
    }

    private fun getApplePubKey(kid: String, alg: String): ApplePubKey {
        return getPubKeys().find { key -> key.kid == kid && key.alg == alg }!!
    }

    private fun getPubKeys(): List<ApplePubKey> {
        return appleOauthClient.getApplePubKeys().keys
    }
}