package com.quizarena.user

import android.content.Context
import com.quizarena.PreferenceUtils
import com.quizarena.R

class CurrentUserPersistence(val context: Context) {

    //TODO: load and save score and all

    var preferenceUtils = PreferenceUtils(context);

    /**
     * Saves the given name in preferences.
     * Makes a {@link CurrentUser} object.
     *
     * @param accountName the account name to save
     * @return a {@link CurrentUser} object
     */
    fun saveName(accountName: String): CurrentUser {
        preferenceUtils.writePreference(context.getString(R.string.pref_credentials_file), context.getString(R.string.pref_credentials_name_key), accountName)

        // create credentials and return it
        return CurrentUser(accountName)
    }

    /**
     * Loads the name saved in the preferences.
     *
     * @return a {@link CurrentUser} object
     */
    fun loadName(): CurrentUser {
        val name = preferenceUtils.loadPreferenceString(context.getString(R.string.pref_credentials_file), context.getString(R.string.pref_credentials_name_key))

        // create credentials and return it
        return CurrentUser(name)
    }


}