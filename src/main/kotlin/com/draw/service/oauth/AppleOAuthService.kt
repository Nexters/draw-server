package com.draw.service.oauth

import com.draw.common.enums.OAuthProvider
import com.draw.component.JwtProvider
import com.draw.domain.promotion.NewlyRegisterPromotionGenerator
import com.draw.domain.user.User
import com.draw.infra.external.apple.AppleOauthClient
import com.draw.infra.external.apple.ApplePubKey
import com.draw.infra.external.apple.RevokeRequest
import com.draw.infra.persistence.user.UserRepository
import com.draw.properties.AppleOAuthProperties
import com.draw.service.oauth.dto.LoginResult
import com.draw.service.promotion.PromotionService
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import mu.KotlinLogging
import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.stereotype.Service
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
    private val newlyRegisterPromotionGenerator: NewlyRegisterPromotionGenerator,
    private val promotionService: PromotionService,
    private val appleTokenGenerator: AppleTokenGenerator,
) {
    private val log = KotlinLogging.logger { }

    fun registerOrLogin(idToken: String, code: String): LoginResult {
        val header =
            objectMapper.readValue(String(Base64.decodeBase64(idToken)), Map::class.java) as Map<String, String>
        log.info("$header, $idToken")
        log.info("${header["kid"]!!}, ${header["alg"]!!}")
        log.info("애플 id token: $idToken, 애플 코드 $code")
        val publicKey = genPubKey(header["kid"]!!, header["alg"]!!)
        val parsedToken = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(idToken)
        log.info("${parsedToken.body}")
        val iss = parsedToken.body["iss"]
        if (iss != appleOauthProperties.aud) {
            throw IllegalArgumentException("apple token issuer is invalid")
        }

        val validationResult = appleTokenGenerator.generateAppleToken(authorizationCode = code)
        val appleId = parsedToken.body["sub"] as String
        val user = userRepository.findByAppleId(appleId)
        if (user != null) {
            if (!user.registrationCompleted) {
                return LoginResult.newlyRegistered(jwtProvider.generateAccessToken(user), jwtProvider.generateRefreshToken(user))
            }
            return LoginResult.normal(jwtProvider.generateAccessToken(user), jwtProvider.generateRefreshToken(user))
        }
        val newUser = userRepository.save(User(appleId = appleId, oauthProvider = OAuthProvider.APPLE))
        val accessToken = jwtProvider.generateAccessToken(newUser)
        newUser.refreshToken = jwtProvider.generateRefreshToken(newUser)
        newUser.appleRefreshToken = validationResult.refreshToken!!
        promotionService.grant(newlyRegisterPromotionGenerator.generate(newUser))
        userRepository.save(newUser)
        return LoginResult.newlyRegistered(accessToken, newUser.refreshToken!!)
    }

    fun withdraw(user: User) {
        appleOauthClient.revoke(
            RevokeRequest(
                clientId = appleOauthProperties.serviceId,
                clientSecret = appleTokenGenerator.createClientSecretToken(),
                token = user.appleRefreshToken!!,
                tokenTypeHint = "refresh_token",
            ).toMap(),
        )
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
