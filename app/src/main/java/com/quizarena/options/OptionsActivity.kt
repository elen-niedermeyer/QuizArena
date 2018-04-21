package com.quizarena.options

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.quizarena.R
import com.quizarena.user.User
import com.quizarena.user.login.LoginActivity
import com.quizarena.user.logout.Logout
import com.quizarena.user.modification.CredentialsUpdater
import kotlinx.android.synthetic.main.activity_options.*


class OptionsActivity : AppCompatActivity() {

    private val onLogoutClick = View.OnClickListener {
        Logout(this@OptionsActivity).logout()
        // logged out
        // start login activity
        startActivity(Intent(this@OptionsActivity, LoginActivity::class.java))
        this@OptionsActivity.finish()
    }

    private val onDisplayNameChangeClick = View.OnClickListener {
        // build an dialog
        val builder = AlertDialog.Builder(this@OptionsActivity)
        builder.setTitle(getString(R.string.display_name_new))
        val viewInflated = LayoutInflater.from(this@OptionsActivity).inflate(R.layout.dialog_text_input, null)
        val input = viewInflated.findViewById(R.id.input) as EditText
        builder.setView(viewInflated)
        builder.setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
            val m_Text = input.text.toString()
            // TODO: handle failure
            //TODO: reload
            CredentialsUpdater(this@OptionsActivity).changeDisplayName(m_Text)
        })
        builder.setNegativeButton(android.R.string.cancel, DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        activity_options_account_name.text = User.accountName
        activity_options_button_logout.setOnClickListener(onLogoutClick)
        activity_options_display_name.text = User.displayName
        activity_options_button_change_name.setOnClickListener(onDisplayNameChangeClick)

    }
}
