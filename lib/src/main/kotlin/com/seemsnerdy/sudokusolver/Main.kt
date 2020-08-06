@file:JvmName("Main")

package com.seemsnerdy.sudokusolver

import java.io.File

fun main() {

    val input = readFile("input")

    val solution = readFile("solution")

    val result = validateGame(input, solution)

    println(result)
}

fun readFile(fileName: String): GameBoard =
    File(fileName)
        .readLines()
        .withIndex()
        .flatMap { indexedValue ->
            val xCoordinate = indexedValue.index
            indexedValue.value.toCharArray().withIndex()
                .filter { indexedChar -> indexedChar.value != '.' }
                .map { indexedChar ->
                    val yCoordinate = indexedChar.index
                    val coordinate = Coordinate(xCoordinate, yCoordinate)
                    val number = Character.getNumericValue(indexedChar.value)
                    coordinate to number
                }
        }
        .toMap()