package com.quizarena.user.authorization

import android.content.Context
import com.quizarena.R

class CredentialPersistence(context: Context) {

    var context: Context = context
        private set

    /**
     * Saves the given credentials in preferences.
     * Makes a {@link Credentials} object.
     *
     * @param accountName the account name to save
     * @param password the user's password to save
     * @return a {@link Credentials} object
     */
    fun saveCredentials(accountName: String, password: String): Credentials {
        writePreference(context.getString(R.string.pref_credentials_file), context.getString(R.string.pref_credentials_name_key), accountName)
        writePreference(context.getString(R.string.pref_credentials_file), context.getString(R.string.pref_credentials_password_key), password)

        // create credentials and return it
        return Credentials(accountName, password)
    }

    /**
     * Loads the credentials saved in the preferences.
     *
     * @return a {@link Credentials} object
     */
    fun loadCredentials(): Credentials {
        val name = loadPreference(context.getString(R.string.pref_credentials_file), context.getString(R.string.pref_credentials_name_key))
        val password = loadPreference(context.getString(R.string.pref_credentials_file), context.getString(R.string.pref_credentials_password_key))

        // create credentials and return it
        return Credentials(name, password)
    }

    /**
     * Loads the requested string preference.
     *
     * @param prefFileName the preference file name
     * @param key the preference key
     * @return the loaded string or an empty string if the preferences couldn't be read
     */
    private fun loadPreference(prefFileName: String, key: String): String {
        val pref = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
        return pref.getString(key, "")
    }

    /**
     * Saves the given string value in the requested preference.
     *
     * @param prefFileName the preference file name
     * @param key the preference key
     * @param value the string value to save
     */
    private fun writePreference(prefFileName: String, key: String, value: String) {
        val pref = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
        val editor = pref!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

}