package com.quizarena.quiz

// TODO: correct implementation of api
class QuizApi {

    // mock up
    //TODO: prove arrays size
    fun getQuestions(sessionID: Int): ArrayList<Question> {
        var array = ArrayList<Question>(10)
        for (i in 1..10) {
            array.add(Question("Frage" + i, "Antwort 1", "Antwort 2", "Antwort 3", "Antwort 4", "Antwort 1"))
        }
        return array
    }

}