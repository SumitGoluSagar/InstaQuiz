package org.mistu.android.exam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.mistu.android.exam.R;
import org.mistu.android.exam.adapter.BatchAdapter;
import org.mistu.android.exam.model.Batch;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BatchActivity extends AppCompatActivity {

    private Map<String, Batch> keyBatchMap;
    private List<Batch> batchList;
    private RecyclerView recyclerView;
    private BatchAdapter adapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference batchDbRef;
    private ChildEventListener batchEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch);
        setToolbar();
        initVariables();
        setRecyclerView();
        attachBatchEventListener();
    }

    private void setRecyclerView() {
        adapter = new BatchAdapter(keyBatchMap, batchList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void attachBatchEventListener() {
        if (batchEventListener == null) {
            batchEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i("onChildAdded", "inside");
                    String key = dataSnapshot.getKey();
                    Batch batch = dataSnapshot.getValue(Batch.class);
                    adapter.addBatch(key, batch);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            batchDbRef.addChildEventListener(batchEventListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // attachBatchEventListener();
    }

    @Override
    protected void onPause() {
        // detachBatchEventListener();
        super.onPause();
    }

    private void detachBatchEventListener() {
        if (batchEventListener != null) {
            batchDbRef.removeEventListener(batchEventListener);
            batchEventListener = null;
            adapter.clearAdapter();
        }
    }

    private void initVariables() {
        keyBatchMap = new LinkedHashMap<>();
        batchList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.activity_batch_rv);
        firebaseDatabase = FirebaseDatabase.getInstance();
        batchDbRef = firebaseDatabase.getReference().child("batches");
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



}
