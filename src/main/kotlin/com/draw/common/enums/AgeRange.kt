package com.draw.common.enums

enum class AgeRange(
    private val scope: IntRange,
) {
    ZERO_TO_TEN(0..9),
    TEEN(10..19),
    TWENTY(20..29),
    THIRTY(30..39),
    FORTY(40..49),
    FIFTY(50..59),
    SIXTY(60..69),
    SEVENTY(70..79),
    EIGHTY(80..89),
    NINETY(90..99),
    OVER_HUNDRED(100..Int.MAX_VALUE),

    ALL(0..Int.MAX_VALUE),
    ;

    fun isInScope(intAge: Int): Boolean {
        return scope.contains(intAge)
    }

    companion object {
        fun of(intAge: Int): AgeRange {
            return listOf(
                ZERO_TO_TEN,
                TEEN,
                TWENTY,
                THIRTY,
                FORTY,
                FIFTY,
                SIXTY,
                SEVENTY,
                EIGHTY,
                NINETY,
                OVER_HUNDRED
            ).first { it.isInScope(intAge) }
        }
    }
}
