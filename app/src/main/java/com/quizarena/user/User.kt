package com.quizarena.user

class User(accountName: String) {

    var accountName = accountName
        private set
    var isLoggedIn = false
    var globalScore = 0

    /**
     * set the static values
     */
    init {
        User.accountName = accountName
        User.isLoggedIn = isLoggedIn
        User.globalScore = globalScore
    }

    /**
     * static values
     */
    companion object {
        var accountName = ""
        var isLoggedIn = false
        var globalScore = 0
    }

}