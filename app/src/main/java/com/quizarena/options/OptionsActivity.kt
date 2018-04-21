package com.quizarena.options

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.quizarena.R
import com.quizarena.user.User
import com.quizarena.user.login.LoginActivity
import com.quizarena.user.logout.Logout
import kotlinx.android.synthetic.main.activity_options.*

class OptionsActivity : AppCompatActivity() {

    private val onLogoutClick = View.OnClickListener {
        Logout(this@OptionsActivity).logout()
        // logged out
        // start login activity
        startActivity(Intent(this@OptionsActivity, LoginActivity::class.java))
        this@OptionsActivity.finish()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        activity_options_account_name.text = User.accountName
        activity_options_button_logout.setOnClickListener(onLogoutClick)

    }
}
