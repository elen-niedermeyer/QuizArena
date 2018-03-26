package com.quizarena.sessions

import java.util.*

// TODO: correct implementation with backend including errors
class SessionApi {

    // mock up
    fun requestAllSessions(): List<QuizSession> {
        val sessions = LinkedList<QuizSession>()

        val now = Date()
        sessions.add(QuizSession("Test1",  "default", Date(now.time + (4 * 60 * 60 * 1000)),true, true, false))
        sessions.add(QuizSession("Test2",  "default", Date(now.time + (2 * 60 * 1000)),false, false, false))
        sessions.add(QuizSession("Test3",  "default", Date(now.time - (2 * 60 * 60 * 1000)),false, true, true))
        sessions.add(QuizSession("Test4",  "default", Date(now.time + (2 * 60 * 60 * 1000)),true, true, true))
        sessions.add(QuizSession("Test5",  "default", Date(now.time + (3 * 60 * 60 * 1000)),false, false, true))

        return sessions
    }
}