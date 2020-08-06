package com.seemnerdy.sudokusolver.app

import android.content.res.Resources
import com.seemsnerdy.sudokusolver.CheckOutcome
import com.seemsnerdy.sudokusolver.Coordinate
import com.seemsnerdy.sudokusolver.GameBoard
import com.seemsnerdy.sudokusolver.validateGame

fun readPuzzle(resources: Resources, id: Int): GameBoard =
    resources.openRawResource(id).reader().readLines()
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

fun validatePuzzle(resources: Resources): CheckOutcome {
    val input = readPuzzle(resources, R.raw.input)
    val solution = readPuzzle(resources, R.raw.solution)
    return validateGame(input, solution)
}