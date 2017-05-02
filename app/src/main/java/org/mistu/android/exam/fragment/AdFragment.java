package org.mistu.android.exam.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.mistu.android.exam.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_URL_TO_HIT = "urlToHit";
    private static final String ARG_POSITION = "position";


    private String urlToHit;
    private int position;
    private ImageView adImageView;

    private OnFragmentInteractionListener mListener;

    public AdFragment() {
        // Required empty public constructor
    }

    public static AdFragment newInstance(String urlToHit, int position) {
        AdFragment fragment = new AdFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL_TO_HIT, urlToHit);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            urlToHit = getArguments().getString(ARG_URL_TO_HIT);
            position = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_ad, container, false);
        adImageView = (ImageView) fragView.findViewById(R.id.home_ad_image_view);

        Glide.with(inflater.getContext())
                .load(urlToHit)
                .centerCrop()
                .placeholder(R.drawable.bg_hori_light)
                .into(adImageView);

        adImageView.setOnClickListener(this);
        return  fragView;
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

    @Override
    public void onClick(View v) {
        mListener.onAdFragmentInteraction(position);
    }

    public interface OnFragmentInteractionListener {
        void onAdFragmentInteraction(int position);
    }
}
