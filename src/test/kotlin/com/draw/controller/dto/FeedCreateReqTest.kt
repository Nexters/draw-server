package com.draw.controller.dto

import com.draw.common.enums.AgeOption
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
            ageOption = AgeOption.ALL,
            mbtiChars = listOf(MBTIChar.E, MBTIChar.S)
        )

        // when
        val feed = req.toEntity(1L, 29)

        // then
        assertAll(
            { assertThat(req.content).isEqualTo(feed.content) },
            { assertThat(req.genders).isEqualTo(feed.genders) },
            { assertThat(feed.ageRange).isEqualTo(AgeRange.ALL) },
            { assertThat(req.mbtiChars).isEqualTo(feed.mbtiChars) },
            { assertThat(feed.writerId).isEqualTo(1L) },
        )
    }
}
