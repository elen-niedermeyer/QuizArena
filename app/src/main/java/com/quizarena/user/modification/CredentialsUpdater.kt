package com.quizarena.user.modification

import android.content.Context
import com.quizarena.user.User
import com.quizarena.user.UserApi

class CredentialsUpdater(val context: Context) {

    fun changeDisplayName(newDisplayName: String): Boolean {
        val wasChangedSuccessfully = UserApi(context).changeDisplayName(User.accountName, newDisplayName)
        if (!wasChangedSuccessfully) {
            // api request wasn't successful
            return false
        }

        User.displayName = newDisplayName
        return true
    }
}