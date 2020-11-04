package com.seemnerdy.sudokusolver.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.seemnerdy.sudokusolver.app.adapter.GameViewAdapter
import com.seemnerdy.sudokusolver.app.adapter.SudokuCellDecorator
import com.seemnerdy.sudokusolver.app.adapter.SudokuLayoutManager
import com.seemsnerdy.sudokusolver.Coordinate
import com.seemsnerdy.sudokusolver.GameBoard
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val adapter = GameViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<RecyclerView>(R.id.game_board).apply {
            layoutManager = SudokuLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
            addItemDecoration(SudokuCellDecorator(this@MainActivity))
        }
        adapter.termsGameBoard = randomContentGenerator()
    }

    private fun randomContentGenerator(): GameBoard = (0..9)
        .flatMap { x ->
            (0..9)
                .map { y -> Coordinate(x, y) }
                .mapNotNull { coordinate ->
                    if (Random.nextBoolean()) {
                        coordinate to Random.nextInt(1, 9)
                    } else {
                        null
                    }
                }
        }
        .toMap()
}
