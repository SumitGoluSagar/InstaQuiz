package org.mistu.android.exam.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.mistu.android.exam.App;
import org.mistu.android.exam.AppConstants;
import org.mistu.android.exam.model.Pair;
import org.mistu.android.exam.model.Problem;
import org.mistu.android.exam.fragment.ProblemFragment;
import org.mistu.android.exam.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnswersActivity extends AppCompatActivity
    implements ProblemFragment.OnProblemFragmentInteractionListener {

    private static final long DEFAULT_QUIZ_ID = -1L;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Problem> problemList;
    private long quizId;
    private String urlToHit;
    private boolean showAnswer;
    private String responseAnswerJsonString;
    private Map<Integer, Pair> questionResponseAnswerMap;
    private String problemIdSetJson;
    private ProgressDialog pDialog;
    private String timeTaken;
    private int totalCount, correctCount;
    private JSONArray params;
    private LinkedHashMap<Integer, Pair> problemResponseAnswerMap;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);
        initVariables();
        if (quizId == DEFAULT_QUIZ_ID) {
            return;
        }
        getQuestionIdList();
        setAppBarView();
        getProblemsFromServer();
    }

    private void initVariables() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        problemList = new ArrayList<>();
        tabLayout = (TabLayout) findViewById(R.id.activity_answers_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.activity_answers_view_pager);
        quizId = getIntent().getLongExtra(AppConstants.DB_ROW_ID, DEFAULT_QUIZ_ID);
        urlToHit = getIntent().getStringExtra(AppConstants.URL_TO_HIT);
        responseAnswerJsonString = getIntent().getStringExtra(AppConstants.JSON_STRING);
        timeTaken = getIntent().getStringExtra(AppConstants.TIME_TAKEN);
        showAnswer = true;
        questionResponseAnswerMap = new LinkedHashMap<>();
        correctCount = 0;
        totalCount = 0;
        params = new JSONArray();
        context = this;
    }

    private void getQuestionIdList(){

        try {
            problemResponseAnswerMap = new ObjectMapper().readValue(responseAnswerJsonString, new TypeReference<LinkedHashMap<Integer, Pair>>() {});
            totalCount = problemResponseAnswerMap.size();
            for(Map.Entry<Integer, Pair> entry : problemResponseAnswerMap.entrySet()) {
                params.put(entry.getKey());
                Pair pair = entry.getValue();
                if (pair != null && pair.areFieldsEqual()) {
                    correctCount++;
                }
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void setAppBarView() {
        TextView correctCountTV = (TextView) findViewById(R.id.activity_answers_correct_questions);
        TextView totalCountTV = (TextView) findViewById(R.id.activity_answers_total_questions);

        correctCountTV.setText(String.valueOf(correctCount));
        totalCountTV.setText(String.valueOf(totalCount));
    }


    private void getProblemsFromServer() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        Log.i("PARAMS_TO_REQUEST ", params.toString());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, urlToHit, params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.i("RESPONSE_TO_REQUEST ", response.toString());
                            problemList = new ObjectMapper().readValue(response.toString(), new TypeReference<ArrayList<Problem>>() {
                            });
                            Log.i("Problems fetched ", problemList.toString());
                            pDialog.hide();
                            setViewPager();
                        } catch (IOException ex) {
                            pDialog.hide();
                            ex.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pDialog.hide();
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
                        Log.e("Volley Error", "Error: " + message + "\n\n" + volleyError.getMessage());
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                });


        // Adding request to request queue
        App.getInstance().addToRequestQueue(request);
    }

    private void setViewPager() {
        AnswersSlidePagerAdapter adapter = new AnswersSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    public class AnswersSlidePagerAdapter extends FragmentStatePagerAdapter {

        public AnswersSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Pair pair = problemResponseAnswerMap.get(problemList.get(position).getId());
            String optionSelected = (pair == null)?null:pair.first;
            return ProblemFragment.newInstance(problemList.get(position), position, showAnswer, optionSelected);
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
    public void onProblemFragmentInteraction(int position, String response, String answer) {
        // Implement if required in future.
    }
}
