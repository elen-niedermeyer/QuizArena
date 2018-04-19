package com.quizarena.user.authorization

class Credentials(accountName: String, password: String) {

    var accountName = accountName
        private set
    var password = password
        private set

    /**
     * set the static account name
     */
    init {
        Credentials.accountName = accountName
    }

    /**
     * account name as static value
     */
    companion object {
        var accountName = ""
    }

}