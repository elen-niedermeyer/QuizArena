package com.quizarena

import android.content.Context

class ApiErrors {

    companion object {

        fun getUserError(context: Context, error: String): String {
            when {
            // TODO: error for duplicated key
                error == "no_pw" -> return context.getString(R.string.endpoint_user_error_password_empty)
                error == "no_display_name" -> return context.getString(R.string.endpoint_user_error_displayname_empty)
                else -> return error

            }
        }

    }
}