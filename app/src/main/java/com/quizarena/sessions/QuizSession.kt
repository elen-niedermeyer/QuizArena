package com.quizarena.sessions

import android.os.Parcel
import android.os.Parcelable

data class QuizSession(val id: String, val name: String, val category: String, val enddate: Long, val isOwner: Boolean, val isParticipant: Boolean, val isPrivate: Boolean) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readLong(),
            1 == source.readInt(),
            1 == source.readInt(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeString(category)
        writeLong(enddate)
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