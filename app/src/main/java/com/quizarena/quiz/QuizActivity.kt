package com.quizarena.quiz

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.quizarena.R
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity() {

    private var counter = 0
    private var correctAnswers = 0
    private lateinit var questions: List<Question>

    private val onAnswerClickListener = object : View.OnClickListener {

        // TODO: correct backgrounds
        override fun onClick(view: View?) {
            if (view is Button) {
                if (view.text == questions[counter].correctAnswer) {
                    view.setBackgroundResource(R.color.correct_answer)
                    correctAnswers++
                } else {
                    view.setBackgroundResource(R.color.wrong_answer)
                }

                view.postDelayed(Runnable {
                    view.setBackgroundResource(R.color.primary_material_light)

                    counter++
                    setNextQuestion()
                }, 1000);
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val sessionID = intent.getIntExtra(getString(R.string.intent_extra_session_id), 0)
        questions = QuizApi().getQuestions(sessionID)

        activity_quiz_button_answer_1.setOnClickListener(onAnswerClickListener)
        activity_quiz_button_answer_2.setOnClickListener(onAnswerClickListener)
        activity_quiz_button_answer_3.setOnClickListener(onAnswerClickListener)
        activity_quiz_button_answer_4.setOnClickListener(onAnswerClickListener)
    }

    override fun onResume() {
        super.onResume()

        setNextQuestion()
    }

    private fun setNextQuestion() {
        if (counter < questions.size) {
            val currentQuestion = questions[counter]
            activity_quiz_question.text = currentQuestion.question
            activity_quiz_button_answer_1.text = currentQuestion.answer1
            activity_quiz_button_answer_2.text = currentQuestion.answer2
            activity_quiz_button_answer_3.text = currentQuestion.answer3
            activity_quiz_button_answer_4.text = currentQuestion.answer4
        }
    }
}
