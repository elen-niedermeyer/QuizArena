package com.quizarena.user.credentials

import android.content.Context
import com.quizarena.user.CurrentUser
import com.quizarena.user.UserApi

class CredentialsUpdater(val context: Context) {

    /**
     * Changes the display name.
     * Makes a request and updates the {@link CurrentUser}.
     *
     * @param newDisplayName
     * @return true if the name was changed, false otherwise
     */
    fun changeDisplayName(newDisplayName: String): Boolean {
        val wasChangedSuccessfully = UserApi(context).changeDisplayName(CurrentUser.accountName, newDisplayName)
        if (!wasChangedSuccessfully) {
            // api request wasn't successful
            return false
        }

        CurrentUser.displayName = newDisplayName
        return true
    }

}