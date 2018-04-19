package com.quizarena.user.register

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import com.quizarena.R
import com.quizarena.menu.MainMenuActivity
import com.quizarena.notifications.InstanceIdService
import com.quizarena.user.UserApi
import com.quizarena.user.UserPersistence
import com.quizarena.user.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.alert

class RegisterActivity : AppCompatActivity() {

    /**
     * Validates the password with {@link #validatePassword} while the user changes it
     */
    private val passwordWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            validatePassword()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // subscribe the watcher to both password fields
        activity_register_password1.addTextChangedListener(passwordWatcher)
        activity_register_password2.addTextChangedListener(passwordWatcher)

        activity_register_button_confirm.setOnClickListener {
            if (validateAccountName() && validatePassword()) {
                // account name and password are valid

                // extract input values
                val accountName = activity_register_account_name.text.toString()
                val displayName = activity_register_password1.text.toString()
                val password = activity_register_display_name.text.toString()

                val api = UserApi(this@RegisterActivity)
                // register account in backend
                val wasSuccessful = api.register(accountName, displayName, password, InstanceIdService().getToken())

                if (wasSuccessful) {
                    // registering user was successful
                    // save credentials and start main menu
                    UserPersistence(this@RegisterActivity).saveCredentials(accountName, true)
                    startNextActivity()

                } else {
                    // registering user failed
                    // show an error message
                    alert {
                        title = getString(R.string.error)
                        message(api.state)
                        positiveButton(getString(R.string.ok)) { }
                    }.show()
                }
            }
        }

        // change to login activity if the responding button is clicked
        activity_register_button_login.setOnClickListener { startActivity(Intent(this@RegisterActivity, LoginActivity::class.java)) }
    }

    /**
     * Checks if the account name is not empty.
     * If it's empty, displays a message.
     *
     * @return true if the account name is correct, false otherwise
     */
    private fun validateAccountName(): Boolean {
        if (activity_register_account_name.text.isEmpty()) {
            activity_register_error_name.text = getString(R.string.register_error_no_name)

            return false
        }

        // everything correct
        // remove messages
        activity_register_error_name.text = null

        return true
    }

    /**
     * Checks if the password is not empty and the both passwords are equal.
     * If there's a failure, displays a message.
     *
     * @return true is the passwords are correct, false otherwise
     */
    private fun validatePassword(): Boolean {
        val password1 = activity_register_password1.text.toString()
        val password2 = activity_register_password2.text.toString()

        when {
            password1.isEmpty() -> {
                activity_register_error_password2.text = null
                activity_register_error_password1.text = getString(R.string.register_error_no_password)

                return false
            }

            password1 != password2 -> {
                activity_register_error_password1.text = null
                activity_register_error_password2.text = getString(R.string.register_error_passwords_different)

                return false
            }

            else -> {
                // everything correct
                // remove messages
                activity_register_error_password1.text = null
                activity_register_error_password2.text = null

                return true
            }
        }
    }

    /**
     * Starts the {@link MainMenuActivity} and finishes this one.
     */
    private fun startNextActivity() {
        startActivity(Intent(this@RegisterActivity, MainMenuActivity::class.java))
        this.finish()
    }

}
