package com.quizarena.sessions.creation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter

import com.quizarena.R

import kotlinx.android.synthetic.main.activity_create_session.*

class CreateSessionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_session)

        initializeCategories()

        addSubmitButtonClicklistener()

        addIsPrivateCheckboxClicklistener()
    }

    /**
     * Initializes the dropdown with all categories from the API
     */
    private fun initializeCategories() {
        val array = CategoryApi().getAllCategories()
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, array)
        activity_create_session_form_category!!.adapter = adapter
    }

    /**
     * Toggles the visibility of the session password EditText on the checkbox click
     */
    private fun addIsPrivateCheckboxClicklistener() {
        activity_create_session_isprivate_checkbox.setOnClickListener {
            // toggle password and password error
            if (activity_create_session_isprivate_checkbox.isChecked) {

                activity_create_session_isprivate_password.visibility = View.VISIBLE
                activity_create_session_error_password.visibility = View.VISIBLE
            } else {

                activity_create_session_isprivate_password.visibility = View.GONE
                activity_create_session_error_password.visibility = View.GONE
            }
        }
    }

    /**
     * If the form is valid, this calls the CreateSessionApi to create a new session
     */
    private fun addSubmitButtonClicklistener() {
        activity_create_session_submit_button.setOnClickListener {
            // check whether input is valid
            if (creationFormIsValid()) {
                // do some create session request here

                val name = activity_create_session_form_name.text.toString()
                val category = activity_create_session_form_category.selectedItem.toString()
                val duration = activity_create_session_form_duration.text.toString().toInt()

                val api = CreateSessionApi()
                if (activity_create_session_isprivate_checkbox.isChecked) {
                    // create private session request
                    val password = activity_create_session_isprivate_password.text.toString()
                    api.createSession(name, category, duration, password)
                } else {
                    // create public session request
                    api.createSession(name, category, duration)
                }
            }
        }
    }

    /**
     * Validates the input of the create session form and displays error messages if necessary
     */
    private fun creationFormIsValid() : Boolean {

        var isValid = true

        // clean all errors
        activity_create_session_error_name.text = ""
        activity_create_session_error_duration.text = ""
        activity_create_session_error_password.text = ""

        // check session name
        if (activity_create_session_form_name.text.isEmpty() || activity_create_session_form_name.text.isBlank()) {
            activity_create_session_error_name.text = getString(R.string.create_session_error_no_name)
            isValid = false
        }

        // check duration
        if (activity_create_session_form_duration.text.isEmpty()) {
            activity_create_session_error_duration.text = getString(R.string.create_session_error_no_duration)
            isValid = false
        }
        // only display the first error if it's empty
        else if (activity_create_session_form_duration.text.toString().toIntOrNull() == null) {
            activity_create_session_error_duration.text = getString(R.string.create_session_error_wrong_duration)
            isValid = false
        }

        // check password if active
        if (activity_create_session_isprivate_checkbox.isChecked && activity_create_session_isprivate_password.text.isEmpty()) {
            activity_create_session_error_password.text = getString(R.string.create_session_error_no_password)
            isValid = false
        }

        return isValid
    }
}
