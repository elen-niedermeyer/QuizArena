package com.quizarena.sessions

import android.content.Context
import com.quizarena.R
import com.quizarena.user.CurrentUser
import org.jetbrains.anko.doAsyncResult
import java.util.*

// TODO: correct implementation with backend including errors
class SessionApi(val context: Context) {

    // mock up
    fun requestAllSessions(username: String): List<QuizSession> {
        val sessions = LinkedList<QuizSession>()

        val now = Date()
        sessions.add(QuizSession("1", "Test1", "default", Date(now.time + (4 * 60 * 60 * 1000)), true, true, false))
        sessions.add(QuizSession("2", "Test2", "default", Date(now.time + (2 * 60 * 1000)), false, false, false))
        sessions.add(QuizSession("3", "Test3", "default", Date(now.time - (2 * 60 * 60 * 1000)), false, true, true))
        sessions.add(QuizSession("4", "Test4", "default", Date(now.time + (2 * 60 * 60 * 1000)), true, true, true))
        sessions.add(QuizSession("5", "Test5", "default", Date(now.time + (3 * 60 * 60 * 1000)), false, false, true))
        sessions.add(QuizSession("6", "Test6", "default", Date(now.time - (2 * 60 * 60 * 1000)), false, false, false))

        return sessions
    }

    // mock up
    fun getSession(sessionID: String, username: String): QuizSession {
        return QuizSession(sessionID, "Test", "category", Date(System.currentTimeMillis()), true, true, true)
    }

    // mock up
    fun terminateSession(sessionID: String): Boolean {
        return true
    }

    // mockup
    fun getParticipants(sessionID: String): List<Participant> {
        val participants = ArrayList<Participant>()
        participants.add(Participant("Test", "Test", 10))
        participants.add(Participant("User2", "User2", 5))

        return participants
    }

    // mock up
    fun addParticipant(sessionID: String, username: String): Boolean {
        return true
    }

    // mock up
    fun setScore(username: String, score: Int): Boolean {
        return true
    }

    /**
     * Execute POST request to create a new public session
     */
    fun createPublicSession (sessionName: String, category: String, duration: Int, user: String) {
        val response = doAsyncResult {
            return@doAsyncResult khttp.post(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_sessions_creation, sessionName),
                    data = mapOf(
                            "category" to category,
                            "private" to false,
                            "run-time" to duration,
                            "user" to user
                    )
            )
        }.get()

        val statusCode = response.statusCode
        val responseState = response.text

        if (statusCode == 200) {
            // session creation request was successful
        } else {
            // session creation request failed
        }
    }

    fun createPrivateSession (sessionName: String, category: String, duration: Int, user: String, password: String) {
        val response = doAsyncResult {
            return@doAsyncResult khttp.post(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_sessions_creation, sessionName),
                    data = mapOf(
                            "category" to category,
                            "private" to true,
                            "password" to password,
                            "run-time" to duration,
                            "user" to user
                    )
            )
        }.get()
    }
}