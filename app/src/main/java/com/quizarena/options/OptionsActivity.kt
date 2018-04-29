package com.quizarena.options

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.quizarena.R
import com.quizarena.user.CurrentUser
import com.quizarena.user.UserApi
import com.quizarena.user.credentials.CredentialsUpdater
import com.quizarena.user.login.LoginActivity
import com.quizarena.user.logout.Logout
import kotlinx.android.synthetic.main.activity_options.*
import org.jetbrains.anko.alert


class OptionsActivity : AppCompatActivity() {

    /**
     * On click listener for the logout button.
     * Logs out the user.
     * Starts the {@link LoginActivity} and finishes this one.
     */
    private val onLogoutClick = View.OnClickListener {
        Logout(this@OptionsActivity).logout()
        // logged out
        // start login activity
        startActivity(Intent(this@OptionsActivity, LoginActivity::class.java))
        this@OptionsActivity.finish()
    }

    /**
     * On click listener for the button to change the display name.
     * Makes a dialog with input field.
     * When confirming the dialog, changes the display name and reloads the activity.
     */
    private val onDisplayNameChangeClick = View.OnClickListener {
        // build an dialog
        val builder = AlertDialog.Builder(this@OptionsActivity)
        builder.setTitle(getString(R.string.display_name_new))
        val viewInflated = LayoutInflater.from(this@OptionsActivity).inflate(R.layout.dialog_text_input, null)
        val input = viewInflated.findViewById(R.id.input) as EditText
        builder.setView(viewInflated)

        builder.setPositiveButton(android.R.string.ok, { dialog, _ ->
            dialog.dismiss()

            // get input
            val nameInput = input.text.toString()
            // change display name
            val wasChanged = CredentialsUpdater(this@OptionsActivity).changeDisplayName(nameInput)
            if (wasChanged) {
                // changing was successful
                this.finish()
                startActivity(intent)
            } else {
                // changing failed
                showError()
            }
        })

        builder.setNegativeButton(android.R.string.cancel, { dialog, _ -> dialog.cancel() })

        // show dialog
        builder.show()
    }

    /**
     * On click listener for the button to change the password.
     * Makes a dialog with input fields.
     * When confirming the dialog, changes the password and displays a second dialog with response.
     */
    private val onPasswordChangeClick = View.OnClickListener {
        // build an dialog
        val builder = AlertDialog.Builder(this@OptionsActivity)
        builder.setTitle(getString(R.string.password_change))
        val viewInflated = LayoutInflater.from(this@OptionsActivity).inflate(R.layout.dialog_password_change, null)
        val passwordOld = viewInflated.findViewById(R.id.input_password_old) as EditText
        val passwordNew1 = viewInflated.findViewById(R.id.input_password_new1) as EditText
        val passwordNew2 = viewInflated.findViewById(R.id.input_password_new2) as EditText
        builder.setView(viewInflated)

        builder.setPositiveButton(android.R.string.ok, { dialog, _ ->
            dialog.dismiss()

            // get input
            val passwordOldInput = passwordOld.text.toString()
            val passwordNew1Input = passwordNew1.text.toString()
            val passwordNew2Input = passwordNew2.text.toString()
            // validate password
            var error = validatePassword(passwordOldInput, passwordNew1Input, passwordNew2Input)
            if (error == null) {
                // validation was successful
                // make api request
                val api = UserApi(this@OptionsActivity)
                val wasChanged = api.changePassword(CurrentUser.accountName, passwordOldInput, passwordNew1Input)
                if (wasChanged) {
                    // api request was successful
                    showPasswordSuccess()
                } else {
                    error = api.state
                }
            }

            if (error != null) {
                // there was an error somewhere
                showError(error)
            }

        })

        builder.setNegativeButton(android.R.string.cancel, { dialog, _ -> dialog.cancel() })

        // show dialog
        builder.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        activity_options_account_name.text = CurrentUser.accountName
        activity_options_button_logout.setOnClickListener(onLogoutClick)
        activity_options_display_name.text = CurrentUser.displayName
        activity_options_button_change_name.setOnClickListener(onDisplayNameChangeClick)
        activity_options_button_change_password.setOnClickListener(onPasswordChangeClick)
    }

    /**
     * Makes an error dialog.
     */
    private fun showError() {
        // show an error message
        alert {
            title = getString(R.string.error)
            message(getString(R.string.error_general))
            positiveButton(getString(R.string.ok)) { }
        }.show()
    }

    /**
     * Makes an error dialog.
     *
     * @param message the error message
     */
    private fun showError(message: String) {
        // show an error message
        alert {
            title = getString(R.string.error)
            message(message)
            positiveButton(getString(R.string.ok)) { }
        }.show()
    }

    private fun showPasswordSuccess() {
        // show an success message
        alert {
            title = getString(R.string.error)
            message(getString(R.string.password_changed))
            positiveButton(getString(R.string.ok)) { }
        }.show()
    }

    /**
     * Checks if the password is not empty and the both passwords are equal.
     * If there's a failure, returns a message.
     *
     * @param passwordOld old password
     * @param password1 new password
     * @param password2 new password repetition
     * @return an error message or null
     */
    private fun validatePassword(passwordOld: String, password1: String, password2: String): String? {
        when {
            passwordOld.isEmpty() ->
                return getString(R.string.error_no_password)
            password1.isEmpty() ->
                return getString(R.string.error_no_password)
            password1 != password2 ->
                return getString(R.string.error_passwords_different)
            else -> return null
        }
    }

}
