package com.quizarena.user

class User(accountName: String) {

    var accountName = accountName
        private set
    var isLoggedIn = false
    var displayName = ""
    var globalScore = 0

    /**
     * set the static values
     */
    init {
        User.accountName = accountName
        User.isLoggedIn = isLoggedIn
        User.displayName = displayName
        User.globalScore = globalScore
    }

    /**
     * static values
     */
    companion object {
        var accountName = ""
        var isLoggedIn = false
        var displayName = ""
        var globalScore = 0
    }

}