package com.quizarena.user.logout

import android.content.Context
import com.quizarena.user.CurrentUser
import com.quizarena.user.UserApi
import com.quizarena.user.CurrentUserPersistence

class Logout(val context: Context) {

    /**
     * Logs out the user.
     * Sets local settings and makes a request.
     *
     * @return true if the api request was successful, false otherwise
     */
    fun logout(): Boolean {
        val isLoggedOut = UserApi(context).logout(CurrentUser.accountName)

        CurrentUser.isLoggedIn = false
        CurrentUser.accountName = ""
        CurrentUserPersistence(context).saveName("")

        if (!isLoggedOut) {
            // failure in request
            return false
        }

        return true
    }
}