package com.quizarena.sessions.overview

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.quizarena.R
import com.quizarena.sessions.QuizSession
import com.quizarena.sessions.SessionProvider
import kotlinx.android.synthetic.main.activity_session_overview.*
import kotlinx.android.synthetic.main.activity_session_overview_session_bar.view.*

class SessionOverviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_overview)

        val provider = SessionProvider()

        // TODO: handle negative times
        for (session in provider.getAllSessionSorted()) {
            activity_session_overview_content.addView(getSessionBar(session))
        }
    }

    private fun getSessionBar(session: QuizSession): View {
        // get bar layout
        val sessionBar = layoutInflater.inflate(R.layout.activity_session_overview_session_bar, null)

        // choose background
        if (session.isOwner) {
            sessionBar.activity_session_overview_bar.setBackgroundResource(R.color.overview_session_owned)
        } else if (session.isParticipant) {
            sessionBar.activity_session_overview_bar.setBackgroundResource(R.color.overview_session_participated)
        } else {
            sessionBar.activity_session_overview_bar.setBackgroundResource(R.color.overview_session_other)
        }

        // set session name
        sessionBar.activity_session_overview_bar_name.text = session.name

        // set duration
        val durationInSeconds = (session.enddate.time - System.currentTimeMillis()) / 1000
        sessionBar.activity_session_overview_bar_duration.text = String.format("%02dh %02dmin", durationInSeconds / 3600, (durationInSeconds % 3600) / 60)

        // set category
        sessionBar.activity_session_overview_bar_category.text = session.category

        return sessionBar
    }
}
