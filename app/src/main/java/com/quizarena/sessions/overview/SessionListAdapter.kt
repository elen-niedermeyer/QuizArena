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

        // set session name
        sessionBar.activity_session_overview_bar_name.text = session.name

        // set category
        sessionBar.activity_session_overview_bar_category.text = session.category

        // set background
        sessionBar.activity_session_overview_bar.setBackgroundResource(getSessionBarBackground(session))

        // set duration
        sessionBar.activity_session_overview_bar_duration.text = getDurationString(session)

        // set image for private sessions
        if (session.isPrivate) {
            sessionBar.activity_session_overview_bar_icon.setImageResource(getPrivateIcon(session))
        }

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
                return R.color.overview_session_owned
            session.isParticipant -> // participated sessions
                return R.color.overview_session_participated
            session.isPrivate -> // private sessions
                return R.color.overview_session_other
            else -> // open sessions
                return R.color.primary_material_light
        }
    }

    /**
     * Gets the left time for a session and return a string representing this time.
     * Returns string that presents termination if the session already terminated.
     *
     * @param session the session for which you want to have ththe left time
     * @return a string representing the left time or termination
     */
    private fun getDurationString(session: QuizSession): String {
        val durationInSeconds = (session.enddate.time - System.currentTimeMillis()) / 1000
        if (durationInSeconds > 0) {
            return adapterContext.getString(R.string.left_time, durationInSeconds / 3600, (durationInSeconds % 3600) / 60)
        } else {
            return adapterContext.getString(R.string.terminated)
        }
    }

    /**
     * Gets the icon for private sessions.
     * A session is private but open for the user, if the user is owner or participant.
     * A session is private and closed for the user otherwise.
     *
     * @param session a (private) for which you want to have the private icon
     * @return an icon resource representing the private session's state
     */
    private fun getPrivateIcon(session: QuizSession): Int {
        when {
            session.isPrivate && (session.isOwner || session.isParticipant) ->
                return R.drawable.ic_lock_open_black
            session.isPrivate -> return R.drawable.ic_lock_closed_black
            else -> // zero is an empty view
                return 0
        }
    }

}
