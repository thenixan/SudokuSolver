package com.seemnerdy.sudokusolver.app.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.getDimensionPixelSizeOrThrow
import com.seemnerdy.sudokusolver.app.R
import kotlin.math.max
import kotlin.math.min


class SudokuCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(
    context,
    attrs,
    defStyleAttr,
    defStyleRes
) {


    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SudokuCellView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                solutionNumberSize =
                    getDimensionPixelSizeOrThrow(R.styleable.SudokuCellView_solutionNumberSize)
                notedNumberSize =
                    getDimensionPixelSizeOrThrow(R.styleable.SudokuCellView_notedNumberSize)
                innerPadding = getDimensionPixelSize(R.styleable.SudokuCellView_innerPadding, 0)
                solutionNumberColor =
                    getColor(R.styleable.SudokuCellView_solutionNumberColor, Color.BLACK)
                notedNumberColor =
                    getColor(R.styleable.SudokuCellView_notedNumberColor, Color.BLACK)
            } finally {
                recycle()
            }
        }
    }

    private val notedNumbersPaint
        get() = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textAlign = Paint.Align.CENTER
            textSize = notedNumberSize.toFloat()
            color = notedNumberColor

        }

    private val solutionNumberPaint
        get() = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textAlign = Paint.Align.CENTER
            textSize = solutionNumberSize.toFloat()
            color = solutionNumberColor
        }


    private var notedNumberSize: Int
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    private var solutionNumberSize: Int
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    private var innerPadding: Int
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    var notedNumberColor: Int
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }

    var solutionNumberColor: Int
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }

    fun setNotedNumberSize(unit: Int, size: Int) {
        notedNumberSize =
            TypedValue.applyDimension(unit, size.toFloat(), resources.displayMetrics).toInt()
    }

    fun setSolutionNumberSize(unit: Int, size: Int) {
        solutionNumberSize =
            TypedValue.applyDimension(unit, size.toFloat(), resources.displayMetrics).toInt()
    }

    fun setInnerPadding(unit: Int, size: Int) {
        innerPadding =
            TypedValue.applyDimension(unit, size.toFloat(), resources.displayMetrics).toInt()
    }

    var sudokuCellContent: SudokuCellContent? = null
        set(value) {
            field = value
            invalidate()
        }

    override fun onSaveInstanceState(): Parcelable? =
        SavedState(super.onSaveInstanceState()).apply {
            content = sudokuCellContent
        }

    override fun onRestoreInstanceState(state: Parcelable?) =
        when (state) {
            is SavedState -> {
                super.onRestoreInstanceState(state.superState)
                sudokuCellContent = state.content
            }
            else -> super.onRestoreInstanceState(state)
        }


    private var internalClipBounds = Rect()

    private fun evaluateMinimalContentDimensions(): Rect {
        val digits = "123456789"
        val singleDigitSize = digits
            .toCharArray()
            .map { digit ->
                val rect = Rect()
                solutionNumberPaint.getTextBounds(digit.toString(), 0, 1, rect)
                rect
            }
            .fold(Rect(), { acc, r ->
                Rect(
                    0,
                    0,
                    max(acc.width(), r.width()) + (2 * innerPadding),
                    max(acc.height(), r.height()) + (2 * innerPadding)
                )
            })
        val notesSize = digits
            .chunked(3)
            .map { row ->
                val rect = Rect()
                notedNumbersPaint.getTextBounds(row, 0, row.length, rect)
                rect
            }
            .fold(Rect(), { acc, rect -> acc.stackOnTop(rect) })
            .let { rect ->
                Rect(
                    0,
                    0,
                    rect.width() + (2 * innerPadding),
                    rect.height() + (2 * innerPadding)
                )
            }
        return singleDigitSize.mergeMax(notesSize)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)

        val height = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val textMeasuredSize = evaluateMinimalContentDimensions()

        val measuredWidth = textMeasuredSize.width() + paddingLeft + paddingRight
        val measuredHeight = textMeasuredSize.height() + paddingTop + paddingBottom

        val resultWidth = when (widthMode) {
            MeasureSpec.EXACTLY -> width
            MeasureSpec.AT_MOST -> min(width, measuredWidth)
            else -> measuredWidth
        }
        val resultHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> height
            MeasureSpec.AT_MOST -> min(height, measuredHeight)
            else -> measuredHeight
        }

        if (resultHeight < measuredHeight || resultWidth < measuredWidth) {
            Log.e(
                SudokuCellView::class.java.name,
                "View's height is smaller than it's required to draw the content. It might be cropped"
            )
        }

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            drawContent(it)
        }
        super.onDraw(canvas)
    }

    private fun drawContent(canvas: Canvas) {
        when (val c = sudokuCellContent) {
            is SudokuCellContent.Solution -> drawDigitContent(canvas, c)
            is SudokuCellContent.Note -> drawNoteContent(canvas, c)
        }
    }

    private fun drawDigitContent(canvas: Canvas, sudokuCellContent: SudokuCellContent.Solution) {
        val text = sudokuCellContent.data.toString()

        canvas.getClipBounds(internalClipBounds)

        val availableWidth = internalClipBounds.width() - paddingLeft - paddingRight
        val availableHeight = internalClipBounds.height() - paddingTop - paddingBottom
        val x = (availableWidth / 2F) + paddingLeft
        val y = paddingTop + ((availableHeight / 2F) - ((solutionNumberPaint.descent() +
                solutionNumberPaint.ascent()) / 2F))

        canvas.drawText(text, x, y, solutionNumberPaint)
    }

    private fun drawNoteContent(canvas: Canvas, sudokuCellContent: SudokuCellContent.Note) {
        canvas.getClipBounds(internalClipBounds)

        val availableWidth = internalClipBounds.width() - paddingLeft - paddingRight
        val availableHeight = internalClipBounds.height() - paddingTop - paddingBottom

        val horizontalSpacing = availableWidth / 3F
        val verticalSpacing = availableHeight / 3F

        (0 until 3)
            .map { step -> horizontalSpacing / 2F + step * horizontalSpacing }
            .map { it + paddingLeft }
            .flatMap { x ->
                (0 until 3)
                    .map { step -> verticalSpacing / 2F + step * verticalSpacing }
                    .map { it + paddingTop }
                    .map { coordinate -> coordinate - ((notedNumbersPaint.descent() + notedNumbersPaint.ascent()) / 2F) }
                    .map { x to it }
            }
            .withIndex()
            .filter { sudokuCellContent.data.contains(it.index) }
            .forEach {
                val text = it.index.toString()
                val x = it.value.first
                val y = it.value.second
                canvas.drawText(
                    text,
                    x,
                    y,
                    notedNumbersPaint
                )
            }
    }

    internal class SavedState : BaseSavedState {
        var content: SudokuCellContent? = null

        constructor(source: Parcel?) : super(source) {
            content = source
                ?.readParcelable<SudokuCellContent?>(SudokuCellContent::class.java.classLoader)
        }

        constructor(superState: Parcelable?) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeParcelable(content, flags)
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel) = SavedState(source)

                override fun newArray(size: Int) = arrayOfNulls<SavedState>(size)
            }
        }
    }

}

private fun Rect.mergeMax(other: Rect): Rect =
    Rect(0, 0, max(width(), other.width()), max(height(), other.height()))

private fun Rect.stackOnTop(other: Rect): Rect =
    Rect(0, 0, max(width(), other.width()), height() + other.height())