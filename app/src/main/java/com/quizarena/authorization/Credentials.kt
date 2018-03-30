package com.quizarena.authorization

class Credentials(accountName: String, password: String) {

    var accountName = accountName
        private set
    var password = password
        private set

    init {
        Credentials.accountName = accountName
    }

    companion object {
        var accountName = ""
    }
    
}