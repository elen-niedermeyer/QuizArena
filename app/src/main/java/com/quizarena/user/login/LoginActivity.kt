package com.quizarena.user.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.quizarena.R
import com.quizarena.user.UserPersistence
import com.quizarena.user.register.RegisterActivity
import com.quizarena.menu.MainMenuActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.alert

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // load saved user credentials
        val creds = UserPersistence(this@LoginActivity).loadName()

        val api = LoginApi()
        // try to authenticate user with loaded credentials
        val wasSuccessfulWithSavedCredentials = api.executeLoginRequest(this@LoginActivity, creds.accountName)

        if (wasSuccessfulWithSavedCredentials) {
            // user credentials are correct
            // continue to main menu
            startNextActivity()

        } else {
            // couldn't load correct credentials
            // load login screen now

            setContentView(R.layout.activity_login)

            activity_login_button_confirm.setOnClickListener {
                if (validateAccountName() && validatePassword()) {
                    // name and password are valid

                    // extract input value
                    val name = activity_login_account_name.text.toString()
                    val password = activity_login_password.text.toString()

                    // try to authenticate user
                    val wasSuccessful = api.executeLoginRequest(this@LoginActivity, name)

                    if (wasSuccessful) {
                        // user credentials are correct
                        // save credentials and start main menu
                        UserPersistence(this@LoginActivity).saveName(name)
                        startNextActivity()

                    } else {
                        // authenticate user failed
                        // show an error message
                        alert {
                            title = getString(R.string.error)
                            message(api.state)
                            positiveButton(getString(R.string.ok)) { }
                        }.show()
                    }
                }
            }

            // change to register activity if the responding button is clicked
            activity_login_button_register.setOnClickListener { startActivity(Intent(this@LoginActivity, RegisterActivity::class.java)) }
        }

    }

    /**
     * Checks if the account name is not empty.
     * If it's empty, displays a message.
     *
     * @return true if the account name is correct, false otherwise
     */
    private fun validateAccountName(): Boolean {
        if (activity_login_account_name.text.isEmpty()) {
            activity_login_error_name.text = getString(R.string.login_error_no_name)

            return false
        }

        // everything correct
        // remove messages
        activity_login_error_name.text = null

        return true
    }

    /**
     * Checks if the password is not empty.
     * If it's empty, displays a message.
     *
     * @return true if the password is correct, false otherwise
     */
    private fun validatePassword(): Boolean {
        if (activity_login_password.text.isEmpty()) {
            activity_login_error_password.text = getString(R.string.login_error_no_password)

            return false
        }

        // everything correct
        // remove messages
        activity_login_error_password.text = null

        return true
    }

    /**
     * Starts the {@link MainMenuActivity} and finishes this one.
     */
    private fun startNextActivity() {
        startActivity(Intent(this@LoginActivity, MainMenuActivity::class.java))
        this.finish()
    }

}
