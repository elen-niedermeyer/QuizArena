package com.quizarena.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.quizarena.R
import com.quizarena.sessions.QuizSession
import com.quizarena.sessions.SessionApi
import com.quizarena.user.CurrentUser

class SessionSharing {

    /**
     * Makes a link and sends an intent for sharing text.
     *
     * @param context
     * @param sessionID id of the session to share
     */
    fun shareLink(context: Context, sessionID: String) {
        var text = context.getString(R.string.sharing_text)
        text += context.getString(R.string.baseurl) + context.getString(R.string.sharing_link, sessionID)

        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = "text/plain"
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.sharing_subject))
        sendIntent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.sharing_chooser_title)))
    }

    /**
     * Extracts the session id from uri and makes the containing session
     *
     * @param context
     * @param uri
     * @return the requested session
     */
    fun getSessionFromLink(context: Context, uri: Uri): QuizSession? {
        val sessionID = uri.lastPathSegment
        return getSessionFromID(context, sessionID)
    }

    /**
     * Provides a QuizSession for a given ID.
     *
     * @param context
     * @param sessionID
     * @return the requested session
     */
    fun getSessionFromID(context: Context, sessionID: String): QuizSession? {
        return SessionApi(context).getSession(sessionID, CurrentUser.accountName)
    }
}