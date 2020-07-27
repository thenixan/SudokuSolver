package com.seemsnerdy.sudokusolver

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MainTest {

    @Test
    fun `correct solution should return an ok result`() {
        val input = """
            .6..4537.
            ..567342.
            ..4.....1
            5..7.2..4
            6.9...25.
            8.7..9..3
            49.5178..
            21..36...
            .5..2.1..
        """.trimIndent().asGameBoard()
        val solution = """
            9.21....8
            18......9
            37.29856.
            .31.6.98.
            .4.381..7
            .2.45.61.
            ..6....32
            ..89..745
            7.38.4.96
        """.trimIndent().asGameBoard()
        assertEquals(CheckOutcome.Ok, validateGame(input, solution))
    }

    @Test
    fun `incomplete solution should return an incomplete result`() {
        val input = """
            .6..4537.
            ..567342.
            ..4.....1
            5..7.2..4
            6.9...25.
            8.7..9..3
            49.5178..
            21..36...
            .5..2.1..
        """.trimIndent().asGameBoard()
        val solution = """
            9.21.....
            18......9
            37.29856.
            .31.6.98.
            .4.381..7
            .2.45.61.
            ..6....32
            ..89..745
            7.38.4.96
        """.trimIndent().asGameBoard()
        assertEquals(CheckOutcome.Incomplete, validateGame(input, solution))
    }

    @Test
    fun `failed solution should return a failed result`() {
        val input = """
            .6..4537.
            ..567342.
            ..4.....1
            5..7.2..4
            6.9...25.
            8.7..9..3
            49.5178..
            21..36...
            .5..2.1..
        """.trimIndent().asGameBoard()
        val solution = """
            9.21....1
            18......9
            37.29856.
            .31.6.98.
            .4.381..7
            .2.45.61.
            ..6....32
            ..89..745
            7.38.4.96
        """.trimIndent().asGameBoard()
        assertEquals(CheckOutcome.Failed, validateGame(input, solution))
    }

    @Test
    fun `row generator should return coordinates in single row`() {
        val generator = rowCoordinateGenerator(rowNumber = 0)
        val generated = listOf(generator(1), generator(0), generator(100))
        assertEquals(listOf(Coordinate(0, 1), Coordinate(0, 0), Coordinate(0, 100)), generated)
    }

    @Test
    fun `column generator should return coordinates in single column`() {
        val generator = colCoordinateGenerator(colNumber = 1)
        val generated = listOf(generator(1), generator(0), generator(100))
        assertEquals(listOf(Coordinate(1, 1), Coordinate(0, 1), Coordinate(100, 1)), generated)
    }

    @Test
    fun `quadrant generator should return coordinates in single area`() {
        val generator = qadCoordinateGenerator(qadNumber = 3)
        val generated = listOf(generator(1), generator(0), generator(8))
        assertEquals(listOf(Coordinate(3, 1), Coordinate(3, 0), Coordinate(5, 2)), generated)
    }
}