package org.mistu.android.exam.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mistu.android.exam.R;
import org.mistu.android.exam.adapter.SummaryListAdapter;
import org.mistu.android.exam.db.ExamDbContract;
import org.mistu.android.exam.db.ExamsDbHelper;


public class SummaryListFragment extends Fragment {
   
    private OnSummaryListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private SummaryListAdapter adapter;
    private SQLiteDatabase mDb;
    private Context context;

    public SummaryListFragment() {
        // Required empty public constructor
    }

    
    public static SummaryListFragment newInstance() {
        SummaryListFragment fragment = new SummaryListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = inflater.getContext();
        Log.d("SUMMARY_LIST_FRAG ", "present");
        View fragView = inflater.inflate(R.layout.fragment_summary_list, container, false);

        recyclerView = (RecyclerView) fragView.findViewById(R.id.summary_frag_rv);
        adapter = new SummaryListAdapter(context, getDbCursor(), mListener);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return fragView;
    }

    private Cursor getDbCursor() {
        mDb = new ExamsDbHelper(context).getReadableDatabase();
        Cursor cursor =  mDb.query(
                ExamDbContract.ExamsTaken.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ExamDbContract.ExamsTaken.COLUMN_NAME_TIMESTAMP + " DESC"
        );
        mListener.onSummaryCount(cursor.getCount());
        return cursor;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSummaryListFragmentInteractionListener) {
            mListener = (OnSummaryListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSummaryListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void refreshExamSummaryList(){
        adapter.swapCursor(getDbCursor());
    }

    public interface OnSummaryListFragmentInteractionListener {
        void onSummaryListFragmentInteraction(long id, String responseAnswerJsonString, String timeTaken);
        void onSummaryCount(int listSize);
    }
}
