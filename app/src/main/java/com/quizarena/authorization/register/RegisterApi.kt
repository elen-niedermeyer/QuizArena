package com.quizarena.authorization.register

import android.content.Context
import com.quizarena.R
import org.jetbrains.anko.doAsync

class RegisterApi {

    var state: String = ""
        private set

    // TODO: handle responses (errors, id not unique, success --> go to main menu)
    // TODO: set users display name
    /*fun executeRegisterRequest(context: Context, name: String, password: String, displayName: String) : Boolean {
        doAsync {
            val response = khttp.post(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.user_endpoint) + name,
                    data = mapOf(context.getString(R.string.api_parameter_password) to password))
        }
    }*/

    // Mocking Request for easier debugging
    fun executeRegisterRequest(context: Context, name: String, password: String, displayName: String): Boolean {
        if (name == "Test") {
            return true
        } else {
            state = "Ein Fehler ist aufgetreten"
            return false
        }
    }
}

