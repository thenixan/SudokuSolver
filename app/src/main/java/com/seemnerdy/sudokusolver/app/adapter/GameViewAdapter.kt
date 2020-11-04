package com.seemnerdy.sudokusolver.app.adapter

import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seemnerdy.sudokusolver.app.view.SudokuCellContent
import com.seemnerdy.sudokusolver.app.view.SudokuCellView
import com.seemsnerdy.sudokusolver.GameBoard

class GameViewAdapter : RecyclerView.Adapter<SudokuCellViewHolder>() {

    var termsGameBoard: GameBoard = mapOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SudokuCellViewHolder {
        val view = SudokuCellView(parent.context)
        return SudokuCellViewHolder(view)
    }

    override fun getItemCount(): Int = 9 * 9

    override fun onBindViewHolder(holder: SudokuCellViewHolder, position: Int) {
        val coordinate = position.toCoordinate()
        val content = termsGameBoard[coordinate]?.let { SudokuCellContent.Solution(it) }
        holder.setSudokuCellContent(content)
        if (coordinate.quadrant().greyedOut()) {
            holder.setBackgroundColor(Color.LTGRAY)
        } else {
            holder.setBackgroundColor(Color.TRANSPARENT)
        }
    }

}