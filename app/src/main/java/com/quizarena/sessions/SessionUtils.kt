package com.quizarena.sessions

import android.content.Context
import com.quizarena.R

class SessionUtils {

    companion object {

        /**
         * Proves if the given session is still active.
         *
         * @param session the session of which to know if it's active
         * @return true, if the session is active, false otherwise
         */
        fun isSessionActive(session: QuizSession): Boolean {
            return session.enddate.time > System.currentTimeMillis()
        }

        /**
         * Gets the left time for a session and return a string representing this time.
         * Returns string that presents termination if the session already terminated.
         *
         * @param session the session for which you want to have ththe left time
         * @return a string representing the left time or termination
         */
        fun getDurationString(context: Context, session: QuizSession): String {
            if (isSessionActive(session)) {
                val durationInSeconds = (session.enddate.time - System.currentTimeMillis()) / 1000
                return context.getString(R.string.left_time, durationInSeconds / 3600, (durationInSeconds % 3600) / 60)
            } else {
                return context.getString(R.string.terminated)
            }
        }

    }

}