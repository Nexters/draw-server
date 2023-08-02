package com.draw.common.enums

enum class AgeOption {
    ALL, SAME_AGE_GROUP
    ;

    fun toAgeRange(userIntAge: Int): AgeRange {
        return when (this) {
            ALL -> AgeRange.ALL
            SAME_AGE_GROUP -> AgeRange.of(userIntAge)
        }
    }
}
