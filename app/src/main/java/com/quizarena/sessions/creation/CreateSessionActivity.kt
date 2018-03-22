package com.quizarena.sessions.creation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter

import com.quizarena.R
import kotlinx.android.synthetic.main.activity_create_session.*

class CreateSessionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_session)

        initializeCategories()
    }

    fun initializeCategories() {
        val array = arrayOf("default", "other")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, array)
        activity_create_session_form_category.adapter = adapter
    }
}
