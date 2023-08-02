package com.draw.common.enums

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class AgeOptionTest {

    @ParameterizedTest
    @ValueSource(ints = [1, 29, 30, 31, 50])
    fun `ALL 옵션이 오면 전체 나이와 관계없이 ALL Range를 반환한다`(userIntAge: Int) {
        // given
        val ageOption = AgeOption.ALL

        // when
        val ageRange = ageOption.toAgeRange(userIntAge)

        // then
        assertThat(ageRange).isEqualTo(AgeRange.ALL)
    }

    @ParameterizedTest
    @ValueSource(ints = [10, 15, 19])
    fun `SAME_AGE_GROUP 옵션이면서, 올린시점에 10대이면, 동일한 Range를 반환한다`(userIntAge: Int) {
        // given
        val ageOption = AgeOption.SAME_AGE_GROUP

        // when
        val ageRange = ageOption.toAgeRange(userIntAge)

        // then
        assertThat(ageRange).isEqualTo(AgeRange.TEEN)
    }

    @ParameterizedTest
    @ValueSource(ints = [20, 25, 29])
    fun `SAME_AGE_GROUP 옵션이면서, 올린시점에 20대이면, 동일한 Range를 반환한다`(userIntAge: Int) {
        // given
        val ageOption = AgeOption.SAME_AGE_GROUP

        // when
        val ageRange = ageOption.toAgeRange(userIntAge)

        // then
        assertThat(ageRange).isEqualTo(AgeRange.TWENTY)
    }

    @ParameterizedTest
    @ValueSource(ints = [30, 35, 39])
    fun `SAME_AGE_GROUP 옵션이면서, 올린시점에 30대이면, 동일한 Range를 반환한다`(userIntAge: Int) {
        // given
        val ageOption = AgeOption.SAME_AGE_GROUP

        // when
        val ageRange = ageOption.toAgeRange(userIntAge)

        // then
        assertThat(ageRange).isEqualTo(AgeRange.THIRTY)
    }

    @ParameterizedTest
    @ValueSource(ints = [40, 45, 49])
    fun `SAME_AGE_GROUP 옵션이면서, 올린시점에 40대이면, 동일한 Range를 반환한다`(userIntAge: Int) {
        // given
        val ageOption = AgeOption.SAME_AGE_GROUP

        // when
        val ageRange = ageOption.toAgeRange(userIntAge)

        // then
        assertThat(ageRange).isEqualTo(AgeRange.FORTY)
    }

    @ParameterizedTest
    @ValueSource(ints = [50, 55, 59])
    fun `SAME_AGE_GROUP 옵션이면서, 올린시점에 50대이면, 동일한 Range를 반환한다`(userIntAge: Int) {
        // given
        val ageOption = AgeOption.SAME_AGE_GROUP

        // when
        val ageRange = ageOption.toAgeRange(userIntAge)

        // then
        assertThat(ageRange).isEqualTo(AgeRange.FIFTY)
    }

    @ParameterizedTest
    @ValueSource(ints = [60, 65, 69])
    fun `SAME_AGE_GROUP 옵션이면서, 올린시점에 60대이면, 동일한 Range를 반환한다`(userIntAge: Int) {
        // given
        val ageOption = AgeOption.SAME_AGE_GROUP

        // when
        val ageRange = ageOption.toAgeRange(userIntAge)

        // then
        assertThat(ageRange).isEqualTo(AgeRange.SIXTY)
    }

    @ParameterizedTest
    @ValueSource(ints = [70, 75, 79])
    fun `SAME_AGE_GROUP 옵션이면서, 올린시점에 70대이면, 동일한 Range를 반환한다`(userIntAge: Int) {
        // given
        val ageOption = AgeOption.SAME_AGE_GROUP

        // when
        val ageRange = ageOption.toAgeRange(userIntAge)

        // then
        assertThat(ageRange).isEqualTo(AgeRange.SEVENTY)
    }

    @ParameterizedTest
    @ValueSource(ints = [80, 85, 89])
    fun `SAME_AGE_GROUP 옵션이면서, 올린시점에 80대이면, 동일한 Range를 반환한다`(userIntAge: Int) {
        // given
        val ageOption = AgeOption.SAME_AGE_GROUP

        // when
        val ageRange = ageOption.toAgeRange(userIntAge)

        // then
        assertThat(ageRange).isEqualTo(AgeRange.EIGHTY)
    }

    @ParameterizedTest
    @ValueSource(ints = [90, 95, 99])
    fun `SAME_AGE_GROUP 옵션이면서, 올린시점에 90대이면, 동일한 Range를 반환한다`(userIntAge: Int) {
        // given
        val ageOption = AgeOption.SAME_AGE_GROUP

        // when
        val ageRange = ageOption.toAgeRange(userIntAge)

        // then
        assertThat(ageRange).isEqualTo(AgeRange.NINETY)
    }
}
