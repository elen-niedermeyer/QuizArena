package com.quizarena.menu

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.quizarena.R
import com.quizarena.imprint.ImprintActivity
import com.quizarena.options.OptionsActivity
import com.quizarena.sessions.creation.CreateSessionActivity
import com.quizarena.sessions.overview.SessionOverviewActivity
import com.quizarena.user.CurrentUser
import com.quizarena.user.credentials.CredentialsUpdater
import com.quizarena.user.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        activity_main_menu_button_show_sessions.setOnClickListener { startActivity(Intent(this@MainMenuActivity, SessionOverviewActivity::class.java)) }

        activity_main_menu_button_create.setOnClickListener { startActivity(Intent(this@MainMenuActivity, CreateSessionActivity::class.java)) }

        activity_main_menu_button_options.setOnClickListener { startActivity(Intent(this@MainMenuActivity, OptionsActivity::class.java)) }

        activity_main_menu_button_imprint.setOnClickListener { startActivity(Intent(this@MainMenuActivity, ImprintActivity::class.java)) }
    }

    override fun onStart() {
        super.onStart()

        // authenticate user
        val isAuthenticated = CredentialsUpdater(this).authenticate()
        if (!isAuthenticated) {
            // user couldn't be authenticated
            startActivity(Intent(this, LoginActivity::class.java))
            this.finish()
        }

        activity_main_menu_displayname.text = CurrentUser.displayName
        activity_main_menu_globalscore.text = CurrentUser.globalScore.toString()
    }

}
