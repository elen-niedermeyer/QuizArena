package com.quizarena.user.authorization.login

import android.content.Context

class LoginApi {

    var state: String = ""
        private set

    // mockup function
    fun executeLoginRequest(context: Context, name: String, password: String): Boolean {
        if (name == "Test") {
            return true
        } else {
            state = "Es ist ein Fehler aufgetreten"
            return false
        }
    }

}