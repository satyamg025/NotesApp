package com.example.satyam.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by satyam on 10/4/16.
 *
 *
 */

public class Recycler_ans_ques extends RecyclerView.Adapter<Recycler_ans_ques.ViewHolder> {

    public List<String> ques, ques_by, ans, ans_by;

    List<String> q, qb, a, ab,subjects;
    Context context;
    public String fac_unik,subject;


    public Recycler_ans_ques(List<String> ques, List<String> ans, List<String> ques_by, List<String> ans_by, String fac_unik,String subject,List<String> subjects, Context co) {
        this.ques = ques;
        this.ques_by = ques_by;
        this.ans = ans;
        this.ans_by = ans_by;
        this.context=co;
        this.fac_unik=fac_unik;
        this.subject=subject;
        this.subjects=subjects;
        q = new ArrayList<String>();
        qb = new ArrayList<String>();
        a = new ArrayList<String>();
        ab = new ArrayList<String>();




        q=ques;
        qb=ques_by;
        a=ans;
        ab=ans_by;
    }

    @Override
    public Recycler_ans_ques.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ans_ques, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Recycler_ans_ques.ViewHolder holder,final int position) {

        holder.tv1.setText(q.get(position));
       // Toast.makeText(context,q.get(position),Toast.LENGTH_SHORT).show();
        if(!Objects.equals(a.get(position), "null")) {
            holder.tv2.setText(a.get(position));
        }
        else
        {
            holder.ans.setVisibility(View.GONE);
        }
        if(!Objects.equals(ab.get(position), "null"))
        {
            holder.tv4.setText(ab.get(position));
        }
        holder.tv3.setText(qb.get(position));
        holder.your_ans.setText("Your Answer");

        holder.your_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,Your_ans.class);
                intent.putExtra("ques",q.get(position));
                intent.putExtra("ans",a.get(position));
                intent.putExtra("fac_unik",fac_unik);
                intent.putExtra("subjects",(ArrayList<String>)subjects);
                intent.putExtra("subject",subject);
                context.startActivity(intent);


            }
        });






    }

    @Override
    public int getItemCount() {
        return q.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv1, tv2, tv3, tv4;
        public Button your_ans;
        public ProgressBar p;
        public LinearLayout linearLayout;
        public CardView ans;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tv1 = (TextView) itemView.findViewById(R.id.questions_fac);
            this.tv2 = (TextView) itemView.findViewById(R.id.answer_fac);
            this.tv3 = (TextView) itemView.findViewById(R.id.question_by_fac);
            this.tv4 = (TextView) itemView.findViewById(R.id.answer_by_fac);
            this.your_ans=(Button) itemView.findViewById(R.id.your_ans);
            this.ans=(CardView)itemView.findViewById(R.id.ans_card_fac);
            this.linearLayout = (LinearLayout) itemView.findViewById(R.id.ques_ans_fac);

        }
    }
}
