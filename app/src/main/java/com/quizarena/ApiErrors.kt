package com.quizarena

import android.content.Context

class ApiErrors {

    companion object {

        /**
         * Parses the errors of the api
         *
         * @param context
         * @param error api error
         * @return an error that's understandable for the user
         */
        fun getError(context: Context, error: String): String {
            when (error) {
                "not_existing_user" -> return context.getString(R.string.api_error_user_not_existing)
                "user_not_found" -> return context.getString(R.string.api_error_user_not_existing)
                "user_already_existing" -> return context.getString(R.string.api_error_username_duplicated)
                "no_display_name" -> return context.getString(R.string.api_error_displayname_empty)
                "pw_missmatch" -> return context.getString(R.string.api_error_wrong_password)
                "old_pw_missmatch" -> return context.getString(R.string.api_error_wrong_password)
                "no_pw" -> return context.getString(R.string.api_error_password_empty)
                "no_pw_for_private_session" -> return context.getString(R.string.api_error_password_empty)
                else -> return context.getString(R.string.error_general) + error
            }
        }

    }
}