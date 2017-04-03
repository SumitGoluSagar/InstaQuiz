package org.mistu.android.exam;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

public class MainActivity extends AppCompatActivity
        implements TestTypeDialogFragment.OnTestTypeFragmentInteractionListener,
                    SummaryListFragment.OnSummaryListFragmentInteractionListener{

    private List<Problem> problemList;
    private List<String> answerList;

    private String urlToHit;
    private String quizType;
    private FloatingActionButton fab;
    private TestTypeDialogFragment dialogFragment;
    private SummaryListFragment summaryFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            if (urlToHit == null){
                Log.d("URL_TO_HIT ", "IS NULL");
            }else {
                Log.d("URL_TO_HIT_SAVED ", urlToHit);
            }
            //Log.d("FAB_PROPERTY ", Integer.toString(fab.getSize()));
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initVariables();
        setSummaryListFragment();
    }

    private void initVariables() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        urlToHit = AppConstants.INSTANT_QUIZ_URL;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTestTypeDialogFragment();
            }
        });
    }

    private void setSummaryListFragment(){
        summaryFragment = SummaryListFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.summary_frag_container, summaryFragment, "summaryFrag");
        ft.commit();
    }

    private void setTestTypeDialogFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        //ft.addToBackStack(null);

        dialogFragment = TestTypeDialogFragment.newInstance();
        dialogFragment.show(ft, "dialog");
    }

    @Override
    protected void onResume() {
        super.onResume();
        summaryFragment.refreshExamSummaryList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTestTypeFragmentInteraction(String type) {
        dialogFragment.dismiss();
        Intent intent = new Intent(this, QuizActivity.class);
        if(type.equals(AppConstants.TEST_TYPE1)) {
            urlToHit = AppConstants.INSTANT_QUIZ_URL;
            quizType = AppConstants.TEST_TYPE1;
        }
        else if(type.equals(AppConstants.TEST_TYPE2)){
            urlToHit = AppConstants.CUSTOM_TEST_URL;
            quizType = AppConstants.TEST_TYPE2;
        }
        else if(type.equals(AppConstants.TEST_TYPE3)){
            urlToHit = AppConstants.FULL_TEST_URL;
            quizType = AppConstants.TEST_TYPE3;
        }
        intent.putExtra(AppConstants.URL_TO_HIT, urlToHit);
        intent.putExtra(AppConstants.TEST_TYPE, quizType);
        startActivity(intent);
    }

    @Override
    public void onSummaryListFragmentInteraction(long id, String responseAnswerJsonString, String timeTaken) {
        Intent intent = new Intent(this, AnswersActivity.class);
        intent.putExtra(AppConstants.DB_ROW_ID, id);
        intent.putExtra(AppConstants.URL_TO_HIT, AppConstants.SUMMARY_URL);
        intent.putExtra(AppConstants.JSON_STRING, responseAnswerJsonString);
        intent.putExtra(AppConstants.TIME_TAKEN, timeTaken);
        Log.i("RESPONSE_ANSWER ", responseAnswerJsonString);
        startActivity(intent);
    }

    @Override
    public void onSummaryCount(int listSize) {
        if (listSize != 0) {
            ((LinearLayout) findViewById(R.id.activity_main_init_layout)).setVisibility(View.GONE);
        }
    }
}
