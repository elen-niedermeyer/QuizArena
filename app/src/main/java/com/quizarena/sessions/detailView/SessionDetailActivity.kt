package com.quizarena.sessions.detailView

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.quizarena.R
import com.quizarena.sessions.QuizSession
import com.quizarena.sessions.overview.SessionUtils
import kotlinx.android.synthetic.main.activity_session_detail.*

class SessionDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_detail)

        val currentSession = intent.getParcelableExtra<QuizSession>(getString(R.string.intent_extra_session_clicked))

        activity_session_detail_name.text = currentSession.name
        activity_session_detail_category.text = currentSession.category
        activity_session_detail_time.text = SessionUtils.getDurationString(this, currentSession)
    }
}
