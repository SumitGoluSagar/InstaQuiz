package org.mistu.android.exam.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import org.mistu.android.exam.model.Problem;
import org.mistu.android.exam.R;


public class ProblemFragment extends Fragment
    implements View.OnClickListener{
   
    private static final String ARG_PROBLEM = "problem";
    private static final String ARG_POSITION = "position";
    private static final String ARG_SHOW_ANSWER = "showAnswer";
    private static final String ARG_OPTION_SELECTED = "selected";

    private Problem problem;
    private int position;
    private boolean showAnswer;

    //Only when used as answerScreen
    private String optionSelected;

    private TextView questionTV;
    private RadioButton option1RB;
    private RadioButton option2RB;
    private RadioButton option3RB;
    private RadioButton option4RB;


    private OnProblemFragmentInteractionListener mListener;

    public ProblemFragment() {
        // Required empty public constructor
    }

    
    public static ProblemFragment newInstance(Problem problem, int position, boolean showAnswer, String optionSelected) {
        ProblemFragment fragment = new ProblemFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROBLEM, problem);
        args.putInt(ARG_POSITION, position);
        args.putBoolean(ARG_SHOW_ANSWER, showAnswer);
        args.putString(ARG_OPTION_SELECTED, optionSelected);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            problem = (Problem) getArguments().getSerializable(ARG_PROBLEM);
            position = getArguments().getInt(ARG_POSITION);
            showAnswer = getArguments().getBoolean(ARG_SHOW_ANSWER);
            optionSelected = getArguments().getString(ARG_OPTION_SELECTED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        View fragView = inflater.inflate(R.layout.fragment_problem, container, false);
        setProblemView(fragView);
        return fragView;
    }

    private void setProblemView(View fragView){
        Log.i("PROBLEM_FRAG ", "setProblemView " + problem);
        questionTV = (TextView) fragView.findViewById(R.id.frag_problem_question_tv);
        option1RB = (RadioButton) fragView.findViewById(R.id.radio_option1);
        option2RB = (RadioButton) fragView.findViewById(R.id.radio_option2);
        option3RB = (RadioButton) fragView.findViewById(R.id.radio_option3);
        option4RB = (RadioButton) fragView.findViewById(R.id.radio_option4);

        questionTV.setText(getTextAsSpannable(problem.getQuestion()));
        option1RB.setText(getTextAsSpannable(problem.getOption1()));
        option2RB.setText(getTextAsSpannable(problem.getOption2()));
        option3RB.setText(getTextAsSpannable(problem.getOption3()));
        option4RB.setText(getTextAsSpannable(problem.getOption4()));

        if(!showAnswer) { // If it is a quiz section
            option1RB.setOnClickListener(this);
            option2RB.setOnClickListener(this);
            option3RB.setOnClickListener(this);
            option4RB.setOnClickListener(this);
        }
        else {  //If it is an answer section
            option1RB.setEnabled(false);
            option2RB.setEnabled(false);
            option3RB.setEnabled(false);
            option4RB.setEnabled(false);
            if (optionSelected != null) {
                RadioButton radioButtonSelected = getRadioButton(optionSelected);
                radioButtonSelected.setChecked(true);
            }
            RadioButton radioButtonCorrect = getRadioButton(problem.getCorrectOption().trim());
            if (radioButtonCorrect != null) {
                if (Build.VERSION.SDK_INT >= 21) {
                    radioButtonCorrect.setBackground(getResources().getDrawable(R.drawable.correct_ans_rect, null));
                } else {
                    radioButtonCorrect.setBackground(getResources().getDrawable(R.drawable.correct_ans_rect));
                }
            }

        }

    }

    private RadioButton getRadioButton(String option) {
        RadioButton correctRB = null;
        switch (option){
            case "a":
                correctRB = option1RB;
                break;

            case "b":
                correctRB = option2RB;
                break;

            case "c":
                correctRB = option3RB;
                break;

            case "d":
                correctRB = option4RB;
                break;
        }
        return correctRB;
    }

    private Spanned getTextAsSpannable(String text){
        Spanned textAsSpanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textAsSpanned = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        }
        else {
            textAsSpanned = Html.fromHtml(text);
        }
        return textAsSpanned;
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProblemFragmentInteractionListener) {
            mListener = (OnProblemFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnProblemFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        String response = null;
        switch (view.getId()) {
            case R.id.radio_option1:
                response = "a";
                break;

            case R.id.radio_option2:
                response = "b";
                break;

            case R.id.radio_option3:
                response = "c";
                break;

            case R.id.radio_option4:
                response = "d";
                break;
        }
        mListener.onProblemFragmentInteraction(problem.getId(), response, problem.getCorrectOption().trim());

    }

    public interface OnProblemFragmentInteractionListener {
        void onProblemFragmentInteraction(int position, String response, String answer);
    }
}
