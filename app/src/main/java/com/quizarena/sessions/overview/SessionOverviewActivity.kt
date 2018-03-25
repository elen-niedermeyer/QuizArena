package com.quizarena.sessions.overview

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SearchView
import com.quizarena.R
import com.quizarena.sessions.QuizSession
import com.quizarena.sessions.SessionProvider
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_overview)

        // get the sessions list
        val provider = SessionProvider()
        sessions = provider.getAllSessionSorted()

        // initialize the list adapter and set it to the list view
        listAdapter = SessionListAdapter(this, displayedSessions)
        activity_session_overview_content.adapter = listAdapter

        // sets the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        // assumes current activity is the searchable activity
        activity_session_overview_search.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        // sets the query listener to the search field
        activity_session_overview_search.setOnQueryTextListener(searchQueryListener)

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
