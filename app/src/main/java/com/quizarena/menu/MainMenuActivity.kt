package com.quizarena.menu

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.quizarena.R
import com.quizarena.imprint.ImprintActivity
import com.quizarena.options.OptionsActivity
import com.quizarena.sessions.QuizSession
import com.quizarena.sessions.creation.CreateSessionActivity
import com.quizarena.sessions.detailView.SessionDetailActivity
import com.quizarena.sessions.detailView.SessionParticipantDetailActivity
import com.quizarena.sessions.overview.SessionOverviewActivity
import com.quizarena.sharing.SessionSharing
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

        // the user was authenticated
        if (intent.action == Intent.ACTION_VIEW) {
            // the activity was opened by a sharing link
            // go to the needed activity
            val session = SessionSharing().getSessionFromLink(this, intent.data)
            startSessionDetailActivity(session)
            // override the intent action for going back in the other activity
            this.intent.action = Intent.ACTION_MAIN
        } else {
            val sessionID = intent.getStringExtra("session")
            if(sessionID != null){
                // the activity was opened by a push notification
                // go to the needed activity
                val session = SessionSharing().getSessionFromID(this, sessionID)
                startSessionDetailActivity(session)
                intent.removeExtra("session")
            }

            // activity was started manually, not by a link or notification
            // initialize content views
            activity_main_menu_displayname.text = CurrentUser.displayName
            activity_main_menu_globalscore.text = CurrentUser.globalScore.toString()
        }
    }

    private fun startSessionDetailActivity(session: QuizSession?){
        if(session != null){
            val detailViewIntent = Intent(this@MainMenuActivity, if(session.isParticipant) SessionParticipantDetailActivity::class.java else SessionDetailActivity::class.java)
            detailViewIntent.putExtra(getString(R.string.intent_extra_session_clicked), session)
            startActivity(detailViewIntent)
        }
    }
}
