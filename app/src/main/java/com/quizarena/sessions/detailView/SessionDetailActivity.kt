package com.quizarena.sessions.detailView

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.quizarena.R
import com.quizarena.quiz.QuizActivity
import com.quizarena.sessions.QuizSession
import com.quizarena.sessions.SessionUtils
import kotlinx.android.synthetic.main.activity_session_detail.*

class SessionDetailActivity : AppCompatActivity() {

    /**
     * Session which is displayed in this activity
     */
    private var currentSession: QuizSession? = null

    private val onParticipateButtonClick = View.OnClickListener {
        // get password
        var password: String? = activity_session_detail_password.text.toString()
        if (password!!.isEmpty()) {
            password = null
        }

        // start quiz activity
        val intent = Intent(this@SessionDetailActivity, QuizActivity::class.java)
        intent.putExtra(getString(R.string.intent_extra_session_id), currentSession!!.id)
        intent.putExtra(getString(R.string.intent_extra_session_password), password)
        startActivity(intent)
        // finishes this activity
        this@SessionDetailActivity.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_detail)

        currentSession = intent.getParcelableExtra(getString(R.string.intent_extra_session_clicked))

        if (!(currentSession!!.isParticipant)) {
            // the user isn't participant of the session
            // initialize content
            // set text views
            activity_session_detail_name.text = currentSession!!.name
            activity_session_detail_category.text = currentSession!!.category
            activity_session_detail_time.text = SessionUtils.getDurationString(this, currentSession!!)

            if (currentSession!!.isPrivate) {
                activity_session_detail_password.visibility = View.VISIBLE
            }

            // set button's on click listener
            activity_session_detail_button_participate.setOnClickListener(onParticipateButtonClick)

        } else {
            // the user is participant of this session
            // open the correct activity and terminate this one
            intent = Intent(this@SessionDetailActivity, SessionParticipantDetailActivity::class.java)
            intent.putExtra(getString(R.string.intent_extra_session_id), currentSession!!.id)
            startActivity(intent)
            this.finish()
        }
    }

}
