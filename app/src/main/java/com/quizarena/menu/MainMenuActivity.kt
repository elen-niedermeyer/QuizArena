package com.quizarena.menu

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

import com.quizarena.R
import com.quizarena.options.OptionsActivity
import com.quizarena.sessions.creation.CreateSessionActivity
import com.quizarena.sessions.overview.SessionOverviewActivity

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
}
