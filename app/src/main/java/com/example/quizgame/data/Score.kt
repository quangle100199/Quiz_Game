package com.example.quizgame.data

import android.os.Parcel
import android.os.Parcelable

data class Score(val incorrectNum : Int, val correctNum : Int) : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(incorrectNum)
        parcel.writeInt(correctNum)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Score> {
        override fun createFromParcel(p0: Parcel): Score {
            return Score(p0)
        }

        override fun newArray(p0: Int): Array<Score?> {
            return arrayOfNulls(p0)
        }
    }
}