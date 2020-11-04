package com.seemnerdy.sudokusolver.app.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class SudokuCellDecorator(context: Context) : RecyclerView.ItemDecoration() {


    private val thickness = TypedValue
        .applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            1.toFloat(),
            context.resources.displayMetrics
        )
        .roundToInt()

    private val paint = Paint().apply {
        color = Color.GRAY
    }

    private val bounds = Rect()

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null) {
            return
        }
        drawVertical(c, parent)
        drawHorizontal(c, parent)
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int

        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left, parent.paddingTop, right,
                parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }

        val children = parent
            .children
            .filter { v ->
                val coordinate = parent.indexOfChild(v).toCoordinate()
                coordinate.isLastInRow()
            }
            .toList()
            .dropLast(1)

        children.forEach { v ->
            parent.getDecoratedBoundsWithMargins(v, bounds)
            val bottom = bounds.bottom + v.translationY.roundToInt()
            val top = bottom - thickness
            val rect = Rect(left, top, right, bottom)
            canvas.drawRect(rect, paint)
        }

        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val top: Int
        val bottom: Int

        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(
                parent.paddingLeft, top,
                parent.width - parent.paddingRight, bottom
            )
        } else {
            top = 0
            bottom = parent.height
        }

        val children = parent
            .children
            .filter { v ->
                val coordinate = parent.indexOfChild(v).toCoordinate()
                coordinate.isLastInColumn()
            }
            .toList()
            .dropLast(1)

        children.forEach { v ->
            parent.getDecoratedBoundsWithMargins(v, bounds)
            val right = bounds.right + v.translationX.roundToInt()
            val left = right - thickness
            val rect = Rect(left, top, right, bottom)
            canvas.drawRect(rect, paint)
        }

        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val index = parent.indexOfChild(view).toCoordinate()
        when {
            index.isLastInColumn() && index.isLastInRow() -> outRect.set(0, 0, 0, 0)
            index.isLastInColumn() -> outRect.set(0, 0, thickness, 0)
            index.isLastInRow() -> outRect.set(0, 0, 0, thickness)
            else -> outRect.set(0, 0, thickness, thickness)
        }
    }
}