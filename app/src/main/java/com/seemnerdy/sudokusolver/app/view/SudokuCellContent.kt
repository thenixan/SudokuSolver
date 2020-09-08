package com.seemnerdy.sudokusolver.app.view

sealed class SudokuCellContent {
    data class Note(val data: IntArray) : SudokuCellContent() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Note

            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            return data.contentHashCode()
        }
    }

    data class Solution(val data: Int) : SudokuCellContent()
}