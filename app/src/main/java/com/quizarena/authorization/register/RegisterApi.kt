package com.quizarena.authorization.register

import android.content.Context
import android.provider.Settings.System.getString
import com.quizarena.R
import org.jetbrains.anko.doAsync


class RegisterApi {

    // TODO: handle responses (errors)
    // TODO: set users display name
    fun registerRequest(context: Context, name: String, password: String, displayName: String) {
        doAsync {
            val response = khttp.post(
                    url = context.getString(R.string.url) + name,
                    data = mapOf(context.getString(R.string.api_parameter_password) to password))
        }
    }
}

