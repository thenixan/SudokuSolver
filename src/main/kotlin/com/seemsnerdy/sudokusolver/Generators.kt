package com.seemsnerdy.sudokusolver

typealias Generator = (Int) -> Coordinate

fun rowCoordinateGenerator(rowNumber: Int): Generator = { index -> Coordinate(x = rowNumber, y = index) }
fun colCoordinateGenerator(colNumber: Int): Generator = { index -> Coordinate(x = index, y = colNumber) }
fun qadCoordinateGenerator(qadNumber: Int): Generator = { index ->
    val x = 3 * (qadNumber / 3) + index / 3
    val y = 3 * (qadNumber % 3) + index % 3
    Coordinate(x, y)
}