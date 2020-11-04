package com.seemnerdy.sudokusolver.app.adapter

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class SudokuLayoutManager(context: Context) : GridLayoutManager(context, 9) {
    override fun canScrollHorizontally(): Boolean = false
    override fun canScrollVertically(): Boolean = false
}
