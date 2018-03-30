package com.quizarena.authorization

// TODO: initialize it after login, not used yet
class User(name: String, displayName: String) {

    init {
        User.name = name
        User.displayName = displayName
    }

    companion object {
        var name: String = ""
        var displayName: String = ""
    }
}
