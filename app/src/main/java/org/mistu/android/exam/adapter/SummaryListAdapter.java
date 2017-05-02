package org.mistu.android.exam.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mistu.android.exam.R;
import org.mistu.android.exam.db.ExamDbContract;
import org.mistu.android.exam.fragment.SummaryListFragment;

/**
 * Created by kedee on 25/3/17.
 */

public class SummaryListAdapter extends RecyclerView.Adapter<SummaryListAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;
    private SummaryListFragment.OnSummaryListFragmentInteractionListener listener;
    private int[] colors;

    public SummaryListAdapter(Context context, Cursor cursor, SummaryListFragment.OnSummaryListFragmentInteractionListener listener) {
        this.context = context;
        this.cursor = cursor;
        this.listener = listener;
        colors = new int[] {R.color.card_bg1, R.color.card_bg2, R.color.card_bg3, R.color.card_bg4, R.color.card_bg5,
                R.color.card_bg6, R.color.card_bg7, R.color.card_bg8, R.color.card_bg9, R.color.card_bg10 };
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_quiz_summary, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(!cursor.moveToPosition(position)) {
            return;
        }
        String title = cursor.getString(cursor.getColumnIndex(ExamDbContract.ExamsTaken.COLUMN_NAME_TITLE));
        int totalQuestionsCount = cursor.getInt(cursor.getColumnIndex(ExamDbContract.ExamsTaken.COLUMN_NAME_TOTAL_QUESTION_COUNT));
        int correctQuestionsCount = cursor.getInt(cursor.getColumnIndex(ExamDbContract.ExamsTaken.COLUMN_NAME_CORRECT_QUESTION_COUNT));
        int attemptedQuestionsCount = cursor.getInt(cursor.getColumnIndex(ExamDbContract.ExamsTaken.COLUMN_NAME_ATTEMPED_QUESTION_COUNT));
        int timeTakenInSec = cursor.getInt(cursor.getColumnIndex(ExamDbContract.ExamsTaken.COLUMN_NAME_TIME_TAKEN));
        String responseAnswerJsonString = cursor.getString(cursor.getColumnIndex(ExamDbContract.ExamsTaken.COLUMN_NAME_ANSWER_RESPONSE_MAP));
        long id = cursor.getLong(cursor.getColumnIndex(ExamDbContract.ExamsTaken._ID));

        String type = title.split(" ")[0].trim();
        if(type.equals("Instant")){
            holder.titleTV.setBackgroundResource(R.drawable.orange_block_layer);
            if (Build.VERSION.SDK_INT >= 23) {
                holder.marksSeparatorLine.setBackgroundColor(context.getResources().getColor(R.color.colorAccent, null));
            }
            else {
                holder.marksSeparatorLine.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            }

        }
        else if (type.equals("Custom")) {
            holder.titleTV.setBackgroundResource(R.drawable.bluish_block_layer);
        }

        holder.titleTV.setText(title);

        String timeTaken = String.format("%02d" +"m : " +"%02d" + "s ", timeTakenInSec/60, timeTakenInSec%60);

        holder.totalQuestionTV.setText(String.valueOf(totalQuestionsCount));
        holder.correctQuestionTV.setText(String.valueOf(correctQuestionsCount));

        holder.correctCountTV.setText(String.valueOf(correctQuestionsCount));
        holder.incorrectCountTV.setText(String.valueOf(attemptedQuestionsCount - correctQuestionsCount));
        holder.unansweredCountTV.setText(String.valueOf(totalQuestionsCount - attemptedQuestionsCount));
        holder.timeTakenTV.setText(timeTaken);

        holder.responseAnswerJsonString = responseAnswerJsonString;
        holder.timeTaken = timeTaken;
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    /**
     * Swaps the Cursor currently held in the adapter with a new one
     * and triggers a UI refresh
     *
     * @param newCursor the new cursor that will replace the existing one
     */
    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView titleTV;
        public TextView totalQuestionTV;
        public TextView correctQuestionTV;
        public TextView timeTakenTV;
        public TextView correctCountTV;
        public TextView incorrectCountTV;
        public TextView unansweredCountTV;
        public View viewItem;
        public String responseAnswerJsonString;
        public String timeTaken;
        public View marksSeparatorLine;

        public ViewHolder(View itemView) {
            super(itemView);
            viewItem = itemView;

            titleTV = (TextView) itemView.findViewById(R.id.item_quiz_summary_title);
            totalQuestionTV =  (TextView) itemView.findViewById(R.id.item_quiz_summary_total_questions);
            correctQuestionTV = (TextView) itemView.findViewById(R.id.item_quiz_summary_correct_questions);
            timeTakenTV = (TextView) itemView.findViewById(R.id.item_quiz_summary_time_taken);
            correctCountTV = (TextView) itemView.findViewById(R.id.correct_count);
            incorrectCountTV = (TextView) itemView.findViewById(R.id.incorrect_count);
            unansweredCountTV = (TextView) itemView.findViewById(R.id.unanswered_count);
            marksSeparatorLine = itemView.findViewById(R.id.marks_separator_line);

            viewItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i("CLICKED", viewItem.getTag().toString() );
            if(v.getId() == viewItem.getId()){
                listener.onSummaryListFragmentInteraction((long) viewItem.getTag(), responseAnswerJsonString, timeTaken);
            }
        }
    }
}
