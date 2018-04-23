package com.quizarena.sessions

import android.content.Context
import com.quizarena.R
import org.jetbrains.anko.doAsyncResult
import java.util.*

// TODO: correct implementation with backend including errors
class SessionApi(val context: Context) {

    var state: String = ""
        private set

    /**
     * Requests the sessions for the given user.
     *
     * @param accountName the user's name
     * @return a list of {@link QuizSession} that are relevant for the user
     */
    fun getSessions(accountName: String): List<QuizSession>? {
        val response = doAsyncResult {
            return@doAsyncResult khttp.get(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_session_user, accountName)
            )
        }.get()

        val statusCode = response.statusCode
        val responseState = response.text

        if (statusCode == 200) {
            // session request was successful
            // make list of session objects
            val sessions = LinkedList<QuizSession>()
            val json = response.jsonObject
            val participatedSessions = json.getJSONArray("sessions_i_participate")
            for (i in 0..participatedSessions.length() - 1) {
                val sessionJson = participatedSessions.getJSONObject(i)
                sessions.add(QuizSession(
                        sessionJson.getString("_id"),
                        sessionJson.getString("name"),
                        sessionJson.getString("category"),
                        Date(sessionJson.getString("deadline").toLong()),
                        sessionJson.getString("admin") == accountName,
                        true,
                        sessionJson.getBoolean("private")
                ))
            }

            val openSessions = json.getJSONArray("sessions_to_participate")
            for (i in 0..openSessions.length() - 1) {
                val sessionJson = openSessions.getJSONObject(i)
                sessions.add(QuizSession(
                        sessionJson.getString("_id"),
                        sessionJson.getString("name"),
                        sessionJson.getString("category"),
                        Date(sessionJson.getString("deadline").toLong()),
                        false,
                        false,
                        sessionJson.getBoolean("private")
                ))
            }

            return sessions

        } else {
            // session request failed
            this.state = responseState
            return null
        }
    }

    /**
     * Requests one session.
     *
     * @return a {@link QuizSession} object
     */
    fun getSession(sessionID: String, accountName: String): QuizSession? {
        val response = doAsyncResult {
            return@doAsyncResult khttp.get(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_session_specific, sessionID))
        }.get()

        val statusCode = response.statusCode
        val responseState = response.text

        if (statusCode == 200) {
            // request was successful
            // make session object
            val json = response.jsonObject
            // look if user participates
            var isParticipant = false
            val participantsJson = json.getJSONArray("users")
            for (i in 0..participantsJson.length() - 1) {
                val participantJson = participantsJson.getJSONObject(i)
                if (participantJson.getString("user") == accountName) {
                    isParticipant = true
                    break
                }
            }

            return QuizSession(
                    json.getString("_id"),
                    json.getString("name"),
                    json.getString("category"),
                    Date(json.getString("deadline").toLong()),
                    json.getString("admin") == accountName,
                    isParticipant,
                    json.getBoolean("private")
            )

        } else {
            // request failed
            this.state = responseState
            return null
        }
    }

    /**
     * Requests the given session and extracts the participants
     *
     * @return a list of {@link Participant}
     */
    fun getParticipants(sessionID: String): List<Participant>? {
        val response = doAsyncResult {
            return@doAsyncResult khttp.get(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_session_specific, sessionID))
        }.get()

        val statusCode = response.statusCode
        val responseState = response.text

        if (statusCode == 200) {
            // request was successful
            // make list of participants
            val participants = ArrayList<Participant>()
            val json = response.jsonObject
            val participantsJson = json.getJSONArray("users")
            for (i in 0..participantsJson.length() - 1) {
                val participantJson = participantsJson.getJSONObject(i)
                // ToDo: get display name
                participants.add(Participant(participantJson.getString("user"), "", participantJson.getInt("score")))
            }
            return participants

        } else {
            // request failed
            this.state = responseState
            return null
        }

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