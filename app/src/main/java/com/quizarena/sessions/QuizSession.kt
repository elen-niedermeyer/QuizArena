package com.quizarena.sessions

import android.os.Parcel
import android.os.Parcelable
import java.util.*

// TODO: look if the time is correct in my time zone
data class QuizSession(var name: String, var category: String, var enddate: Date, var isOwner: Boolean, var isParticipant: Boolean, var isPrivate: Boolean) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readSerializable() as Date,
            1 == source.readInt(),
            1 == source.readInt(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(category)
        writeSerializable(enddate)
        writeInt((if (isOwner) 1 else 0))
        writeInt((if (isParticipant) 1 else 0))
        writeInt((if (isPrivate) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<QuizSession> = object : Parcelable.Creator<QuizSession> {
            override fun createFromParcel(source: Parcel): QuizSession = QuizSession(source)
            override fun newArray(size: Int): Array<QuizSession?> = arrayOfNulls(size)
        }
    }
}