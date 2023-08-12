package com.draw.domain

class Point(val value: Long) {

    operator fun plus(other: Point): Point {
        return Point(this.value + other.value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Point) return false

        return value == other.value
    }
}
