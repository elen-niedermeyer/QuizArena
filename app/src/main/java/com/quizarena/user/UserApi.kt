package com.quizarena.user

import android.content.Context
import com.quizarena.ApiErrors
import com.quizarena.R
import org.jetbrains.anko.doAsyncResult

class UserApi(val context: Context) {

    var state: String = ""
        private set

    fun authenticate(username: String, token: String?): Boolean {
        var statusCode = 0
        var responseState = ""

        val response = doAsyncResult {
            val response = khttp.get(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_user_token_authentication, username, token))
            return@doAsyncResult response
        }.get()

        statusCode = response.statusCode
        responseState = response.text

        if (statusCode == 200) {
            // authentication was successful
            return true

        } else {
            // authentication was not successful
            // parse error
            this.state = ApiErrors.getUserError(context, responseState)
            return false
        }
    }

    // TODO: rules for passwords?
    fun register(name: String, displayName: String, password: String, token: String?): Boolean {
        var statusCode = 0
        var responseState = ""

        val response = doAsyncResult {
            val response = khttp.post(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_user_specific, name),
                    data = mapOf(context.getString(R.string.endpoint_user_param_display_name) to displayName,
                            context.getString(R.string.endpoint_user_param_password) to password,
                            context.getString(R.string.endpoint_user_param_token) to token))
            return@doAsyncResult response
        }.get()

        statusCode = response.statusCode
        responseState = response.text

        if (statusCode == 200) {
            // register was successful
            return true

        } else {
            // register was not successful
            // parse error
            this.state = ApiErrors.getUserError(context, responseState)
            return false
        }
    }

    fun login(accountName: String, password: String, token: String?): Boolean {
        var statusCode = 0
        var responseState = ""

        val response = doAsyncResult {
            val response = khttp.patch(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_user_login, accountName),
                    data = mapOf(context.getString(R.string.endpoint_user_param_password) to password,
                            context.getString(R.string.endpoint_user_param_token) to token))
            return@doAsyncResult response
        }.get()

        statusCode = response.statusCode
        responseState = response.text

        if (statusCode == 200) {
            // register was successful
            return true

        } else {
            // register was not successful
            // parse error
            this.state = ApiErrors.getUserError(context, responseState)
            return false
        }
    }
}