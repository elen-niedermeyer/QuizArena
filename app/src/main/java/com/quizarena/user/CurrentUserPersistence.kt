package com.quizarena.user

import android.content.Context
import com.quizarena.PreferenceUtils
import com.quizarena.R

class CurrentUserPersistence(val context: Context) {

    private var preferenceUtils = PreferenceUtils(context)

    /**
     * Saves the given name in preferences.
     * Makes a {@link CurrentUser} object.
     *
     * @param accountName the account name to save
     */
    fun saveName(accountName: String) {
        preferenceUtils.writePreference(context.getString(R.string.pref_credentials_file), context.getString(R.string.pref_credentials_name_key), accountName)
    }

    /**
     * Loads the name saved in the preferences and set it
     */
    fun loadName() {
        val name = preferenceUtils.loadPreferenceString(context.getString(R.string.pref_credentials_file), context.getString(R.string.pref_credentials_name_key))

        CurrentUser.accountName = name
    }

}