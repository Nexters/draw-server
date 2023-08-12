package com.draw.domain.user

data class DateOfBirth(
    val value: String,
) {
    init {
        require(isValidDate(value)) { "$value 는 올바른 생년월일 값이 아닙니다" }
    }

    private fun isValidDate(input: String): Boolean {
        if (input.length != 6) {
            return false
        }

        val year = input.substring(0, 2).toInt()
        val month = input.substring(2, 4).toInt()
        val day = input.substring(4, 6).toInt()

        if (year !in 0..99) {
            return false
        }

        if (month !in 1..12) {
            return false
        }

        if (day !in 1..31) {
            return false
        }

        val maxDay = when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (year % 4 == 0) 29 else 28
            else -> return false
        }

        return day <= maxDay
    }
}
