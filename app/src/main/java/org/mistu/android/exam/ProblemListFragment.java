package org.mistu.android.exam;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ProblemListFragment extends Fragment {
    
    private static final String URL_TO_HIT = "urlToHit";
    private static final String ARG_PARAM2 = "param2";

    private String urlToHit;
    private String mParam2;
    private ObjectMapper mapper;
    private ProblemListAdapter adapter;
    private RecyclerView recyclerView;
    private List<Problem> problemList;


    private OnProblemListFragmentInteractionListener mListener;

    public ProblemListFragment() {
        // Required empty public constructor
    }

  
    public static ProblemListFragment newInstance(String urlToHit, String param2) {
        ProblemListFragment fragment = new ProblemListFragment();
        Bundle args = new Bundle();
        args.putString(URL_TO_HIT, urlToHit);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            urlToHit = getArguments().getString(URL_TO_HIT);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        problemList = new ArrayList<>();
        mapper = new ObjectMapper();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_problem_list, container, false);
        recyclerView = (RecyclerView) fragView.findViewById(R.id.problem_frag_rv);
        adapter = new ProblemListAdapter(problemList, mListener);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(inflater.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        getProblemsFromServer(urlToHit);
        return  fragView;
    }

    private void getProblemsFromServer(String urlToHit){
        JsonArrayRequest req = new JsonArrayRequest(urlToHit,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            problemList = mapper.readValue(response.toString(), new TypeReference<ArrayList<Problem>>() {});
                            Log.d("ELEMENT_LIST", problemList.toString());
                            adapter.setProblemList(problemList);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", "Error: " + error.getMessage());
            }
        });
        // Adding request to request queue
        App.getInstance().addToRequestQueue(req);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProblemListFragmentInteractionListener) {
            mListener = (OnProblemListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnProblemListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    
    public interface OnProblemListFragmentInteractionListener {
        void onProblemListFragmentInteraction();
    }
}
