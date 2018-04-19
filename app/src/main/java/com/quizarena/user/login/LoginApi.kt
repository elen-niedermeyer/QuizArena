package com.quizarena.user.login

import android.content.Context

class LoginApi {

    var state: String = ""
        private set

    // mockup function
    fun executeLoginRequest(context: Context, name: String): Boolean {
        if (name == "Test") {
            return true
        } else {
            state = "Es ist ein Fehler aufgetreten"
            return false
        }
    }

}