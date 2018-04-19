package com.quizarena.sessions

import com.quizarena.user.authorization.Credentials

class SessionProvider {

    /**
     * list of current sessions
     * Getter makes a request if the field isn't already set.
     */
    var allSessions: List<QuizSession>? = null
        get() {
            if (field == null) {
                allSessions = SessionApi().requestAllSessions(Credentials.accountName)
            }

            return field
        }

    /**
     * Provides a list of all sessions of which the logged in user is owner.
     * @return the list
     */
    fun getOwnedSessions(): List<QuizSession> {
        if (allSessions == null) {
            allSessions = SessionApi().requestAllSessions(Credentials.accountName)
        }

        val copyAllSessions = allSessions
        return copyAllSessions!!.filter { it.isOwner }
    }

    /**
     * Sorts the list of {@link #getOwnedSessions} by end dates of the sessions.
     * @return the sorted list
     */
    fun getOwnedSessionsSorted(): List<QuizSession> {
        return getOwnedSessions().sortedWith(compareBy { it.enddate })
    }

    /**
     * Provides a list of all sessions of which the logged in user is participant and not owner.
     * @return the list
     */
    fun getParticipatedSessions(): List<QuizSession> {
        if (allSessions == null) {
            allSessions = SessionApi().requestAllSessions(Credentials.accountName)
        }

        val copyAllSessions = allSessions
        return copyAllSessions!!.filter { it.isParticipant && !it.isOwner }
    }

    /**
     * Sorts the list of {@link #getParticipatedeSessions} by end dates of the sessions.
     * @return the sorted list
     */
    fun getParticipatedSessionsSorted(): List<QuizSession> {
        return getParticipatedSessions().sortedWith(compareBy { it.enddate })
    }

    /**
     * Provides a list of all open sessions. The current user is not owner nor participant of the session. The session is not private.
     * Exfiltrates terminated sessions. They aren't interesting for the user.
     * @return the list
     */
    fun getOpenSessions(): List<QuizSession> {
        if (allSessions == null) {
            allSessions = SessionApi().requestAllSessions(Credentials.accountName)
        }

        val copyAllSessions = allSessions
        return copyAllSessions!!.filter { !it.isOwner && !it.isParticipant && !it.isPrivate && SessionUtils.isSessionActive(it) }
    }

    /**
     * Sorts the list of {@link #getOpenSessions} by end dates of the sessions.
     * @return the sorted list
     */
    fun getOpenSessionsSorted(): List<QuizSession> {
        return getOpenSessions().filter { it.enddate.time > System.currentTimeMillis() }.sortedWith(compareBy { it.enddate })
    }

    /**
     * Provides a list of all private sessions. The current user is not owner nor participant of the session.
     * Exfiltrates terminated sessions. They aren't interesting for the user.
     * @return the list
     */
    fun getPrivateSessions(): List<QuizSession> {
        if (allSessions == null) {
            allSessions = SessionApi().requestAllSessions(Credentials.accountName)
        }

        val copyAllSessions = allSessions
        return copyAllSessions!!.filter { it.isPrivate && !it.isOwner && !it.isParticipant && SessionUtils.isSessionActive(it) }
    }

    /**
     * Sorts the list of {@link #getPrivateSessions} by end dates of the sessions.
     * @return the sorted list
     */
    fun getPrivateSessionsSorted(): List<QuizSession> {
        return getPrivateSessions().filter { it.enddate.time > System.currentTimeMillis() }.sortedWith(compareBy { it.enddate })
    }

    /**
     * Adds the lists of {@link #getOwnedSessionsSorted}, {@link #getParticipatedSessionsSorted},
     * {@link #getOpenSessionsSorted} and {@link #getPrivateSessionsSorted} to one list.
     * @return the sorted list of all sessions
     */
    fun getAllSessionSorted(): List<QuizSession> {
        return getOwnedSessionsSorted() + getParticipatedSessionsSorted() + getOpenSessionsSorted() + getPrivateSessionsSorted()
    }

}