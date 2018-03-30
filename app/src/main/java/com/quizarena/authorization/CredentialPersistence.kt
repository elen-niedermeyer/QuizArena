package com.quizarena.authorization

import android.content.Context
import com.quizarena.R

class CredentialPersistence(context: Context) {

    var context: Context = context
        private set

    fun saveCredentials(accountName: String, password: String) {
        writePreference(context.getString(R.string.pref_credentials_file), context.getString(R.string.pref_credentials_name_key), accountName)
        writePreference(context.getString(R.string.pref_credentials_file), context.getString(R.string.pref_credentials_password_key), password)

        // create credentials
        Credentials(accountName, password)
    }

    fun saveCredentials(credentials: Credentials) {
        saveCredentials(credentials.accountName, credentials.password)
    }

    fun loadCredentials(): Credentials {
        val name = loadPreference(context.getString(R.string.pref_credentials_file), context.getString(R.string.pref_credentials_name_key))
        val password = loadPreference(context.getString(R.string.pref_credentials_file), context.getString(R.string.pref_credentials_password_key))

        // create credentials and return it
        return Credentials(name, password)
    }

    private fun loadPreference(prefFileName: String, key: String): String {
        val pref = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
        return pref.getString(key, "")
    }

    private fun writePreference(prefFileName: String, key: String, value: String) {
        val pref = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
        val editor = pref!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

}