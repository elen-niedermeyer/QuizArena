package com.quizarena.sessions.detailView

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.quizarena.R
import com.quizarena.authorization.Credentials
import com.quizarena.sessions.Participant
import com.quizarena.sessions.SessionApi
import com.quizarena.sessions.overview.SessionUtils
import kotlinx.android.synthetic.main.activity_session_participant_detail.*

//TODO: add button for owners to terminate session
class SessionParticipantDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_participant_detail)

        val sessionID = intent.getIntExtra(getString(R.string.intent_extra_session_id), 0)
        val api = SessionApi()
        val currentSession = api.getSession(sessionID, Credentials.accountName)

        if (currentSession.isParticipant) {
            // the user is participant of the session and allowed to see this view

            // set text views about the session
            activity_session_participant_detail_name.text = currentSession.name
            activity_session_participant_detail_category.text = currentSession.category
            activity_session_participant_detail_time.text = SessionUtils.getDurationString(this, currentSession)

            val participants = api.getParticipants(sessionID) as ArrayList<Participant>
            val thisUser = participants.filter { it.accountName == Credentials.accountName }.get(0)
            val thisUsersRank = participants.indexOf(thisUser) + 1
            // set text views about the current user
            activity_session_participant_detail_place.text = thisUsersRank.toString()
            activity_session_participant_detail_score.text = thisUser.sessionScore.toString()
            // set participants list
            activity_session_participant_detail_list.adapter = ParticipantsListAdapter(this, participants)

        } else {
            // the user is not participant of this session
            // terminate the activity
            this.finish()
        }

    }

}
