package com.quizarena.sessions.detailView

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.quizarena.R
import com.quizarena.sessions.*
import com.quizarena.sharing.SessionSharing
import com.quizarena.user.CurrentUser
import kotlinx.android.synthetic.main.activity_session_participant_detail.*
import org.jetbrains.anko.alert

class SessionParticipantDetailActivity : AppCompatActivity() {

    /**
     * Session which is displayed in this activity
     */
    private var currentSession: QuizSession? = null

    private val onShareButtonClick = View.OnClickListener {
        SessionSharing().shareLink(this@SessionParticipantDetailActivity, currentSession!!.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_participant_detail)

        // get session
        val sessionID = intent.getStringExtra(getString(R.string.intent_extra_session_id))
        val api = SessionApi(this@SessionParticipantDetailActivity)
        if (sessionID != null) {
            currentSession = api.getSession(sessionID, CurrentUser.accountName)
        } else {
            currentSession = intent.getParcelableExtra(getString(R.string.intent_extra_session_clicked))
        }

        if (currentSession!!.isParticipant) {
            // the user is participant of the session and allowed to see this view

            // set text views about the session
            activity_session_participant_detail_name.text = currentSession!!.name
            activity_session_participant_detail_category.text = currentSession!!.category
            activity_session_participant_detail_time.text = SessionUtils.getDurationString(this, currentSession!!)

            // get participants
            var participants = api.getParticipants(currentSession!!.id)
            participants = ArrayList<Participant>(ParticipantsUtils.addRanking(participants!!))
            val thisUser = participants.filter { it.accountName == CurrentUser.accountName }[0]
            val thisUsersRank = thisUser.ranking
            // set text views about the current user
            activity_session_participant_detail_place.text = thisUsersRank.toString()
            activity_session_participant_detail_score.text = thisUser.sessionScore.toString()
            // set participants list
            activity_session_participant_detail_list.adapter = ParticipantsListAdapter(this, participants)

            // set share button
            activity_session_participant_detail_button_share.setOnClickListener(onShareButtonClick)

            if (currentSession!!.isOwner) {
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

        if (SessionUtils.isSessionActive(currentSession!!)) {
            // the session is active
            // set the on click listener
            activity_session_participant_detail_button_terminate.setOnClickListener {
                // terminate the session
                val api = SessionApi(this@SessionParticipantDetailActivity)
                if (api.terminateSession(currentSession!!.id, CurrentUser.accountName)) {
                    // reload activity
                    startActivity(this.intent)
                    this.finish()
                } else {
                    // handling errors from api
                    // show an error message
                    alert {
                        title = getString(R.string.error)
                        message(api.state)
                        positiveButton(getString(R.string.ok)) { }
                    }.show()
                }
            }

        } else {
            // the session is inactive
            // disable the button
            activity_session_participant_detail_button_terminate.isEnabled = false
        }

    }

}