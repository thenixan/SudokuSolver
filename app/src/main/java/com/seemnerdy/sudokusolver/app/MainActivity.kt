package com.seemnerdy.sudokusolver.app

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.seemsnerdy.sudokusolver.CheckOutcome

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
        }
    }
}
