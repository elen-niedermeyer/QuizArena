package com.quizarena.user.logout

import android.content.Context
import com.quizarena.user.User
import com.quizarena.user.UserApi
import com.quizarena.user.UserPersistence

class Logout(val context: Context) {

    /**
     * Logs out the user.
     * Sets local settings and makes a request.
     *
     * @return true if the api request was successful, false otherwise
     */
    fun logout(): Boolean {
        val isLoggedOut = UserApi(context).logout(User.accountName)

        User.isLoggedIn = false
        User.accountName = ""
        UserPersistence(context).saveName("")

        if (!isLoggedOut) {
            // failure in request
            return false
        }

        return true
    }
}