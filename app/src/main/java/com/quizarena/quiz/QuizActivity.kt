package com.quizarena.quiz

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.content.res.AppCompatResources
import android.view.View
import android.widget.Button
import com.quizarena.R
import com.quizarena.sessions.SessionApi
import com.quizarena.sessions.detailView.SessionParticipantDetailActivity
import com.quizarena.user.CurrentUser
import com.quizarena.user.credentials.CredentialsUpdater
import kotlinx.android.synthetic.main.activity_quiz.*
import org.jetbrains.anko.alert

class QuizActivity : AppCompatActivity() {

    /**
     * id of the session, initialized in onCreate
     */
    private var sessionID = ""
    private var password: String? = null

    /**
     * questions for the quiz
     */
    private var questions: List<Question>? = null

    /**
     * question counter
     */
    private var counter = 0

    /**
     * counter for correct answers
     */
    private var correctAnswers = 0

    /**
     * array of answer buttons
     * they have all the same behavior
     */
    private lateinit var answerButtons: Array<Button>

    /**
     * on click listener for the answer buttons
     */
    private val onAnswerClickListener = object : View.OnClickListener {

        override fun onClick(view: View?) {
            if (view is Button) {
                // disable the buttons until the next question
                answerButtons.forEach { it.isEnabled = false }

                // figure out if the answer was right or wrong
                if (view.text == questions!![counter].correctAnswer) {
                    // the answer is correct
                    ViewCompat.setBackgroundTintList(view, AppCompatResources.getColorStateList(this@QuizActivity, R.color.correct_answer))
                    view.setTextColor(resources.getColor(android.R.color.black))
                    correctAnswers++

                } else {
                    // the answer is wrong
                    ViewCompat.setBackgroundTintList(view, AppCompatResources.getColorStateList(this@QuizActivity, R.color.wrong_answer))
                    view.setTextColor(resources.getColor(android.R.color.black))
                }

                // short delay for showing the result to the user
                view.postDelayed(
                        // behavior after delay
                        Runnable {
                            // reset buttons
                            val buttonStyle = obtainStyledAttributes(R.style.button, intArrayOf(android.R.attr.backgroundTint, android.R.attr.textColor))
                            ViewCompat.setBackgroundTintList(view, AppCompatResources.getColorStateList(this@QuizActivity, buttonStyle.getResourceId(0, R.color.colorPrimary)))
                            view.setTextColor(buttonStyle.getColor(1, resources.getColor(R.color.text_button)))
                            buttonStyle.recycle()
                            answerButtons.forEach { it.isEnabled = true }

                            // next question
                            counter++
                            setNextQuestion()
                        },
                        // delay time
                        500
                );
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // get session id from intent
        sessionID = intent.getStringExtra(getString(R.string.intent_extra_session_id))
        password = intent.getStringExtra(getString(R.string.intent_extra_session_password))
        // get questions for this session
        val quizApi = QuizApi(this@QuizActivity)
        questions = quizApi.getQuestions(sessionID)
        if (questions == null) {
            // request failed
            // show an error message and finish the activity
            alert {
                title = getString(R.string.error)
                message(quizApi.state)
                positiveButton(getString(R.string.ok)) { }
            }.show()
            this.finish()
        }

        // initialize answer buttons
        answerButtons = arrayOf(activity_quiz_button_answer_1, activity_quiz_button_answer_2, activity_quiz_button_answer_3, activity_quiz_button_answer_4)
        answerButtons.forEach { it.setOnClickListener(onAnswerClickListener) }
    }

    override fun onResume() {
        super.onResume()

        // initialize content
        setNextQuestion()
    }

    override fun onStart() {
        super.onStart()

        val sessionApi = SessionApi(this@QuizActivity)
        if (!sessionApi.addParticipant(sessionID, CurrentUser.accountName, password)) {
            // request failed
            // show an error message and terminate the activity
            alert {
                title = getString(R.string.error)
                message(sessionApi.state)
                positiveButton(getString(R.string.ok)) { }
            }.show()
            this.finish()
        }
    }

    override fun onPause() {
        super.onPause()

        if (!CredentialsUpdater(this@QuizActivity).updateScore(sessionID, correctAnswers)) {
            // request failed
            // show an error message
            alert {
                title = getString(R.string.error)
                message(getString(R.string.error_general))
                positiveButton(getString(R.string.ok)) { }
            }.show()
        }
    }

    /**
     * Shows the next question or finishs it.
     */
    private fun setNextQuestion() {
        if (counter < questions!!.size) {
            // go on with the next question
            val currentQuestion = questions!![counter]
            // set all text for this question
            activity_quiz_question.text = currentQuestion.question
            activity_quiz_button_answer_1.text = currentQuestion.answers[0]
            activity_quiz_button_answer_2.text = currentQuestion.answers[1]
            activity_quiz_button_answer_3.text = currentQuestion.answers[2]
            activity_quiz_button_answer_4.text = currentQuestion.answers[3]

        } else {
            // quiz finished
            // start next activity
            startDetailView()
        }

    }

    /**
     * Starts the {@link SessionParticipantDetailActivity} with the session id.
     * Finishes this activity.
     */
    private fun startDetailView() {
        val intent = Intent(this@QuizActivity, SessionParticipantDetailActivity::class.java)
        intent.putExtra(getString(R.string.intent_extra_session_id), sessionID)
        startActivity(intent)

        this.finish()
    }
}
