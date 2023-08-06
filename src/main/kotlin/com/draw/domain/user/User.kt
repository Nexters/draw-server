package com.draw.domain.user

import com.draw.common.enums.OAuthProvider
import com.draw.domain.Point
import com.draw.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime

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

    private var point: Long = 0L,

    var lastLoggedAt: ZonedDateTime? = null,
) : BaseEntity() {
    fun grantPoint(point: Point) {
        this.point = point.value
    }
    fun getPoint(): Point {
        return Point(this.point)
    }

    fun isFirstActivity(): Boolean {
        return lastLoggedAt == null
    }
}
