package org.mistu.android.exam.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.mistu.android.exam.AppConstants;
import org.mistu.android.exam.R;

public class PracticeActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView instantQuizStart;
    private TextView customQuizStart;
    private TextView selectChapters;
    private TextView selectNumOfQuestions;
    private Toolbar toolbar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        initVariables();
        setToolbar();
        setFab();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initVariables() {
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        instantQuizStart = (TextView) findViewById(R.id.practice_instant_quiz_start_tv);
        customQuizStart = (TextView) findViewById(R.id.practice_custom_quiz_start_tv);
        selectChapters = (TextView) findViewById(R.id.practice_instant_quiz_select_chapter_tv);
        selectNumOfQuestions = (TextView) findViewById(R.id.practice_instant_quiz_num_questions_tv);

        instantQuizStart.setOnClickListener(this);
        customQuizStart.setOnClickListener(this);
        selectChapters.setOnClickListener(this);
        selectNumOfQuestions.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, QuizActivity.class);
        switch (v.getId()) {
            case R.id.practice_instant_quiz_select_chapter_tv:

                break;

            case R.id.practice_instant_quiz_num_questions_tv:

                break;

            case R.id.practice_instant_quiz_start_tv:
                intent.putExtra(AppConstants.URL_TO_HIT, AppConstants.INSTANT_QUIZ_URL);
                intent.putExtra(AppConstants.TEST_TYPE, AppConstants.QUIZ_TYPE_INSTANT);
                startActivity(intent);
                break;

            case R.id.practice_custom_quiz_start_tv:
                intent.putExtra(AppConstants.URL_TO_HIT, AppConstants.CUSTOM_QUIZ_URL);
                intent.putExtra(AppConstants.TEST_TYPE, AppConstants.QUIZ_TYPE_CUSTOM);
                startActivity(intent);
                break;
        }
    }
}
