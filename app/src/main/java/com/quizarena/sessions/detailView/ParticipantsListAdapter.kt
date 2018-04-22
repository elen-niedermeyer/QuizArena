package com.quizarena.sessions.detailView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.quizarena.R
import com.quizarena.sessions.Participant
import kotlinx.android.synthetic.main.activity_session_participant_detail_bar_participant.view.*

class ParticipantsListAdapter(private val adapterContext: Context, var values: ArrayList<Participant>) : ArrayAdapter<Participant>(adapterContext, -1, values) {

    /**
     * Makes the view for each list item.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // get bar layout for sessions
        val layoutInflater = adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val participantBar = layoutInflater.inflate(R.layout.activity_session_participant_detail_bar_participant, null)

        // get current session
        val user = values[position]

        // set user's ranking
        participantBar.activity_session_participant_detail_bar_participant_rank.text = user.ranking.toString()

        // set user name
        participantBar.activity_session_participant_detail_bar_participant_name.text = user.displayName

        // set user's globalScore
        participantBar.activity_session_participant_detail_bar_participant_score.text = user.sessionScore.toString()

        return participantBar
    }

    /**
     * Disable all items. They should not be clickable.
     */
    override fun isEnabled(position: Int): Boolean {
        return false
    }

}