package com.quizarena.sessions.overview

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.SearchView
import com.quizarena.R
import com.quizarena.sessions.QuizSession
import com.quizarena.sessions.SessionProvider
import com.quizarena.sessions.detailView.SessionDetailActivity
import com.quizarena.sessions.detailView.SessionParticipantDetailActivity
import kotlinx.android.synthetic.main.activity_session_overview.*
import java.util.*

class SessionOverviewActivity : AppCompatActivity() {

    /**
     * List of sessions
     */
    var sessions: List<QuizSession> = LinkedList<QuizSession>()

    /**
     * List of sessions to display
     */
    var displayedSessions: ArrayList<QuizSession> = ArrayList<QuizSession>()

    /**
     * List adapter
     */
    lateinit var listAdapter: SessionListAdapter

    /**
     * Listener for the search input.
     * Updates the sessions list if the text is changed.
     */
    private val searchQueryListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            updateSessionsList(query)
            return true
        }

        override fun onQueryTextChange(query: String): Boolean {
            updateSessionsList(query)
            return true
        }
    }

    /**
     * on click listener for the sessions
     * Opens a detailed view, depending on the state of the session.
     */
    private val onSessionClickListener = object : AdapterView.OnItemClickListener {
        override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
            val clickedSession = listAdapter.getItem(position)

            when {
                !clickedSession.isParticipant && clickedSession.isPrivate -> {
                    // the user does not participate this private session
                    // he should enter a password
                    // TODO: dialog for password
                }

                !clickedSession.isParticipant -> {
                    // the user does not participate the session
                    val intent = Intent(this@SessionOverviewActivity, SessionDetailActivity::class.java)
                    intent.putExtra(getString(R.string.intent_extra_session_clicked), clickedSession)
                    startActivity(intent)
                }

                else -> {
                    // the user participates the session
                    val intent = Intent(this@SessionOverviewActivity, SessionParticipantDetailActivity::class.java)
                    intent.putExtra(getString(R.string.intent_extra_session_id), clickedSession.id)
                    startActivity(intent)
                }
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_overview)

        // initialize the list adapter and set it to the list view
        listAdapter = SessionListAdapter(this, displayedSessions)
        activity_session_overview_content.adapter = listAdapter
        // set the on click listener for the sessions
        activity_session_overview_content.onItemClickListener = onSessionClickListener

        // initialize search
        // sets the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        // assumes current activity is the searchable activity
        activity_session_overview_search.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        // sets the query listener to the search field
        activity_session_overview_search.setOnQueryTextListener(searchQueryListener)
    }

    override fun onResume() {
        super.onResume()

        // get the sessions list
        val provider = SessionProvider()
        sessions = provider.getAllSessionSorted()
        // initialize content
        updateSessionsList("")

        // reset search
        activity_session_overview_search.setQuery("", true)
    }

    /**
     * Makes a regex from the given query.
     * Initializes the {@link #displayedSessions} with {@link #sessions}. Then filters only the matching sessions. Looking for the name and the category of each session.
     * Updates the {@link #listAdapter}
     *
     * @param query the query for filtering the sessions
     */
    private fun updateSessionsList(query: String) {
        val regex = Regex(".*" + query + ".*")
        displayedSessions = ArrayList<QuizSession>(sessions)
        listAdapter.clear()
        listAdapter.addAll(displayedSessions.filter { it.name.matches(regex) || it.category.matches(regex) })
    }

}
