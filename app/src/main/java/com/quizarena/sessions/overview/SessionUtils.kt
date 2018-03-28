package com.quizarena.sessions.overview

import android.content.Context
import com.quizarena.R
import com.quizarena.sessions.QuizSession

class SessionUtils {

    companion object {

        /**
         * Gets the left time for a session and return a string representing this time.
         * Returns string that presents termination if the session already terminated.
         *
         * @param session the session for which you want to have ththe left time
         * @return a string representing the left time or termination
         */
        fun getDurationString(context: Context, session: QuizSession): String {
            val durationInSeconds = (session.enddate.time - System.currentTimeMillis()) / 1000
            if (durationInSeconds > 0) {
                return context.getString(R.string.left_time, durationInSeconds / 3600, (durationInSeconds % 3600) / 60)
            } else {
                return context.getString(R.string.terminated)
            }
        }

    }
    
}