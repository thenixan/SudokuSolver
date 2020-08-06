package com.seemsnerdy.sudokusolver

typealias GameBoard = Map<Coordinate, Int>

fun validateGame(input: GameBoard, solution: GameBoard): CheckOutcome {
    var result = CheckOutcome.Ok
    for (i in 0 until 9) {
        val rowCheckOutcome =
            validateSimilar(input, solution, rowCoordinateGenerator(rowNumber = i))
        if (rowCheckOutcome != CheckOutcome.Ok) {
            result = rowCheckOutcome
            break
        }
        val columnCheckOutcome = validateSimilar(input, solution, colCoordinateGenerator(colNumber = i))
        if (columnCheckOutcome != CheckOutcome.Ok) {
            result = columnCheckOutcome
            break
        }
        val quadrantCheckOutcome = validateSimilar(input, solution, qadCoordinateGenerator(qadNumber = i))
        if (quadrantCheckOutcome != CheckOutcome.Ok) {
            result = quadrantCheckOutcome
            break
        }
    }
    return result
}

fun validateSimilar(
    input: GameBoard,
    solution: GameBoard,
    coordinateGenerator: Generator
): CheckOutcome {
    for (i in 0 until 8) {
        val checkedCoordinate = coordinateGenerator(i)
        val checkedValue = input[checkedCoordinate] ?: solution[checkedCoordinate] ?: return CheckOutcome.Incomplete
        for (j in i + 1 until 9) {
            val internalCoordinate = coordinateGenerator(j)
            val internalValue =
                input[internalCoordinate] ?: solution[internalCoordinate] ?: return CheckOutcome.Incomplete
            if (checkedValue == internalValue) {
                return CheckOutcome.Failed
            }
        }
    }
    return CheckOutcome.Ok
}