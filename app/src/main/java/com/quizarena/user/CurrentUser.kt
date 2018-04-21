package com.quizarena.user

class CurrentUser(accountName: String) {

    var accountName = accountName
        private set
    var isLoggedIn = false
    var displayName = ""
    var globalScore = -1

    /**
     * set the static values
     */
    init {
        CurrentUser.accountName = accountName
        CurrentUser.isLoggedIn = isLoggedIn
        CurrentUser.displayName = displayName
        CurrentUser.globalScore = globalScore
    }

    /**
     * static values
     */
    companion object {
        var accountName = ""
        var isLoggedIn = false
        var displayName = ""
        var globalScore = -1
    }

}