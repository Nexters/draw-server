package com.draw.service.dto

import com.draw.common.enums.AgeRange
import com.draw.common.enums.Gender
import com.draw.common.enums.MBTI
import com.draw.common.enums.MBTIChar
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class FeedProjectionTest {

    @Test
    fun `MBTI를 등록하지 않아도 성별 및 나이가 범위내에 들면, 맞춤피드이다`() {
        // given
        val feedProjection = FeedProjection(
            id = 1L,
            content = "content",
            genders = mutableListOf(Gender.MALE, Gender.FEMALE),
            mbtiChars = mutableListOf(),
            ageRange = AgeRange.ALL,
            isFavorite = false,
            createdAt = ZonedDateTime.now(),
            favoriteCount = 10,
        )
        // when
        val fit = feedProjection.isFit(Gender.MALE, 29, MBTI.ESTJ)

        // then
        assertTrue(fit)
    }

    @Test
    fun `등록한 MBTI에 모두 포함되고 성별 및 나이가 범위내에 들면, 맞춤피드이다`() {
        // given
        val feedProjection = FeedProjection(
            id = 1L,
            content = "content",
            genders = mutableListOf(Gender.MALE, Gender.FEMALE),
            mbtiChars = mutableListOf(MBTIChar.E, MBTIChar.S, MBTIChar.T),
            ageRange = AgeRange.ALL,
            isFavorite = false,
            createdAt = ZonedDateTime.now(),
            favoriteCount = 10,
        )

        // when
        val fit = feedProjection.isFit(Gender.MALE, 29, MBTI.ESTJ)

        // then
        assertTrue(fit)
    }

    @Test
    fun `등록한 MBTI에 포함되고 성별 및 나이가 범위내에 들면, 맞춤피드이다-2`() {
        // given
        val feedProjection = FeedProjection(
            id = 1L,
            content = "content",
            genders = mutableListOf(Gender.MALE, Gender.FEMALE),
            mbtiChars = mutableListOf(MBTIChar.E, MBTIChar.T),
            ageRange = AgeRange.ALL,
            isFavorite = false,
            createdAt = ZonedDateTime.now(),
            favoriteCount = 10,
        )

        // when
        val fit = feedProjection.isFit(Gender.MALE, 29, MBTI.ESTJ)

        // then
        assertTrue(fit)
    }

    @Test
    fun `등록한 MBTI에 포함되고 성별 및 나이가 범위내에 들면, 맞춤피드이다-3`() {
        // given
        val feedProjection = FeedProjection(
            id = 1L,
            content = "content",
            genders = mutableListOf(Gender.MALE, Gender.FEMALE),
            mbtiChars = mutableListOf(MBTIChar.S, MBTIChar.T),
            ageRange = AgeRange.ALL,
            isFavorite = false,
            createdAt = ZonedDateTime.now(),
            favoriteCount = 10,
        )

        // when
        val fit = feedProjection.isFit(Gender.MALE, 29, MBTI.ESTJ)

        // then
        assertTrue(fit)
    }

    @Test
    fun `등록한 MBTI에 하나라도 일치하지 않으면, 맞춤피드가 아니다`() {
        // given
        val feedProjection = FeedProjection(
            id = 1L,
            content = "content",
            genders = mutableListOf(Gender.MALE, Gender.FEMALE),
            mbtiChars = mutableListOf(MBTIChar.N, MBTIChar.T),
            ageRange = AgeRange.ALL,
            isFavorite = false,
            createdAt = ZonedDateTime.now(),
            favoriteCount = 10,
        )

        // when
        val fit = feedProjection.isFit(Gender.MALE, 29, MBTI.ESTJ)

        // then
        assertFalse(fit)
    }

    @Test
    fun `등록한 MBTI중 같은 위치의 MBTI 두가지중 한가지만 일치하고, 성별 및 나이가 일치하면 맞춤 피드이다`() {
        // given
        val feedProjection = FeedProjection(
            id = 1L,
            content = "content",
            genders = mutableListOf(Gender.MALE, Gender.FEMALE),
            mbtiChars = mutableListOf(MBTIChar.E, MBTIChar.S, MBTIChar.T, MBTIChar.F),
            ageRange = AgeRange.ALL,
            isFavorite = false,
            createdAt = ZonedDateTime.now(),
            favoriteCount = 10,
        )

        // when
        val fit = feedProjection.isFit(Gender.MALE, 29, MBTI.ESTJ)

        // then
        assertTrue(fit)
    }

    @Test
    fun `성별이 일치하지 않으면 맞춤피드가 아니다`() {
        // given
        val feedProjection = FeedProjection(
            id = 1L,
            content = "content",
            genders = mutableListOf(Gender.FEMALE),
            mbtiChars = mutableListOf(),
            ageRange = AgeRange.ALL,
            isFavorite = false,
            createdAt = ZonedDateTime.now(),
            favoriteCount = 10,
        )

        // when
        val fit = feedProjection.isFit(Gender.MALE, 29, MBTI.ESTJ)

        // then
        assertFalse(fit)
    }

    @Test
    fun `연령대가 일치하지 않으면 맞춤피드가 아니다`() {
        // given
        val feedProjection = FeedProjection(
            id = 1L,
            content = "content",
            genders = mutableListOf(Gender.FEMALE),
            mbtiChars = mutableListOf(),
            ageRange = AgeRange.THIRTY,
            isFavorite = false,
            createdAt = ZonedDateTime.now(),
            favoriteCount = 10,
        )

        // when
        val fit = feedProjection.isFit(Gender.MALE, 29, MBTI.ESTJ)

        // then
        assertFalse(fit)
    }
}
