package com.quizarena

import android.content.Context

class ApiErrors {

    companion object {

        fun getUserError(context: Context, error: String): String {
            when {
                error == "no_pw" -> return context.getString(R.string.endpoint_user_error_password_empty)
                error == "no_display_name" -> return context.getString(R.string.endpoint_user_error_displayname_empty)
                error == "user_already_existing" -> return context.getString(R.string.endpoint_user_error_username_duplicated)
                error == "not_existing_user" -> return context.getString(R.string.endpoint_user_error_no_user)
                else -> return context.getString(R.string.api_request_error) + error

            }
        }

    }
}