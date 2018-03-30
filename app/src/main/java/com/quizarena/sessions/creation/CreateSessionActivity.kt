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
    }

    fun initializeCategories() {
        val array = CategoryApi().getAllCategories()
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, array)
        activity_create_session_form_category.adapter = adapter
    }
}
