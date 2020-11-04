package com.seemnerdy.sudokusolver.app.view

import android.os.Parcel
import android.os.Parcelable

sealed class SudokuCellContent : Parcelable {

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        val type = when (this) {
            is Note -> ParcelType.Note
            is Solution -> ParcelType.Solution
        }
        dest.writeType(type)
    }

    data class Note(val data: IntArray) : SudokuCellContent() {

        internal constructor(parcel: Parcel) : this(data = intArrayOf().also {
            parcel.readIntArray(
                it
            )
        })

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Note

            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun writeToParcel(parcel: Parcel, flags: kotlin.Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeIntArray(data)
        }

        override fun hashCode() = data.contentHashCode()
    }

    data class Solution(val data: Int) : SudokuCellContent() {

        internal constructor(parcel: Parcel) : this(data = parcel.readInt())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeInt(data)
        }
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<SudokuCellContent> {
            override fun createFromParcel(parcel: Parcel) = when (parcel.readType()) {
                ParcelType.Note -> Note(parcel)
                ParcelType.Solution -> Solution(parcel)
            }

            override fun newArray(size: Int) = arrayOfNulls<SudokuCellContent>(size)
        }
    }
}

private enum class ParcelType {
    Note, Solution
}

private fun Parcel.readType() = readInt().let { ParcelType.values()[it] }
private fun Parcel.writeType(type: ParcelType) = writeInt(type.ordinal)
