package org.mistu.android.exam.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.system.Os;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.mistu.android.exam.R;
import org.mistu.android.exam.model.Feed;

import java.util.List;

/**
 * Created by kedee on 26/4/17.
 */

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int IMAGE = 0;
    private static final int PDF = 1;
    private static final int TEXT = 2;

    private List<Feed> feedList;
    private Context context;

    public FeedAdapter(List<Feed> feedList) {
        this.feedList = feedList;
    }

    public void addItem(Feed feed){
        feedList.add(feed);
        notifyDataSetChanged();
    }

    public void clearAdapter() {
        int size = feedList.size();
        feedList.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView ;
        switch (viewType) {
            case IMAGE:
                itemView = LayoutInflater.from(context).inflate(R.layout.item_home_feed_image_type, parent, false);
                return new ImageTypeViewHolder(itemView);

            case PDF:
                itemView = LayoutInflater.from(context).inflate(R.layout.item_home_feed_pdf_type, parent, false);
                return new PdfTypeViewHolder(itemView);

            case TEXT:
                itemView = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
                return new TextTypeViewHolder(itemView);

            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
                return new TextTypeViewHolder(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (feedList.get(position).getFeedFormat()){
            case IMAGE:
                return IMAGE;

            case PDF:
                return PDF;

            case TEXT:
                return TEXT;

            default:
                return TEXT;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Feed feed = feedList.get(position);
        switch (holder.getItemViewType()) {
            case IMAGE:
                ImageTypeViewHolder imageTypeViewHolder = (ImageTypeViewHolder) holder;


                imageTypeViewHolder.titleTV.setText(feed.getTitle());
                imageTypeViewHolder.textTV.setText(feed.getText());
                imageTypeViewHolder.formatTV.setText(feed.getFeedFormat().toString());
                imageTypeViewHolder.imageView.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(feed.getResourceUrl())
                        .centerCrop()
                        .into(imageTypeViewHolder.imageView);
                break;

            case PDF:
                PdfTypeViewHolder pdfTypeViewHolder = (PdfTypeViewHolder) holder;


                pdfTypeViewHolder.titleTV.setText(feed.getTitle());
                pdfTypeViewHolder.textTV.setText(feed.getText());
                pdfTypeViewHolder.formatTV.setText(feed.getFeedFormat().toString());

                break;

            case TEXT:
                TextTypeViewHolder textTypeViewHolder = (TextTypeViewHolder) holder;
                textTypeViewHolder.titleTV.setText(feed.getTitle());
                textTypeViewHolder.textTV.setText(feed.getText());
                textTypeViewHolder.formatTV.setText(feed.getFeedFormat().toString());
                break;

            default:
                TextTypeViewHolder defaultTypeViewHolder = (TextTypeViewHolder) holder;
                defaultTypeViewHolder.titleTV.setText(feed.getTitle());
                defaultTypeViewHolder.textTV.setText(feed.getText());
                defaultTypeViewHolder.formatTV.setText(feed.getFeedFormat().toString());
                break;
        }
    }

    private void setDrawable(int resId) {

    }


    @Override
    public int getItemCount() {
        return feedList.size();
    }

    class ImageTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTV;
        TextView textTV;
        TextView formatTV;
        ImageView imageView;
        LinearLayout coloredBlock;
        View viewItem;
        public ImageTypeViewHolder(View itemView) {
            super(itemView);
            viewItem = itemView;
            titleTV = (TextView) itemView.findViewById(R.id.feed_home_image_type_title_tv);
            textTV = (TextView) itemView.findViewById(R.id.feed_home_image_type_text_tv);
            formatTV = (TextView) itemView.findViewById(R.id.feed_home_image_type_format_tv);
            imageView = (ImageView) itemView.findViewById(R.id.feed_home_image_type_image);
            coloredBlock = (LinearLayout) itemView.findViewById(R.id.feed_colored_block);
            viewItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Clicked :)", Toast.LENGTH_SHORT).show();
        }
    }

    class PdfTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView titleTV;
        TextView textTV;
        TextView formatTV;
        View viewItem;
        public PdfTypeViewHolder(View itemView) {
            super(itemView);
            viewItem = itemView;
            titleTV = (TextView) itemView.findViewById(R.id.feed_home_pdf_type_title_tv);
            textTV = (TextView) itemView.findViewById(R.id.feed_home_pdf_type_text_tv);
            formatTV = (TextView) itemView.findViewById(R.id.feed_home_pdf_type_format_tv);

            viewItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Clicked :)", Toast.LENGTH_SHORT).show();
        }
    }

    class TextTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView titleTV;
        TextView textTV;
        TextView formatTV;
        LinearLayout coloredBlock;
        View viewItem;

        public TextTypeViewHolder(View itemView) {
            super(itemView);
            viewItem = itemView;
            titleTV = (TextView) itemView.findViewById(R.id.feed_title_tv);
            textTV = (TextView) itemView.findViewById(R.id.feed_text_tv);
            formatTV = (TextView) itemView.findViewById(R.id.feed_format_tv);
            coloredBlock = (LinearLayout) itemView.findViewById(R.id.feed_colored_block);
            viewItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Clicked :)", Toast.LENGTH_SHORT).show();
        }
    }
}
