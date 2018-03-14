package com.quizarena.sessions

import java.util.*

// TODO: correct implementation with backend including errors
class SessionApi {

    // mock up
    fun requestAllSessions(): List<QuizSession> {
        val sessions = LinkedList<QuizSession>()

        val now = Date()
        sessions.add(QuizSession("Test1", Date(now.time + (2 * 60 * 60 * 1000)), "default", true, true))
        sessions.add(QuizSession("Test2", Date(now.time + (2 * 60 * 1000)), "default", false, false))
        sessions.add(QuizSession("Test3", Date(now.time - (2 * 60 * 60 * 1000)), "default", false, true))

        return sessions
    }
}