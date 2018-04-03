package com.quizarena.sessions.creation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter

import com.quizarena.R

import kotlinx.android.synthetic.main.activity_create_session.*

class CreateSessionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_session)

        initializeCategories()

        activity_create_session_submit_button.setOnClickListener {
            // check whether input is valid
            if (creationFormIsValid()) {
                // do some create session request here

                val name = activity_create_session_form_name.text.toString()
                val category = activity_create_session_form_category.selectedItem.toString()
                val duration = activity_create_session_form_duration.text.toString().toInt()

                val api = CreateSessionApi()
                api.createSession(name, category, duration)
            }
        }
    }

    /**
     * initializes the dropdown with all categories from the API
     */
    private fun initializeCategories() {
        val array = CategoryApi().getAllCategories()
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, array)
        activity_create_session_form_category.adapter = adapter
    }

    /**
     * validates the input of the create session form and displays error messages if necessary
     * TODO: display error messages
     */
    private fun creationFormIsValid() : Boolean {
        if (activity_create_session_form_name.text.isEmpty())
            return false
        if (activity_create_session_form_name.text.isBlank())
            return false
        if (activity_create_session_form_duration.text.isEmpty())
            return false
        if (activity_create_session_form_duration.text.toString().toIntOrNull() == null)
            return false
        return true
    }
}
