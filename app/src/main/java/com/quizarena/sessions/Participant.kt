package com.quizarena.sessions

data class Participant(val accountName: String, val displayName: String, val sessionScore: Int) {
    var ranking = 0
}