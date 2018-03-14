package com.quizarena.sessions

import java.util.*

class SessionProvider {

    var allSessions: List<QuizSession>? = null

    fun getOwnedSessions(): List<QuizSession> {
        if (allSessions == null) {
            allSessions = SessionApi().requestAllSessions();
        }

        val copyAllSessions = allSessions
        return copyAllSessions!!.filter { it.isOwner }
    }

    fun getParticipatedSessions(): List<QuizSession> {
        if (allSessions == null) {
            allSessions = SessionApi().requestAllSessions();
        }

        val copyAllSessions = allSessions
        return copyAllSessions!!.filter { it.isParticipant && !it.isOwner }
    }

    fun getOpenSessions(): List<QuizSession> {
        if (allSessions == null) {
            allSessions = SessionApi().requestAllSessions();
        }

        val copyAllSessions = allSessions
        return copyAllSessions!!.filter { !it.isParticipant && !it.isOwner }
    }
}