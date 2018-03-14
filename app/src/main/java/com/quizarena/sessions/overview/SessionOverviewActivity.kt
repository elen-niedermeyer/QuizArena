package com.quizarena.sessions.overview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.quizarena.R
import com.quizarena.sessions.SessionProvider

class SessionOverviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_overview)

        val provider = SessionProvider()
        var ownedSessions = provider.getOwnedSessions()
        var participatedSessions = provider.getParticipatedSessions()
        var otherSessions = provider.getOpenSessions()
    }
}
