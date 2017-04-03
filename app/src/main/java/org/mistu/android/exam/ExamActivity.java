package org.mistu.android.exam;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import static android.R.attr.configure;

public class ExamActivity extends AppCompatActivity
    implements ProblemListFragment.OnProblemListFragmentInteractionListener {

    private ProblemListFragment problemListFragment;
    private String urlToHit;
    private String typeOfExam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        typeOfExam = bundle.getString(AppConstants.TEST_TYPE);
        configureUrlToHit();
        setProblemListFragment(urlToHit);
    }

    private void configureUrlToHit() {
        urlToHit = "http://mistu.org/etutor/api/getProblems.php";
    }

    private void setProblemListFragment(String urlToHit) {
        problemListFragment = ProblemListFragment.newInstance(urlToHit, "");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("problemList");
        if (prev != null) {
            ft.remove(prev);
        }
        // ft.addToBackStack(null);

        ft.add(R.id.exam_frag_container, problemListFragment, "problemList");
        ft.commit();
    }

    @Override
    public void onProblemListFragmentInteraction() {

    }
}
