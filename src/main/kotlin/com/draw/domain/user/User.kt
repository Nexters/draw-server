package com.draw.domain.user

import com.draw.common.enums.OAuthProvider
import com.draw.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "draw_user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var kakaoId: String? = null,

    var appleId: String? = null,

    var registrationCompleted: Boolean = false,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var oauthProvider: OAuthProvider? = null,

    var refreshToken: String? = null,
) : BaseEntity() {

    // TODO: 개발용 임시 2023/08/04 (koi)
    fun getIntAge(): Int {
        return 29
    }
}
