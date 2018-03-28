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

    private val onSessionClickListener = object : AdapterView.OnItemClickListener {
        override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
            val clickedSession = listAdapter.getItem(position)
            val copyClickedSession = QuizSession(clickedSession.name, clickedSession.category, clickedSession.enddate, clickedSession.isOwner, clickedSession.isParticipant, clickedSession.isPrivate)

            when {
                !clickedSession.isParticipant && clickedSession.isPrivate -> {
                    // TODO: dialog for password
                }
                !clickedSession.isParticipant -> {
                    val intent = Intent(this@SessionOverviewActivity, SessionDetailActivity::class.java)
                    intent.putExtra(getString(R.string.intent_extra_session_clicked), copyClickedSession)
                    startActivity(intent)
                }
                else -> {
                    // TODO: detail view with participants
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

        // get the sessions list
        val provider = SessionProvider()
        sessions = provider.getAllSessionSorted()
        // initialize content
        updateSessionsList("")
    }

    override fun onResume() {
        super.onResume()

        // clear search
        activity_session_overview_search.setQuery("", true)
    }

    /**
     * Makes a regex from the given query.
     * Initializes the {@link #displayedSessions} with {@link #sessions}. Then filters only the matches sessions. Looking for the name and the category of each session.
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
