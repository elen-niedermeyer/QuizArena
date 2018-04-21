package com.quizarena.user

import android.content.Context
import com.quizarena.ApiErrors
import com.quizarena.R
import org.jetbrains.anko.doAsyncResult

class UserApi(val context: Context) {

    var state: String = ""
        private set

    /**
     * Makes a request to authenticate the user with the account name and the device's token.
     * Sets the {@link #state} if there's a failure.
     *
     * @param accountName the user's account name
     * @param token the device's token
     * @return true if the request was successful, false otherwise
     */
    fun authenticate(accountName: String, token: String?): Boolean {
        val response = doAsyncResult {
            val response = khttp.get(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_user_token_authentication, accountName, token))
            return@doAsyncResult response
        }.get()

        val statusCode = response.statusCode
        val responseState = response.text

        if (statusCode == 200) {
            // authentication was successful
            return true

        } else {
            // authentication was not successful
            // parse error
            this.state = ApiErrors.getUserError(context, responseState)
            return false
        }
    }

    // TODO: rules for passwords?
    /**
     * Makes a request to register the user with the given attributes.
     * Sets the {@link #state} if there's a failure.
     *
     * @param accountName the new user's account name
     * @param displayName the new user's display name
     * @param password the new user's password
     * @param token the device's token
     * @return true if the request was successful, false otherwise
     */
    fun register(accountName: String, displayName: String, password: String, token: String?): Boolean {
        val response = doAsyncResult {
            val response = khttp.post(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_user_specific, accountName),
                    data = mapOf(context.getString(R.string.endpoint_user_data_display_name) to displayName,
                            context.getString(R.string.endpoint_user_data_password) to password,
                            context.getString(R.string.endpoint_user_data_token) to token))
            return@doAsyncResult response
        }.get()

        val statusCode = response.statusCode
        val responseState = response.text

        if (statusCode == 200) {
            // register was successful
            return true

        } else {
            // register was not successful
            // parse error
            this.state = ApiErrors.getUserError(context, responseState)
            return false
        }
    }

    /**
     * Makes a request to login the user with the given attributes.
     * Sets the {@link #state} if there's a failure.
     *
     * @param accountName the user's account name
     * @param password the user's password
     * @param token the device's token
     * @return true if the request was successful, false otherwise
     */
    fun login(accountName: String, password: String, token: String?): Boolean {
        val response = doAsyncResult {
            val response = khttp.patch(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_user_login, accountName),
                    data = mapOf(context.getString(R.string.endpoint_user_data_password) to password,
                            context.getString(R.string.endpoint_user_data_token) to token))
            return@doAsyncResult response
        }.get()

        val statusCode = response.statusCode
        val responseState = response.text

        if (statusCode == 200) {
            // register was successful
            return true

        } else {
            // register was not successful
            // parse error
            this.state = ApiErrors.getUserError(context, responseState)
            return false
        }
    }

    /**
     * Makes a request to log out the user.
     * Sets the {@link #state} if there's a failure.
     *
     * @param accountName the user's account name
     * @return true if the request was successful, false otherwise
     */
    fun logout(accountName: String): Boolean {
        val response = doAsyncResult {
            val response = khttp.patch(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_user_logout, accountName))
            return@doAsyncResult response
        }.get()

        val statusCode = response.statusCode
        val responseState = response.text

        if (statusCode == 200) {
            // register was successful
            return true

        } else {
            // register was not successful
            // parse error
            this.state = ApiErrors.getUserError(context, responseState)
            return false
        }
    }

    fun changeDisplayName(accountName: String, newDisplayName: String): Boolean {
        val response = doAsyncResult {
            val response = khttp.patch(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_user_change_displayname, accountName),
                    data = mapOf(context.getString(R.string.endpoint_user_data_display_name) to newDisplayName))
            return@doAsyncResult response
        }.get()

        val statusCode = response.statusCode
        val responseState = response.text

        if (statusCode == 200) {
            // register was successful
            return true

        } else {
            // register was not successful
            // parse error
            this.state = ApiErrors.getUserError(context, responseState)
            return false
        }
    }

}