package com.quizarena.user

class User(accountName: String) {

    var accountName = accountName
        private set
    var isLoggedIn = false
    var globalScore = 0

    /**
     * set the static account name
     */
    init {
        User.accountName = accountName
        User.isLoggedIn = isLoggedIn
        User.globalScore = globalScore
    }

    /**
     * account name as static value
     */
    companion object {
        var accountName = ""
        var isLoggedIn = false
        var globalScore = 0
    }

}