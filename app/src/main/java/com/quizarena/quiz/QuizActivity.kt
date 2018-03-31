package com.quizarena.quiz

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.quizarena.R
import com.quizarena.authorization.Credentials
import com.quizarena.sessions.SessionApi
import com.quizarena.sessions.detailView.SessionParticipantDetailActivity
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity() {

    /**
     * id of the session, initialized in onCreate
     */
    private var sessionID = 0

    /**
     * questions for the quiz
     */
    private lateinit var questions: List<Question>

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

        // TODO: correct backgrounds
        override fun onClick(view: View?) {
            if (view is Button) {
                // disable the buttons until the next question
                answerButtons.forEach { it.isEnabled = false }

                // figure out if the answer was right or wrong
                if (view.text == questions[counter].correctAnswer) {
                    // the answer is correct
                    view.setBackgroundResource(R.color.correct_answer)
                    correctAnswers++

                } else {
                    // the answer is wrong
                    view.setBackgroundResource(R.color.wrong_answer)
                }

                // short delay for showing the result to the user
                view.postDelayed(
                        // behavior after delay
                        Runnable {
                            // reset buttons
                            view.setBackgroundResource(R.color.primary_material_light)
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
        sessionID = intent.getIntExtra(getString(R.string.intent_extra_session_id), 0)
        // get questions for this session
        questions = QuizApi().getQuestions(sessionID)

        // initialize answer buttons
        answerButtons = arrayOf(activity_quiz_button_answer_1, activity_quiz_button_answer_2, activity_quiz_button_answer_3, activity_quiz_button_answer_4)
        answerButtons.forEach { it.setOnClickListener(onAnswerClickListener) }
    }

    override fun onResume() {
        super.onResume()

        // initialize content
        setNextQuestion()
    }

    /**
     * Shows the next question or finishs it.
     */
    private fun setNextQuestion() {
        if (counter < questions.size) {
            // go on with the next question
            val currentQuestion = questions[counter]
            // set all text for this question
            activity_quiz_question.text = currentQuestion.question
            activity_quiz_button_answer_1.text = currentQuestion.answer1
            activity_quiz_button_answer_2.text = currentQuestion.answer2
            activity_quiz_button_answer_3.text = currentQuestion.answer3
            activity_quiz_button_answer_4.text = currentQuestion.answer4

        } else {
            // quiz finished
            // set score
            if (SessionApi().setScore(Credentials.accountName, correctAnswers)) {
                // start next activity
                startDetailView()
            } else {
                // TODO: error handling in request
            }
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