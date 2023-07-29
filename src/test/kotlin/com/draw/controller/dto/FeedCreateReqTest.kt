package com.draw.controller.dto

import com.draw.common.enums.AgeRange
import com.draw.common.enums.Gender
import com.draw.common.enums.MBTIChar
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test

class FeedCreateReqTest {

    @Test
    fun `Feed로 변환된다`() {
        // given
        val req = FeedCreateReq(
            content = "content",
            genders = listOf(Gender.MALE),
            ageRange = AgeRange.ALL,
            mbtiChars = listOf(MBTIChar.E, MBTIChar.S)
        )

        // when
        val feed = req.toEntity(1L)

        // then
        assertAll(
            { assertThat(req.content).isEqualTo(feed.content) },
            { assertThat(req.genders).isEqualTo(feed.genders) },
            { assertThat(req.ageRange).isEqualTo(feed.ageRange) },
            { assertThat(req.mbtiChars).isEqualTo(feed.mbtiChars) },
            { assertThat(feed.writerId).isEqualTo(1L) },
        )
    }
}
