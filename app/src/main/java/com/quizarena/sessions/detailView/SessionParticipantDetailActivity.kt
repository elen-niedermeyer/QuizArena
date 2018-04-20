package com.quizarena.sessions.detailView

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.quizarena.R
import com.quizarena.sessions.*
import com.quizarena.user.User
import kotlinx.android.synthetic.main.activity_session_participant_detail.*

//TODO: add button for owners to terminate session
// TODO: share feature
class SessionParticipantDetailActivity : AppCompatActivity() {

    /**
     * Session which is displayed in this activity
     */
    private lateinit var currentSession: QuizSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_participant_detail)

        // get session
        val sessionID = intent.getIntExtra(getString(R.string.intent_extra_session_id), 0)
        val api = SessionApi()
        currentSession = api.getSession(sessionID, User.accountName)

        if (currentSession.isParticipant) {
            // the user is participant of the session and allowed to see this view

            // set text views about the session
            activity_session_participant_detail_name.text = currentSession.name
            activity_session_participant_detail_category.text = currentSession.category
            activity_session_participant_detail_time.text = SessionUtils.getDurationString(this, currentSession)

            // get participants
            var participants = api.getParticipants(sessionID)
            participants = ArrayList<Participant>(ParticipantsUtils.addRanking(participants))
            val thisUser = participants.filter { it.accountName == User.accountName }.get(0)
            val thisUsersRank = thisUser.ranking
            // set text views about the current user
            activity_session_participant_detail_place.text = thisUsersRank.toString()
            activity_session_participant_detail_score.text = thisUser.sessionScore.toString()
            // set participants list
            activity_session_participant_detail_list.adapter = ParticipantsListAdapter(this, participants)

            if (currentSession.isOwner && currentSession.isPrivate) {
                // the owner can terminate private sessions
                // set the button for termination
                initializeTerminateButton()
            }

        } else {
            // the user is not participant of this session
            // terminate the activity
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
