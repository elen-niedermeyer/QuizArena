package com.quizarena

import android.content.Context

class PreferenceUtils(val context: Context) {

    /**
     * Loads the requested string preference.
     *
     * @param prefFileName the preference file name
     * @param key the preference key
     * @return the loaded string or an empty string if the preferences couldn't be read
     */
    fun loadPreferenceString(prefFileName: String, key: String): String {
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
    fun writePreference(prefFileName: String, key: String, value: String) {
        val pref = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
        val editor = pref!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * Loads the requested boolean preference.
     *
     * @param prefFileName the preference file name
     * @param key the preference key
     * @return the loaded boolean or false if the preferences couldn't be read
     */
    fun loadPreferenceBoolean(prefFileName: String, key: String): Boolean {
        val pref = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
        return pref.getBoolean(key, false)
    }

    /**
     * Saves the given boolean value in the requested preference.
     *
     * @param prefFileName the preference file name
     * @param key the preference key
     * @param value the boolean value to save
     */
    fun writePreference(prefFileName: String, key: String, value: Boolean) {
        val pref = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
        val editor = pref!!.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
}
