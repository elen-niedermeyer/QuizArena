package com.quizarena.user.credentials

import android.content.Context
import com.quizarena.notifications.InstanceIdService
import com.quizarena.user.CurrentUser
import com.quizarena.user.CurrentUserPersistence
import com.quizarena.user.UserApi

class CredentialsUpdater(val context: Context) {

    /**
     * Authenticates the user and updates it's credentials.
     *
     * @return true if the user was authenticated, false otherwise
     */
    fun authenticate(): Boolean {
        CurrentUserPersistence(context).loadName();
        if (!CurrentUser.isLoggedIn) {
            val userApi = UserApi(context)
            val isAuthenticated = userApi.authenticate(CurrentUser.accountName, InstanceIdService().getToken())
            if (!isAuthenticated) {
                // user couldn't be authenticated
                return false

            } else {
                CurrentUser.isLoggedIn = true

                // update current user
                val user = userApi.getUser(CurrentUser.accountName)
                if (user != null) {
                    CurrentUser.displayName = user.displayName
                    CurrentUser.globalScore = user.totalScore
                }
            }
        }

        return true
    }

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