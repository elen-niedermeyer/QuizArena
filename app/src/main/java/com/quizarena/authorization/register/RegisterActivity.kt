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
            // TODO: show error
            return false
        }

        return true
    }

    private fun validatePassword(): Boolean {
        val password1 = activity_register_password1.text.toString()
        val password2 = activity_register_password2.text.toString()

        if (password1.isEmpty()) {
            // TODO: show error

            return false

        } else if (password1 != password2) {
            // TODO: show another error

            return false

        } else {

            return true
        }
    }
}
