package com.draw.common.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class VisibleTargetTest {

    @Test
    fun `성인이면 ADULT를 반환한다`() {
        // given
        val userIntAge = 19

        // when
        val visibleTarget = VisibleTarget.of(userIntAge)

        // then
        assertEquals(VisibleTarget.ADULT, visibleTarget)
    }

    @Test
    fun `성인이 아니면 CHILDREN을 반환한다`() {
        // given
        val userIntAge = 18

        // when
        val visibleTarget = VisibleTarget.of(userIntAge)

        // then
        assertEquals(VisibleTarget.CHILDREN, visibleTarget)
    }
}
