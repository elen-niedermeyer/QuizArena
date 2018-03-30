package com.quizarena.authorization

// TODO: initialize it after login, not used yet
class User(name: String, displayName: String, score: Int) {

    init {
        User.name = name
        User.displayName = displayName
        User.score = score
    }

    companion object {
        var name: String = ""
        var displayName: String = ""
        var score: Int = 0
    }
}
