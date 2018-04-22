package com.quizarena.user

import android.content.Context
import com.quizarena.ApiErrors
import com.quizarena.R
import org.jetbrains.anko.doAsyncResult

class UserApi(val context: Context) {

    var state: String = ""
        private set

    /**
     * Makes a get request for the given user.
     * Sets the {@link #state} if there's a failure.
     *
     * @param accountName the user's account name
     * @return a {@link User} or null
     */
    fun getUser(accountName: String): User? {
        val response = doAsyncResult {
            val response = khttp.get(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_user_specific, accountName))
            return@doAsyncResult response
        }.get()

        val statusCode = response.statusCode
        val responseState = response.text

        if (statusCode == 200) {
            // user request was successful
            val json = response.jsonObject
            return User(json.getString(context.getString(R.string.api_user_accountName)),
                    json.getString(context.getString(R.string.api_user_displayName)),
                    json.getInt(context.getString(R.string.api_user_totalScore)))

        } else {
            // user request was not successful
            // parse error
            this.state = ApiErrors.getError(context, responseState)
            return null
        }
    }

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
            this.state = ApiErrors.getError(context, responseState)
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
            this.state = ApiErrors.getError(context, responseState)
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
            // login was successful
            return true

        } else {
            // login was not successful
            // parse error
            this.state = ApiErrors.getError(context, responseState)
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
            // logout was successful
            return true

        } else {
            // logout was not successful
            // parse error
            this.state = ApiErrors.getError(context, responseState)
            return false
        }
    }

    /**
     * Makes a request to change the display name.
     * Sets the {@link #state} if there's a failure.
     *
     * @param accountName the user's account name
     * @param newDisplayName
     * @return true if the request was successful, false otherwise
     */
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
            // changing was successful
            return true

        } else {
            // changing was not successful
            // parse error
            this.state = ApiErrors.getError(context, responseState)
            return false
        }
    }

    /**
     * Makes a request to change the password.
     * Sets the {@link #state} if there's a failure.
     *
     * @param accountName the user's account name
     * @param oldPassword the old password
     * @param newPassword the new password
     * @return true if the request was successful, false otherwise
     */
    fun changePassword(accountName: String, oldPassword: String, newPassword: String): Boolean {
        val response = doAsyncResult {
            val response = khttp.patch(
                    url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_user_change_password, accountName),
                    data = mapOf(context.getString(R.string.endpoint_user_data_password_old) to oldPassword,
                            context.getString(R.string.endpoint_user_data_password_new) to newPassword))
            return@doAsyncResult response
        }.get()

        val statusCode = response.statusCode
        val responseState = response.text

        if (statusCode == 200) {
            // changing was successful
            return true

        } else {
            // changing was not successful
            // parse error
            this.state = ApiErrors.getError(context, responseState)
            return false
        }
    }

}