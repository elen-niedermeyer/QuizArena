package com.quizarena.sessions

import android.content.Context
import com.quizarena.R
import org.jetbrains.anko.doAsyncResult
import java.util.*

// TODO: correct implementation with backend including errors
class SessionApi(val context: Context) {

    var state: String = ""
        private set

    // mock up
    fun requestAllSessions(username: String): List<QuizSession> {
        val sessions = LinkedList<QuizSession>()

        val now = Date()
        sessions.add(QuizSession("5aa94ad88c99510e90812f71", "Test1", "default", Date(now.time + (4 * 60 * 60 * 1000)), true, true, false))
        sessions.add(QuizSession("5aa94ad88c99510e90812f71", "Test2", "default", Date(now.time + (2 * 60 * 1000)), false, false, false))
        sessions.add(QuizSession("3", "Test3", "default", Date(now.time - (2 * 60 * 60 * 1000)), false, true, true))
        sessions.add(QuizSession("4", "Test4", "default", Date(now.time + (2 * 60 * 60 * 1000)), true, true, true))
        sessions.add(QuizSession("5", "Test5", "default", Date(now.time + (3 * 60 * 60 * 1000)), false, false, true))
        sessions.add(QuizSession("6", "Test6", "default", Date(now.time - (2 * 60 * 60 * 1000)), false, false, false))

        return sessions
    }

    // mock up
    fun getSession(sessionID: String, username: String): QuizSession {
        return QuizSession("5aa94ad88c99510e90812f71", "Test", "category", Date(System.currentTimeMillis()), true, true, true)
    }

    /**
     * Execute POST request to create a new session
     *
     * @return the id of the created session or null
     */
    fun createSession(sessionName: String, category: String, duration: Int, isPrivate: Boolean, password: String?, accountName: String): String? {
        val response = doAsyncResult {
            return@doAsyncResult khttp.post(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_sessions_creation, sessionName),
                    data = mapOf(
                            "category" to category,
                            "run-time" to duration,
                            "private" to isPrivate,
                            "password" to password,
                            "user" to accountName
                    )
            )
        }.get()

        val statusCode = response.statusCode
        val responseState = response.text

        if (statusCode == 200) {
            // session creation request was successful
            // TODO extract session id
            return "1"

        } else {
            // session creation request failed
            this.state = responseState
            return null
        }
    }


    // mockup
    fun getParticipants(sessionID: String): List<Participant> {
        val participants = ArrayList<Participant>()
        participants.add(Participant("Test", "Test", 10))
        participants.add(Participant("User2", "User2", 5))

        return participants
    }

    /**
     * Requests the given session and updates adds the user.
     *
     * @param sessionID session to update
     * @param accountName user's name
     * @param password session's password
     * @return true if the update was successful, false otherwise
     */
    fun addParticipant(sessionID: String, accountName: String, password: String?): Boolean {
        val response = doAsyncResult {
            return@doAsyncResult khttp.patch(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_session_add_user, sessionID),
                    data = mapOf(
                            "user" to accountName,
                            "password" to password
                    )
            )
        }.get()

        val statusCode = response.statusCode
        val responseState = response.text

        if (statusCode == 200) {
            // update request was successful
            return true

        } else {
            // update request failed
            this.state = responseState
            return false
        }
    }

    /**
     * Requests the given session and updates the score for the user.
     *
     * @param sessionID session to update
     * @param accountName user's name
     * @param score user's score
     * @return true if the update was successful, false otherwise
     */
    fun setScore(sessionID: String, accountName: String, score: Int): Boolean {
        val response = doAsyncResult {
            return@doAsyncResult khttp.patch(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_session_update_score, sessionID),
                    data = mapOf(
                            "user" to accountName,
                            "score" to score
                    )
            )
        }.get()

        val statusCode = response.statusCode
        val responseState = response.text

        if (statusCode == 200) {
            // update request was successful
            return true

        } else {
            // update request failed
            this.state = responseState
            return false
        }
    }

    /**
     * Requests the given session and terminates it.
     *
     * @param sessionID session to update
     * @param accountName user's name
     * @return true if the closing was successful, false otherwise
     */
    fun terminateSession(sessionID: String, accountName: String): Boolean {
        val response = doAsyncResult {
            return@doAsyncResult khttp.patch(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_session_terminate, sessionID),
                    data = mapOf(
                            "user" to accountName
                    )
            )
        }.get()

        val statusCode = response.statusCode
        val responseState = response.text

        if (statusCode == 200) {
            // update request was successful
            return true

        } else {
            // update request failed
            this.state = responseState
            return false
        }
    }


}