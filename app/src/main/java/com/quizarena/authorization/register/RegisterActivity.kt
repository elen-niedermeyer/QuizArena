package com.quizarena.authorization.register

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import com.quizarena.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

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

        activity_register_password1.addTextChangedListener(passwordWatcher)
        activity_register_password2.addTextChangedListener(passwordWatcher)

        activity_register_button_confirm.setOnClickListener {
            if (validateAccountName() && validatePassword()) {
                RegisterApi().registerRequest(this, activity_register_account_name.text.toString(),
                        activity_register_password1.text.toString(),
                        activity_register_display_name.text.toString())
            }
        }
    }

    private fun validateAccountName(): Boolean {
        if (activity_register_account_name.text.isEmpty()) {
            activity_register_error_name.text = getString(R.string.account_error_no_name)

            return false
        }

        activity_register_error_name.text = null

        return true
    }

    private fun validatePassword(): Boolean {
        val password1 = activity_register_password1.text.toString()
        val password2 = activity_register_password2.text.toString()

        if (password1.isEmpty()) {
            activity_register_error_password2.text = null
            activity_register_error_password1.text = getString(R.string.account_error_no_password)

            return false

        } else if (password1 != password2) {
            activity_register_error_password1.text = null
            activity_register_error_password2.text = getString(R.string.account_error_passwords_different)

            return false

        } else {
            activity_register_error_password1.text = null
            activity_register_error_password2.text = null

            return true
        }
    }
}
