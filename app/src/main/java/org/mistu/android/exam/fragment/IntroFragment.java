package org.mistu.android.exam.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.mistu.android.exam.R;


public class IntroFragment extends Fragment {
    
    private static final String ARG_POSITION = "position";
    private static final String ARG_INFO = "info";
    
    private int position;
    private String info;

    private Context context;

    private TypedArray introImages;
    private int[] introColors;
    private String[] introInfos;

    private RelativeLayout viewContainer;
    private TextView textView;
    private ImageView imageView;

    private OnFragmentInteractionListener mListener;

    public IntroFragment() {
        // Required empty public constructor
    }

   
    public static IntroFragment newInstance(int position, String info) {
        IntroFragment fragment = new IntroFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_INFO, info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
            info = getArguments().getString(ARG_INFO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_intro, container, false);
        context = inflater.getContext();
        initVariables(fragView);
        setLayout();
        return fragView;
    }

    private void initVariables(View fragView) {
        viewContainer = (RelativeLayout) fragView.findViewById(R.id.frag_intro_container);
        textView = (TextView) fragView.findViewById(R.id.frag_intro_tv);
        imageView = (ImageView) fragView.findViewById(R.id.frag_intro_iv);

        introColors = context.getResources().getIntArray(R.array.intro_slider_colors);
        introImages = context.getResources().obtainTypedArray(R.array.intro_slider_images);
        introInfos = context.getResources().getStringArray(R.array.intro_slider_info);

    }

    private void setLayout() {
        // viewContainer.setBackgroundResource(introColors[position]);
        imageView.setImageResource(introImages.getResourceId(position, -1));
        textView.setText(introInfos[position]);

        introImages.recycle();
    }
    

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onIntroFragmentInteraction(int position);
    }
}
