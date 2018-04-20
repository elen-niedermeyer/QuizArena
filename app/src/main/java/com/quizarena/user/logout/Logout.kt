package com.quizarena.user.logout

import android.content.Context
import com.quizarena.user.User
import com.quizarena.user.UserApi
import com.quizarena.user.UserPersistence

class Logout(val context: Context) {

    fun logout(): Boolean {
        val isLoggedOut = UserApi(context).logout(User.accountName)
        if (!isLoggedOut) {
            // failure in request
            return false
        }

        User.isLoggedIn = false
        User.accountName = ""
        UserPersistence(context).saveName("")

        return true
    }
}