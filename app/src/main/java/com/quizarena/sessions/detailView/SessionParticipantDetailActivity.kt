package com.quizarena.sessions.detailView

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.quizarena.R
import com.quizarena.authorization.Credentials
import com.quizarena.sessions.Participant
import com.quizarena.sessions.QuizSession
import com.quizarena.sessions.SessionApi
import com.quizarena.sessions.SessionUtils
import kotlinx.android.synthetic.main.activity_session_participant_detail.*

//TODO: add button for owners to terminate session
// TODO: share feature
class SessionParticipantDetailActivity : AppCompatActivity() {

    /**
     * Session which is displayed in this activity
     */
    private lateinit var currentSession: QuizSession

    private val onShareButtonClick = object : View.OnClickListener {
        override fun onClick(view: View?) {
            var text = getString(R.string.sharing_text)
            text = text + getString(R.string.sharing_link, currentSession.id.toString())

            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.type = "text/plain"
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sharing_subject))
            sendIntent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(Intent.createChooser(sendIntent, getString(R.string.sharing_chooser_title)))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_participant_detail)

        // get session
        val sessionID = intent.getIntExtra(getString(R.string.intent_extra_session_id), 0)
        val api = SessionApi()
        currentSession = api.getSession(sessionID, Credentials.accountName)

        if (currentSession.isParticipant) {
            // the user is participant of the session and allowed to see this view

            // set text views about the session
            activity_session_participant_detail_name.text = currentSession.name
            activity_session_participant_detail_category.text = currentSession.category
            activity_session_participant_detail_time.text = SessionUtils.getDurationString(this, currentSession)

            // get participants
            val participants = api.getParticipants(sessionID) as ArrayList<Participant>
            val thisUser = participants.filter { it.accountName == Credentials.accountName }.get(0)
            val thisUsersRank = participants.indexOf(thisUser) + 1
            // set text views about the current user
            activity_session_participant_detail_place.text = thisUsersRank.toString()
            activity_session_participant_detail_score.text = thisUser.sessionScore.toString()
            // set participants list
            activity_session_participant_detail_list.adapter = ParticipantsListAdapter(this, participants)

            // set share button
            activity_session_participant_detail_button_share.setOnClickListener(onShareButtonClick)

            if (currentSession.isOwner && currentSession.isPrivate) {
                // the owner can terminate private sessions
                // set the button for termination
                initializeTerminateButton()
            }

        } else {
            // the user is not participant of this session
            // open the correct activity and terminate this one
            intent = Intent(this@SessionParticipantDetailActivity, SessionDetailActivity::class.java)
            intent.putExtra(getString(R.string.intent_extra_session_clicked), currentSession)
            startActivity(intent)
            this.finish()
        }

    }

    /**
     * Initializes the terminate button depending on the state of {@link #currentSession}.
     */
    private fun initializeTerminateButton() {
        activity_session_participant_detail_button_terminate.visibility = View.VISIBLE

        if (SessionUtils.isSessionActive(currentSession)) {
            // the session is active
            // set the on click listener
            activity_session_participant_detail_button_terminate.setOnClickListener {
                // terminate the session
                if (SessionApi().terminateSession(currentSession.id)) {
                    // reload activity
                    startActivity(this.intent)
                    this.finish()
                } else {
                    // handling errors from api
                    // TODO: error handling
                }
            }

        } else {
            // the session is inactive
            // disable the button
            activity_session_participant_detail_button_terminate.isEnabled = false
        }

    }

}
