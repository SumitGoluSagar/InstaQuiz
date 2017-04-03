package org.mistu.android.exam;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TestTypeDialogFragment extends DialogFragment implements View.OnClickListener{
   
    private OnTestTypeFragmentInteractionListener mListener;
    private TextView instantQuizTV;
    private TextView createTestTV;
    private TextView fullTestTV;

    public TestTypeDialogFragment() {
        // Required empty public constructor
    }

    public static TestTypeDialogFragment newInstance() {
        TestTypeDialogFragment fragment = new TestTypeDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NORMAL;
        int theme = android.R.style.Theme_Holo_Light_Dialog;
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_test_type_dialog, container, false);

        instantQuizTV = (TextView) fragView.findViewById(R.id.instant_quiz);
        instantQuizTV.setOnClickListener(this);

        createTestTV = (TextView) fragView.findViewById(R.id.custom_test);
        createTestTV.setOnClickListener(this);

        fullTestTV = (TextView) fragView.findViewById(R.id.full_test);
        fullTestTV.setOnClickListener(this);

        getDialog().setTitle("Select Quiz Type");
        return  fragView;
    }
    

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTestTypeFragmentInteractionListener) {
            mListener = (OnTestTypeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTestTypeFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == instantQuizTV.getId()){
            mListener.onTestTypeFragmentInteraction(AppConstants.TEST_TYPE1);
        }
        else if (v.getId() == createTestTV.getId()) {
            mListener.onTestTypeFragmentInteraction(AppConstants.TEST_TYPE2);
        }
        else if (v.getId() == fullTestTV.getId()){
            mListener.onTestTypeFragmentInteraction(AppConstants.TEST_TYPE3);
        }
    }

    public interface OnTestTypeFragmentInteractionListener {
        void onTestTypeFragmentInteraction(String type);
    }
}
