package com.seemnerdy.sudokusolver.app.adapter

import androidx.recyclerview.widget.RecyclerView
import com.seemnerdy.sudokusolver.app.view.SudokuCellContent
import com.seemnerdy.sudokusolver.app.view.SudokuCellView

class SudokuCellViewHolder(itemView: SudokuCellView) : RecyclerView.ViewHolder(itemView) {

    fun setSudokuCellContent(content: SudokuCellContent?) {
        (itemView as SudokuCellView).sudokuCellContent = content
    }

    fun setBackgroundColor(color: Int) = itemView.setBackgroundColor(color)

}