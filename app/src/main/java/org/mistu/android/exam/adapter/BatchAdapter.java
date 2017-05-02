package org.mistu.android.exam.adapter;

import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mistu.android.exam.AppConstants;
import org.mistu.android.exam.R;
import org.mistu.android.exam.model.Batch;
import org.mistu.android.exam.model.BatchTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kedee on 1/5/17.
 */

public class BatchAdapter extends RecyclerView.Adapter<BatchAdapter.MyViewHolder>{

    private Map<String, Batch> keyBatchMap;
    private List<Batch> batchList;
    private List<String> keyList;
    private Context context;

    public BatchAdapter(Map<String, Batch> keyBatchMap, List<Batch> batchList) {
        this.keyBatchMap = keyBatchMap;
        this.batchList = batchList;
        keyList = new ArrayList<>();
    }

    public void addBatch(String key, Batch batch) {
        batchList.add(batch);
        keyList.add(key);
        keyBatchMap.put(key, batch);
        notifyDataSetChanged();
    }

    public void clearAdapter() {
        int size = batchList.size();
        keyBatchMap.clear();
        batchList.clear();
        keyList.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_batch, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Batch batch = batchList.get(position);
        holder.nameTV.setText(batch.getName());
        String std = "Class: " + batch.getStd() ;
        holder.stdTV.setText(std);
        holder.subjectTV.setText(batch.getSubject());
        List<BatchTime> batchTimeList = batch.getBatchTimeList();
        String batchTiming = "";
        for (BatchTime batchTime : batchTimeList) {
            batchTiming = batchTiming + "<b>" + batchTime.getDay() + "</b>" + ": " + batchTime.getStartTime() + " to "  +batchTime.getEndTime() + "<br>";
        }

        if (Build.VERSION.SDK_INT >= 24) {
            holder.timingTV.setText(Html.fromHtml(batchTiming, Html.FROM_HTML_MODE_LEGACY));
        }
        else {
            holder.timingTV.setText(Html.fromHtml(batchTiming));
        }
        // holder.timingTV.setText(batchTiming);
    }

    @Override
    public int getItemCount() {
        return batchList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameTV, stdTV, subjectTV, timingTV;
        View viewItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            viewItem = itemView;
            nameTV = (TextView) itemView.findViewById(R.id.batch_name_tv);
            stdTV = (TextView) itemView.findViewById(R.id.batch_std_tv);
            subjectTV = (TextView) itemView.findViewById(R.id.batch_subject_tv);
            timingTV = (TextView) itemView.findViewById(R.id.batch_timing_tv);

            viewItem.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            /*if (v.getId() == viewItem.getId()) {
                Intent intent = new Intent(context, BatchDetailActivity.class);
                intent.putExtra(AppConstants.BATCHKEY, batchList.get(getAdapterPosition()).getName());
                intent.putExtra(AppConstants.BATCHNAME, keyList.get(getAdapterPosition()) );
                context.startActivity(intent);
            }*/
        }
    }
}
