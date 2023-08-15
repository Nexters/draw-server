package com.draw.domain.user

import com.draw.common.enums.Gender
import com.draw.common.enums.MBTI
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
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity
@Table(name = "draw_user")
@SQLDelete(sql = "UPDATE draw_user SET deleted_at = CURRENT_TIMESTAMP() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
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

    @Enumerated(EnumType.STRING)
    var mbti: MBTI? = null,

    @Enumerated(EnumType.STRING)
    var gender: Gender? = null,

    var dateOfBirth: String? = null,

    var fcmToken: String? = null,

    var lastFitPushReceivedAt: ZonedDateTime? = null,

    var deletedAt: ZonedDateTime? = null,
) : BaseEntity() {
    fun grantPoint(point: Point) {
        this.point = point.value
    }

    fun canUsePoint(value: Long): Boolean {
        return this.point >= value
    }

    fun usePoint(value: Long) {
        check(canUsePoint(value)) { "can't use point" }

        this.point = this.point - value
    }

    fun addPoint(value: Long) {
        this.point = this.point + value
    }

    fun getPoint(): Point {
        return Point(this.point)
    }

    fun isFirstActivity(): Boolean {
        return lastLoggedAt == null
    }

    fun getAge(): Int {
        // 백도어 API 로 가입완료 시킨 유저들 대응하기 위해서 임시로 추가
        if (dateOfBirth == null) {
            return 20
        }
        require(dateOfBirth!!.length == 6) { "dateOfBirth length must be 6" }

        val userYear = dateOfBirth!!.substring(0, 2).let {
            if (it.toInt() > 40) {
                "19$it"
            } else {
                "20$it"
            }
        }.toInt()

        val now = LocalDate.now(ZoneId.of("Asia/Seoul"))
        val thisYearBirthDay =
            LocalDate.of(now.year, dateOfBirth!!.substring(2, 4).toInt(), dateOfBirth!!.substring(4, 6).toInt())

        if (now.isAfter(thisYearBirthDay)) {
            return now.year - userYear
        }

        return now.year - userYear - 1
    }

    fun isFitRecommendationIntervalElapsed(): Boolean {
        if (lastFitPushReceivedAt == null) {
            return true
        }
        val gap = Duration.between(ZonedDateTime.now(), lastFitPushReceivedAt)
        val minGap = Duration.ofHours(6)
        if (gap >= minGap) {
            return true
        }
        return false
    }

    fun markFitRecommendationReceived() {
        this.lastFitPushReceivedAt = ZonedDateTime.now()
    }
}
