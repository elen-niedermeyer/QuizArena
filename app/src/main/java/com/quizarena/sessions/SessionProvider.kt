package com.quizarena.sessions

class SessionProvider {

    var allSessions: List<QuizSession>? = null
        get() {
            if (field == null) {
                allSessions = SessionApi().requestAllSessions();
            }

            return field
        }

    fun getOwnedSessions(): List<QuizSession> {
        if (allSessions == null) {
            allSessions = SessionApi().requestAllSessions();
        }

        val copyAllSessions = allSessions
        return copyAllSessions!!.filter { it.isOwner }
    }

    fun getOwnedSessionsSorted(): List<QuizSession> {
        return getOwnedSessions().sortedWith(compareBy { it.enddate })
    }

    fun getParticipatedSessions(): List<QuizSession> {
        if (allSessions == null) {
            allSessions = SessionApi().requestAllSessions();
        }

        val copyAllSessions = allSessions
        return copyAllSessions!!.filter { it.isParticipant && !it.isOwner }
    }

    fun getParticipatedSessionsSorted(): List<QuizSession> {
        return getParticipatedSessions().sortedWith(compareBy { it.enddate })
    }

    fun getOpenSessions(): List<QuizSession> {
        if (allSessions == null) {
            allSessions = SessionApi().requestAllSessions();
        }

        val copyAllSessions = allSessions
        return copyAllSessions!!.filter { !it.isParticipant && !it.isOwner }
    }

    fun getOpenSessionsSorted(): List<QuizSession> {
        return getOpenSessions().sortedWith(compareBy { it.enddate })
    }

    fun getAllSessionSorted(): List<QuizSession> {
        return getOwnedSessionsSorted() + getParticipatedSessionsSorted() + getOpenSessionsSorted()
    }

}