package com.quizarena.user

import android.content.Context
import com.quizarena.PreferenceUtils
import com.quizarena.R

class UserPersistence(val context: Context) {

    //TODO: load and save score and all

    var preferenceUtils = PreferenceUtils(context);

    /**
     * Saves the given credentials in preferences.
     * Makes a {@link User} object.
     *
     * @param accountName the account name to save
     * @param password the user's password to save
     * @return a {@link User} object
     */
    fun saveCredentials(accountName: String, isLoggedIn: Boolean): User {
        preferenceUtils.writePreference(context.getString(R.string.pref_credentials_file), context.getString(R.string.pref_credentials_name_key), accountName)
        preferenceUtils.writePreference(context.getString(R.string.pref_credentials_file), context.getString(R.string.pref_credentials_login_key), isLoggedIn)

        // create credentials and return it
        return User(accountName, isLoggedIn)
    }

    /**
     * Loads the credentials saved in the preferences.
     *
     * @return a {@link User} object
     */
    fun loadCredentials(): User {
        val name = preferenceUtils.loadPreferenceString(context.getString(R.string.pref_credentials_file), context.getString(R.string.pref_credentials_name_key))
        val isLoggedIn = preferenceUtils.loadPreferenceBoolean(context.getString(R.string.pref_credentials_file), context.getString(R.string.pref_credentials_login_key))

        // create credentials and return it
        return User(name, isLoggedIn)
    }


}