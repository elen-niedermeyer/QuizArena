package com.quizarena.menu

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.quizarena.R
import com.quizarena.notifications.InstanceIdService
import com.quizarena.options.OptionsActivity
import com.quizarena.sessions.creation.CreateSessionActivity
import com.quizarena.sessions.overview.SessionOverviewActivity
import com.quizarena.user.User
import com.quizarena.user.UserApi
import com.quizarena.user.UserPersistence
import com.quizarena.user.login.LoginActivity

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val sessionOverviewButton = findViewById<Button>(R.id.activity_main_menu_button_show_sessions)
        sessionOverviewButton.setOnClickListener { startActivity(Intent(this@MainMenuActivity, SessionOverviewActivity::class.java)) }

        val createSessionButton = findViewById<Button>(R.id.activity_main_menu_button_create)
        createSessionButton.setOnClickListener { startActivity(Intent(this@MainMenuActivity, CreateSessionActivity::class.java)) }

        val optionsButton = findViewById<Button>(R.id.activity_main_menu_button_options)
        optionsButton.setOnClickListener { startActivity(Intent(this@MainMenuActivity, OptionsActivity::class.java)) }
    }

    override fun onStart() {
        super.onStart()

        // authenticate user
        UserPersistence(this).loadName();
        if (!User.isLoggedIn) {
            val isAuthenticated = UserApi(this).authenticate(User.accountName, InstanceIdService().getToken())
            if (!isAuthenticated) {
                // user couldn't be authenticated
                startActivity(Intent(this@MainMenuActivity, LoginActivity::class.java))
                this.finish()
            } else {
                User.isLoggedIn = true
            }
        }
    }

}
