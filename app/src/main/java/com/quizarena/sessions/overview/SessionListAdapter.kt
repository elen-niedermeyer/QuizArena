package com.quizarena.sessions.overview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.quizarena.R
import com.quizarena.sessions.QuizSession
import kotlinx.android.synthetic.main.activity_session_overview_session_bar.view.*

class SessionListAdapter(private val adapterContext: Context, var values: ArrayList<QuizSession>) : ArrayAdapter<QuizSession>(adapterContext, -1, values) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // get bar layout
        val layoutInflater = adapterContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val sessionBar = layoutInflater.inflate(R.layout.activity_session_overview_session_bar, null)

        // get current session
        val session = values[position]

        // set background depending on the session's mode
        when {
            session.isOwner -> // owned sessions
                sessionBar.activity_session_overview_bar.setBackgroundResource(R.color.overview_session_owned)
            session.isParticipant -> // participated sessions
                sessionBar.activity_session_overview_bar.setBackgroundResource(R.color.overview_session_participated)
            session.isPrivate -> // private sessions
                sessionBar.activity_session_overview_bar.setBackgroundResource(R.color.overview_session_other)
            else -> // open sessions
                sessionBar.activity_session_overview_bar.setBackgroundResource(R.color.primary_material_light)
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
