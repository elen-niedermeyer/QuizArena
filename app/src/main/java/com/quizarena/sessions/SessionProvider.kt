package com.quizarena.sessions

import android.content.Context
import com.quizarena.user.CurrentUser

class SessionProvider(val context: Context) {

    /**
     * list of current sessions
     * Getter makes a request if the field isn't already set.
     */
    private var allSessions: List<QuizSession>? = null
        get() {
            if (field == null) {
                allSessions = SessionApi(context).getSessions(CurrentUser.accountName)
            }

            return field
        }

    /**
     * Provides a list of all sessions of which the logged in user is owner.
     * @return the list or null
     */
    private fun getOwnedSessions(): List<QuizSession>? {
        if (allSessions == null) {
            allSessions = SessionApi(context).getSessions(CurrentUser.accountName)
        }

        if (allSessions != null) {
            val copyAllSessions = allSessions
            return copyAllSessions!!.filter { it.isOwner }
        } else {
            return null
        }
    }

    /**
     * Sorts the list of {@link #getOwnedSessions} by end dates of the sessions.
     * @return the sorted list or null
     */
    private fun getOwnedSessionsSorted(): List<QuizSession>? {
        val sessions = getOwnedSessions()
        if (sessions != null) {
            return sessions.sortedWith(compareBy { it.enddate })
        } else {
            return null
        }
    }

    /**
     * Provides a list of all sessions of which the logged in user is participant and not owner.
     * @return the list or null
     */
    private fun getParticipatedSessions(): List<QuizSession>? {
        if (allSessions == null) {
            allSessions = SessionApi(context).getSessions(CurrentUser.accountName)
        }

        if (allSessions != null) {
            val copyAllSessions = allSessions
            return copyAllSessions!!.filter { it.isParticipant && !it.isOwner }
        } else {
            return null
        }
    }

    /**
     * Sorts the list of {@link #getParticipatedeSessions} by end dates of the sessions.
     * @return the sorted list or null
     */
    private fun getParticipatedSessionsSorted(): List<QuizSession>? {
        val sessions = getParticipatedSessions()
        if (sessions != null) {
            return sessions.sortedWith(compareBy { it.enddate })
        } else {
            return null
        }
    }

    /**
     * Provides a list of all open sessions. The current user is not owner nor participant of the session. The session is not private.
     * Exfiltrates terminated sessions. They aren't interesting for the user.
     * @return the list or null
     */
    private fun getOpenSessions(): List<QuizSession>? {
        if (allSessions == null) {
            allSessions = SessionApi(context).getSessions(CurrentUser.accountName)
        }

        if (allSessions != null) {
            val copyAllSessions = allSessions
            return copyAllSessions!!.filter { !it.isOwner && !it.isParticipant && !it.isPrivate && SessionUtils.isSessionActive(it) }
        } else {
            return null
        }
    }

    /**
     * Sorts the list of {@link #getOpenSessions} by end dates of the sessions.
     * @return the sorted list or null
     */
    private fun getOpenSessionsSorted(): List<QuizSession>? {
        val sessions = getOpenSessions()
        if (sessions != null) {
            return sessions.filter { it.enddate > System.currentTimeMillis() }.sortedWith(compareBy { it.enddate })
        } else {
            return null
        }
    }

    /**
     * Provides a list of all private sessions. The current user is not owner nor participant of the session.
     * Exfiltrates terminated sessions. They aren't interesting for the user.
     * @return the list or null
     */
    private fun getPrivateSessions(): List<QuizSession>? {
        if (allSessions == null) {
            allSessions = SessionApi(context).getSessions(CurrentUser.accountName)
        }

        if (allSessions != null) {
            val copyAllSessions = allSessions
            return copyAllSessions!!.filter { it.isPrivate && !it.isOwner && !it.isParticipant && SessionUtils.isSessionActive(it) }
        } else {
            return null
        }
    }

    /**
     * Sorts the list of {@link #getPrivateSessions} by end dates of the sessions.
     * @return the sorted list or null
     */
    private fun getPrivateSessionsSorted(): List<QuizSession>? {
        val sessions = getPrivateSessions()
        if (sessions != null) {
            return sessions.filter { it.enddate > System.currentTimeMillis() }.sortedWith(compareBy { it.enddate })
        } else {
            return null
        }
    }

    /**
     * Adds the lists of {@link #getOwnedSessionsSorted}, {@link #getParticipatedSessionsSorted},
     * {@link #getOpenSessionsSorted} and {@link #getPrivateSessionsSorted} to one list.
     * @return the sorted list of all sessions
     */
    fun getAllSessionSorted(): List<QuizSession>? {
        val ownedSessions = getOwnedSessionsSorted()
        val participatedSessions = getParticipatedSessionsSorted()
        val openSessions = getOpenSessionsSorted()
        val privateSessions = getPrivateSessionsSorted()
        if (ownedSessions != null && participatedSessions != null && openSessions != null && privateSessions != null) {
            return ownedSessions + participatedSessions + openSessions + privateSessions
        } else {
            return null
        }
    }

}