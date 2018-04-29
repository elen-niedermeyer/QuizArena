package com.quizarena.sessions.overview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.quizarena.R
import com.quizarena.sessions.QuizSession
import com.quizarena.sessions.SessionUtils
import kotlinx.android.synthetic.main.activity_session_overview_session_bar.view.*

class SessionListAdapter(private val adapterContext: Context, var values: ArrayList<QuizSession>) : ArrayAdapter<QuizSession>(adapterContext, -1, values) {

    /**
     * Makes the view for each list item.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // get bar layout for sessions
        val layoutInflater = adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val sessionBar = layoutInflater.inflate(R.layout.activity_session_overview_session_bar, null)

        // get current session
        val session = values[position]

        // set session name
        sessionBar.activity_session_overview_bar_name.text = session.name

        // set category
        sessionBar.activity_session_overview_bar_category.text = session.category

        // set background
        sessionBar.activity_session_overview_bar.setBackgroundResource(getSessionBarBackground(session))

        // set duration
        sessionBar.activity_session_overview_bar_duration.text = SessionUtils.getDurationString(adapterContext, session)

        // set image for sessions
        sessionBar.activity_session_overview_bar_icon.setImageResource(getSessionIcon(session))

        return sessionBar
    }

    /**
     * Gets the session bar's background depending on the session's mode.
     *
     * @param session the session for which you want to have the bar's background
     * @return the resource id of the background
     */
    private fun getSessionBarBackground(session: QuizSession): Int {
        when {
            session.isOwner -> // owned sessions
                return R.drawable.background_session_overview_owned
            session.isParticipant -> // participated sessions
                return R.drawable.background_session_overview_participated
            else -> // user does not participate the session
                return R.drawable.background_session_overview_open
        }
    }

    /**
     * Gets the icon for private sessions.
     * A session could be public.
     * or s session is private but accessible for the user, if the user is owner or participant.
     * Or a session is private and not accessible for the user
     *
     * @param session a session for which you want to have the icon
     * @return an icon resource representing the private session's state
     */
    private fun getSessionIcon(session: QuizSession): Int {
        when {
            session.isPrivate && (session.isOwner || session.isParticipant) ->
                return R.drawable.ic_lock_open_black_32dp
            session.isPrivate ->
                return R.drawable.ic_lock_closed_black_32dp
            else -> // session is open
                return R.drawable.ic_person_black_32dp
        }
    }

}
