package com.draw.common.enums

private const val i = 19

enum class VisibleTarget {
    CHILDREN, ADULT, ;

    companion object {
        private const val ADULT_MIN_AGE = 19
        fun of(age: Int): VisibleTarget {
            if (age >= ADULT_MIN_AGE) {
                return ADULT
            }

            return CHILDREN
        }
    }
}
