package org.mistu.android.exam;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kedee on 23/3/17.
 */

public class ProblemListAdapter  extends RecyclerView.Adapter<ProblemListAdapter.ViewHolder>{

    private List<Problem> problemList;
    private ProblemListFragment.OnProblemListFragmentInteractionListener listener;

    public ProblemListAdapter(List<Problem> problemList,
                              ProblemListFragment.OnProblemListFragmentInteractionListener listener) {
        this.problemList = problemList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_problem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Problem problem = problemList.get(position);
        Spanned htmlAsSpanned;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            htmlAsSpanned = Html.fromHtml(problem.getQuestion(), Html.FROM_HTML_MODE_LEGACY);
        }
        else {
            htmlAsSpanned = Html.fromHtml(problem.getQuestion());
        }
        holder.questionTV.setText(htmlAsSpanned);
        String count = position+1 + ".";
        holder.countTV.setText(count);
    }

    @Override
    public int getItemCount() {
        return problemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView questionTV;
        TextView countTV;
        View viewItem;
        public ViewHolder(View itemView) {
            super(itemView);
            viewItem = itemView;
            questionTV = (TextView) itemView.findViewById(R.id.item_problem_question_tv);
            countTV = (TextView) itemView.findViewById(R.id.item_problem_question_count);
            viewItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == itemView.getId()) {

            }
        }
    }

    public void setProblemList(List<Problem> problemList) {
        this.problemList = problemList;
        notifyDataSetChanged();
    }
}
