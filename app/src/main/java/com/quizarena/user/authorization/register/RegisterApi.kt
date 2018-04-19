package com.quizarena.user.authorization.register

import android.content.Context
import com.quizarena.ApiErrors
import com.quizarena.R
import org.jetbrains.anko.doAsync

class RegisterApi(val context: Context) {

    var state: String = ""
        private set

    // TODO: register with displayName
    // TODO: rules for passwords
    // Mocking Request for easier debugging
    fun executeRegisterRequest(name: String, displayName: String, password: String, token: String?): Boolean {
        var statusCode = 0
        var responseState = ""

        doAsync {
            val response = khttp.post(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_user_specific, name),
                    data = mapOf(context.getString(R.string.endpoint_user_param_password) to password, context.getString(R.string.endpoint_user_param_token) to token))
            statusCode = response.statusCode
            responseState = response.text
        }

        if (statusCode == 200) {
            // register was successful
            return true

        } else {
            // register was not successful
            // parse error
            this.state = ApiErrors.getRegisterError(context, responseState)
            return false
        }
    }
}
