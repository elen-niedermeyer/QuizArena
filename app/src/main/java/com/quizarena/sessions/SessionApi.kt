package com.quizarena.sessions

import java.util.*

// TODO: correct implementation with backend including errors
class SessionApi {

    // mock up
    fun requestAllSessions(): List<QuizSession> {
        val sessions = LinkedList<QuizSession>()

        val now = Date()
        sessions.add(QuizSession(1, "Test1", "default", Date(now.time + (4 * 60 * 60 * 1000)), true, true, false))
        sessions.add(QuizSession(2, "Test2", "default", Date(now.time + (2 * 60 * 1000)), false, false, false))
        sessions.add(QuizSession(3, "Test3", "default", Date(now.time - (2 * 60 * 60 * 1000)), false, true, true))
        sessions.add(QuizSession(4, "Test4", "default", Date(now.time + (2 * 60 * 60 * 1000)), true, true, true))
        sessions.add(QuizSession(5, "Test5", "default", Date(now.time + (3 * 60 * 60 * 1000)), false, false, true))
        sessions.add(QuizSession(6, "Test6", "default", Date(now.time - (2 * 60 * 60 * 1000)), false, false, false))

        return sessions
    }

    // mock up
    fun addParticipant(username: String): Boolean {
        return true
    }
    
}