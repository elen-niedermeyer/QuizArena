package com.quizarena.sessions

import android.os.Parcel
import android.os.Parcelable
import java.util.*

// TODO: look if the time is correct in my time zone
class QuizSession(var name: String, var category: String, var enddate: Date, var isOwner: Boolean, var isParticipant: Boolean, var isPrivate: Boolean) : Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.setDataPosition(0)
        dest?.writeString(name)
        dest?.writeString(category)
        dest?.writeLong(enddate.time)
        dest?.writeBoolean(isOwner)
        dest?.writeBoolean(isParticipant)
        dest?.writeBoolean(isPrivate)
    }

    val CREATOR: Parcelable.Creator<QuizSession> = object : Parcelable.Creator<QuizSession> {
        override fun createFromParcel(source: Parcel): QuizSession {
            source.setDataPosition(0)
            return QuizSession(source.readString(),
                    source.readString(),
                    Date(source.readLong()),
                    source.readBoolean()!!,
                    source.readBoolean()!!,
                    source.readBoolean()!!)
        }

        override fun newArray(size: Int): Array<QuizSession?> {
            return arrayOfNulls<QuizSession>(size)
        }
    }

    private fun Parcel.writeBoolean(flag: Boolean?) {
        when (flag) {
            true -> writeInt(1)
            false -> writeInt(0)
            else -> writeInt(-1)
        }
    }

    private fun Parcel.readBoolean(): Boolean? {
        return when (readInt()) {
            1 -> true
            0 -> false
            else -> null
        }
    }

}