package com.quizarena.quiz

import android.content.Context
import com.quizarena.ApiErrors
import com.quizarena.R
import org.jetbrains.anko.doAsyncResult
import java.net.UnknownHostException

class QuizApi(val context: Context) {

    var state: String = ""
        private set

    /**
     * Requests the given session and extracts the questions
     *
     * @return a list of {@link Question}
     */
    fun getQuestions(sessionID: String): List<Question>? {
        try {
            val response = doAsyncResult {
                return@doAsyncResult khttp.get(
                        url = context.getString(R.string.baseurl) + context.getString(R.string.endpoint_session_specific, sessionID))
            }.get()

            val statusCode = response.statusCode
            val responseState = response.text

            if (statusCode == 200) {
                // session request was successful
                // make list of question objects
                val questionsObjects = ArrayList<Question>(10)
                val json = response.jsonArray
                val sessionJson = json.getJSONObject(0)
                val questionsJson = sessionJson.getJSONArray("questions")
                for (i in 0..questionsJson.length() - 1) {
                    val questionJson = questionsJson.getJSONObject(i)
                    val question = questionJson.getString("question")
                    val answersJson = questionJson.getJSONObject("answers")
                    val correctAnswer = answersJson.getString("right_answer")
                    val wrongAnswers = answersJson.getJSONArray("wrong_answers")
                    val answers = listOf<String>(correctAnswer, wrongAnswers.getString(0), wrongAnswers.getString(1), wrongAnswers.getString(2))

                    questionsObjects.add(Question(question, answers.shuffled().toTypedArray(), correctAnswer))
                }

                return questionsObjects.shuffled()

            } else {
                // session request failed
                this.state = ApiErrors.getError(context, responseState)
                return null
            }

        } catch (e: Exception) {
            if (e.cause is UnknownHostException) {
                state = context.getString(R.string.error_network)
            } else {
                this.state = ApiErrors.getError(context, e.message!!)
            }
            return null
        }
    }

}