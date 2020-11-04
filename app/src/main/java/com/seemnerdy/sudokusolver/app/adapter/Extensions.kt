package com.seemnerdy.sudokusolver.app.adapter

import com.seemsnerdy.sudokusolver.Coordinate


internal fun Int.toCoordinate(): Coordinate {
    val x = this / 9
    val y = this % 9
    return Coordinate(x, y)
}

internal fun Int.greyedOut(): Boolean =
    when (this) {
        1, 3, 5, 7 -> true
        else -> false
    }

internal fun Coordinate.isLastInRow(): Boolean = y == 8
internal fun Coordinate.isLastInColumn(): Boolean = x == 8

internal fun Coordinate.quadrant(): Int {
    val firstSpan = (0 until 3)
    val secondSpan = (3 until 6)
    val thirdSpan = (6 until 9)
    return when {
        x in firstSpan && y in firstSpan -> 0
        x in firstSpan && y in secondSpan -> 1
        x in firstSpan && y in thirdSpan -> 2
        x in secondSpan && y in firstSpan -> 3
        x in secondSpan && y in secondSpan -> 4
        x in secondSpan && y in thirdSpan -> 5
        x in thirdSpan && y in firstSpan -> 6
        x in thirdSpan && y in secondSpan -> 7
        x in thirdSpan && y in thirdSpan -> 8
        else -> throw IllegalArgumentException("Coordinate: $this is out of bounds")
    }
}