package com.seemnerdy.sudokusolver.app

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.seemnerdy.sudokusolver.app.view.SudokuCellContent
import com.seemnerdy.sudokusolver.app.view.SudokuCellView
import com.seemsnerdy.sudokusolver.CheckOutcome
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.solve_puzzle_button).setOnClickListener {
            val resultString = when (validatePuzzle(this.resources)) {
                CheckOutcome.Ok -> "Puzzle is correct"
                CheckOutcome.Incomplete -> "Puzzle is incomplete"
                CheckOutcome.Failed -> "Puzzle is wrong"
            }
            findViewById<TextView>(R.id.puzzle_outcome_text).text = resultString
            findViewById<SudokuCellView>(R.id.test_puzzle_cell).sudokuCellContent =
                if (Random.nextBoolean()) {
                    SudokuCellContent.Solution(Random.nextInt(1, 9))
                } else {
                    SudokuCellContent.Note((1..9).filter { Random.nextBoolean() }.toIntArray())
                }
        }
    }
}
