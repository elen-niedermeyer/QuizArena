package com.quizarena.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.quizarena.R;
import com.quizarena.options.OptionsActivity;
import com.quizarena.sessions.creation.CreateSessionActivity;
import com.quizarena.sessions.overview.SessionOverviewActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button sessionOverviewButton = findViewById(R.id.activity_main_menu_button_show_sessions);
        sessionOverviewButton.setOnClickListener(onSessionOverviewButtonClickListener);

        Button createSessionButton = findViewById(R.id.activity_main_menu_button_create);
        createSessionButton.setOnClickListener(onCreateSessionButtonClickListener);

        Button optionsButton = findViewById(R.id.activity_main_menu_button_options);
        optionsButton.setOnClickListener(onOptionsButtonClickListener);
    }

    private View.OnClickListener onSessionOverviewButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainMenuActivity.this, SessionOverviewActivity.class));
        }
    };

    private View.OnClickListener onCreateSessionButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainMenuActivity.this, CreateSessionActivity.class));
        }
    };

    private View.OnClickListener onOptionsButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainMenuActivity.this, OptionsActivity.class));
        }
    };
}
