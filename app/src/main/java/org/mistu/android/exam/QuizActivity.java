package org.mistu.android.exam;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.mistu.android.exam.db.DbUtils;
import org.mistu.android.exam.db.ExamDbContract;
import org.mistu.android.exam.db.ExamsDbHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity
    implements ProblemFragment.OnProblemFragmentInteractionListener{

    private ViewPager viewPager;
    private PagerAdapter adapter;
    private int problemsCount;
    private List<Problem> problemList;
    private String urlToHit;
    private Boolean showAnswer;
    private String quizType;

    private Map<Integer, Pair> questionResponseAnswerMap;

    private SQLiteDatabase mDb;
    private int timeTakenInSeconds;
    private boolean isStopWatchRunning;
    private TextView stopWatchTV;
    private TabLayout tabLayout;
    private ProgressDialog pDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        urlToHit = bundle.getString(AppConstants.URL_TO_HIT);
        quizType = bundle.getString(AppConstants.TEST_TYPE);

        initVariables();
        getProblemsFromServer(urlToHit);
    }

    private void initVariables() {
        showAnswer = false;
        viewPager = (ViewPager) findViewById(R.id.activity_quiz_view_pager);
        problemList = new ArrayList<>();
        ExamsDbHelper dbHelper = new ExamsDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        timeTakenInSeconds = 0;
        isStopWatchRunning = false;
        stopWatchTV = (TextView) findViewById(R.id.quiz_stop_watch);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        questionResponseAnswerMap = new LinkedHashMap<>();
        context = this;
    }

    private void getProblemsFromServer(String urlToHit){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Quiz...");
        pDialog.show();
        JsonArrayRequest req = new JsonArrayRequest(urlToHit,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Log.d("Volley Response", response.toString());
                        try {
                            problemList = new ObjectMapper().readValue(response.toString(), new TypeReference<ArrayList<Problem>>() {});
                            pDialog.hide();
                            setViewPager();

                        } catch (IOException e) {
                            pDialog.hide();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            pDialog.hide();
                            Log.e("Volley Error", "Error: " + volleyError.getMessage());
                            String message = null;
                            if (volleyError instanceof NetworkError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof ServerError) {
                                message = "The server could not be found. Please try again after some time!!";
                            } else if (volleyError instanceof AuthFailureError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof ParseError) {
                                message = "Parsing error! Please try again after some time!!";
                            } else if (volleyError instanceof TimeoutError) {
                                message = "Connection TimeOut! Please check your internet connection.";
                            }
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                            finish();
                        }
                }
        );
        // Adding request to request queue
        App.getInstance().addToRequestQueue(req);
    }

    private void setViewPager() {
        adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        isStopWatchRunning = true;
        startStopWatch();
        for(Problem problem : problemList) {
            questionResponseAnswerMap.put(problem.getId(), null);
        }
    }

    private void startStopWatch(){

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = timeTakenInSeconds/60;
                int seconds = timeTakenInSeconds%60;

                String time = String.format("%02d:%02d", minutes, seconds);
                stopWatchTV.setText(time);
                if(isStopWatchRunning){
                    timeTakenInSeconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    public void onProblemFragmentInteraction(int problemId, String response, String answer ) {
        Pair responseAnswerPair = new Pair(response, answer);
        questionResponseAnswerMap.put(problemId, responseAnswerPair);

    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ProblemFragment.newInstance(problemList.get(position), position, showAnswer, null);
        }

        @Override
        public int getCount() {
            return problemList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return String.valueOf(position+1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_submit_test) {
            isStopWatchRunning = false;
            showAlertDialog(R.string.quiz_submit_alert);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        isStopWatchRunning = false;
        showAlertDialog(R.string.quiz_back_press_alert);
    }

    void showAlertDialog(int alertTitle) {
        DialogFragment newFragment = AlertDialogFragment.newInstance(alertTitle);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void doPositiveClick() {
        Log.i("FragmentAlertDialog", "Positive click!");
        verifyAndStoreTestResult();
        finish();
    }

    public void doNegativeClick() {
        isStopWatchRunning = true;
        Log.i("FragmentAlertDialog", "Negative click!");
    }

    private void verifyAndStoreTestResult() {
        int correctAnswers = 0;
        int attemptedQuestions = 0;
        String responseAnswerJsonArray;

        for(Map.Entry<Integer, Pair> entry : questionResponseAnswerMap.entrySet()) {
            int problemId = entry.getKey();
            Pair pair = entry.getValue();
            if(pair != null) {
                attemptedQuestions++;
               if (pair.areFieldsEqual()) {
                   correctAnswers++;
               }
                Log.d("Problem Id " + problemId, "  : Response : " + pair.first + " , Answer " + pair.second);
            }
        }

        try {
            responseAnswerJsonArray = new ObjectMapper().writeValueAsString(questionResponseAnswerMap);
            Log.d("QUESTION_RESPONSE_ARRAY", responseAnswerJsonArray);

        }catch (IOException ex){
            ex.printStackTrace();
            return;
        }


        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM d, yyyy 'at' hh:mm:ss a");
        String currentDateTime = dateFormatter.format(new Date());



        ContentValues cv = new ContentValues();
        cv.put(ExamDbContract.ExamsTaken.COLUMN_NAME_TITLE, quizType + " : " + currentDateTime);
        cv.put(ExamDbContract.ExamsTaken.COLUMN_NAME_ANSWER_RESPONSE_MAP, responseAnswerJsonArray);
        cv.put(ExamDbContract.ExamsTaken.COLUMN_NAME_TIME_TAKEN, timeTakenInSeconds);
        cv.put(ExamDbContract.ExamsTaken.COLUMN_NAME_TOTAL_QUESTION_COUNT, problemList.size());
        cv.put(ExamDbContract.ExamsTaken.COLUMN_NAME_ATTEMPED_QUESTION_COUNT, attemptedQuestions);
        cv.put(ExamDbContract.ExamsTaken.COLUMN_NAME_CORRECT_QUESTION_COUNT, correctAnswers);

        DbUtils.insertExamRow(mDb, cv);
    }
}
