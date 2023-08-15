package com.draw.service.oauth

import com.draw.infra.external.apple.AppleOauthClient
import com.draw.infra.external.apple.TokenRequest
import com.draw.infra.external.apple.TokenResponse
import com.draw.properties.AppleOAuthProperties
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.springframework.stereotype.Component
import java.io.StringReader
import java.util.Date

@Component
class AppleTokenGenerator(
    private val appleOAuthProperties: AppleOAuthProperties,
    private val appleOauthClient: AppleOauthClient,
) {
    fun createClientSecretToken(): String {
        val now = Date()
        val parser = PEMParser(StringReader(appleOAuthProperties.privateKey))
        val privateKey = JcaPEMKeyConverter().getPrivateKey(parser.readObject() as PrivateKeyInfo)

        val claims = Jwts.claims().also {
            it["iss"] = appleOAuthProperties.teamId
            it["iat"] = now
            it["exp"] = Date(now.time + 1_800_000)
            it["aud"] = appleOAuthProperties.aud
        }

        return Jwts.builder()
            .setSubject(appleOAuthProperties.serviceId)
            .setClaims(claims)
            .setIssuedAt(now)
            .setHeaderParam("kid", appleOAuthProperties.kid)
            .setExpiration(Date(now.time + 1_800_000))
            .signWith(privateKey, SignatureAlgorithm.ES256)
            .compact()
    }

    fun generateAppleToken(refreshToken: String? = null, authorizationCode: String? = null): TokenResponse {
        return appleOauthClient.validateToken(
            TokenRequest(
                clientId = appleOAuthProperties.serviceId,
                clientSecret = createClientSecretToken(),
                code = authorizationCode,
                refreshToken = refreshToken,
                grantType = if (authorizationCode != null) "authorization_code" else "refresh_token",
            ).toMap(),
        )
    }
}
