package com.quizarena.sessions

import android.content.Context
import com.quizarena.ApiErrors
import com.quizarena.R
import com.quizarena.user.UserApi
import org.jetbrains.anko.doAsyncResult
import java.net.UnknownHostException
import java.util.*

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
        try {
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
                    // get end date
                    var endDate = Date(System.currentTimeMillis() - 1)
                    if (!sessionJson.getBoolean("closed")) {
                        endDate = Date(sessionJson.getString("deadline").toLong())
                    }
                    // add session
                    sessions.add(QuizSession(
                            sessionJson.getString("_id"),
                            sessionJson.getString("name"),
                            sessionJson.getString("category"),
                            endDate,
                            sessionJson.getString("admin") == accountName,
                            true,
                            sessionJson.getBoolean("private")
                    ))
                }

                val openSessions = json.getJSONArray("sessions_to_participate")
                for (i in 0..openSessions.length() - 1) {
                    val sessionJson = openSessions.getJSONObject(i)
                    // get end date
                    var endDate = Date(System.currentTimeMillis() - 1)
                    if (!sessionJson.getBoolean("closed")) {
                        endDate = Date(sessionJson.getString("deadline").toLong())
                    }
                    // add session
                    sessions.add(QuizSession(
                            sessionJson.getString("_id"),
                            sessionJson.getString("name"),
                            sessionJson.getString("category"),
                            endDate,
                            false,
                            false,
                            sessionJson.getBoolean("private")
                    ))
                }

                return sessions

            } else {
                // session request failed
                this.state = ApiErrors.getError(context, responseState)
                return null
            }

        } catch (e: Exception) {
            if (e.cause is UnknownHostException) {
                state = context.getString(R.string.error_network)
            } else {
                this.state = ApiErrors.getError(context, e.message!!)
            }
            return null
        }
    }

    /**
     * Requests one session.
     *
     * @return a {@link QuizSession} object
     */
    fun getSession(sessionID: String, accountName: String): QuizSession? {
        try {
            val response = doAsyncResult {
                return@doAsyncResult khttp.get(
                        url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_session_specific, sessionID))
            }.get()

            val statusCode = response.statusCode
            val responseState = response.text

            if (statusCode == 200) {
                // request was successful
                // make session object
                val json = response.jsonArray
                val jsonObject = json.getJSONObject(0)
                // look if user participates
                var isParticipant = false
                val participantsJson = jsonObject.getJSONArray("users")
                for (i in 0..participantsJson.length() - 1) {
                    val participantJson = participantsJson.getJSONObject(i)
                    if (participantJson.getString("user") == accountName) {
                        isParticipant = true
                        break
                    }
                }
                // get end date
                var endDate = Date(System.currentTimeMillis() - 1)
                if (!jsonObject.getBoolean("closed")) {
                    endDate = Date(jsonObject.getString("deadline").toLong())
                }

                return QuizSession(
                        jsonObject.getString("_id"),
                        jsonObject.getString("name"),
                        jsonObject.getString("category"),
                        endDate,
                        jsonObject.getString("admin") == accountName,
                        isParticipant,
                        jsonObject.getBoolean("private")
                )

            } else {
                // request failed
                this.state = ApiErrors.getError(context, responseState)
                return null
            }

        } catch (e: Exception) {
            if (e.cause is UnknownHostException) {
                state = context.getString(R.string.error_network)
            } else {
                this.state = ApiErrors.getError(context, e.message!!)
            }
            return null
        }
    }

    /**
     * Requests the given session and extracts the participants
     *
     * @return a list of {@link Participant}
     */
    fun getParticipants(sessionID: String): List<Participant>? {
        try {
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
                val json = response.jsonArray
                val jsonObject = json.getJSONObject(0)
                val participantsJson = jsonObject.getJSONArray("users")
                val userApi = UserApi(context)
                for (i in 0..participantsJson.length() - 1) {
                    val participantJson = participantsJson.getJSONObject(i)
                    val username = participantJson.getString("user")
                    participants.add(Participant(username, userApi.getDisplayName(username), participantJson.getInt("score")))
                }
                return participants

            } else {
                // request failed
                this.state = ApiErrors.getError(context, responseState)
                return null
            }

        } catch (e: Exception) {
            if (e.cause is UnknownHostException) {
                state = context.getString(R.string.error_network)
            } else {
                this.state = ApiErrors.getError(context, e.message!!)
            }
            return null
        }
    }

    /**
     * Execute POST request to create a new session
     *
     * @return the id of the created session or null
     */
    fun createSession(sessionName: String, category: String, duration: Int, isPrivate: Boolean, password: String?, accountName: String): String? {
        try {
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
                // in this case the response state is the session id
                return responseState

            } else {
                // session creation request failed
                this.state = ApiErrors.getError(context, responseState)
                return null
            }

        } catch (e: Exception) {
            if (e.cause is UnknownHostException) {
                state = context.getString(R.string.error_network)
            } else {
                this.state = ApiErrors.getError(context, e.message!!)
            }
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
        try {
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
                this.state = ApiErrors.getError(context, responseState)
                return false
            }

        } catch (e: Exception) {
            if (e.cause is UnknownHostException) {
                state = context.getString(R.string.error_network)
            } else {
                this.state = ApiErrors.getError(context, e.message!!)
            }
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
        try {
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
                this.state = ApiErrors.getError(context, responseState)
                return false
            }

        } catch (e: Exception) {
            if (e.cause is UnknownHostException) {
                state = context.getString(R.string.error_network)
            } else {
                this.state = ApiErrors.getError(context, e.message!!)
            }
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
        try {
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
                this.state = ApiErrors.getError(context, responseState)
                return false
            }

        } catch (e: Exception) {
            if (e.cause is UnknownHostException) {
                state = context.getString(R.string.error_network)
            } else {
                this.state = ApiErrors.getError(context, e.message!!)
            }
            return false
        }
    }

}