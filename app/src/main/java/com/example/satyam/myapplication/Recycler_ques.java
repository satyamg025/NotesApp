package com.example.satyam.myapplication;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by satyam on 9/28/16.
 */
public class Recycler_ques extends RecyclerView.Adapter<Recycler_ques.ViewHolder> {
    public List<String> ques,ans,ques_by,ans_by;
    public String name,subject;
    Context context;
    RecyclerView mrecyclerView;
    LinearLayoutManager mlinearLayoutManager;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    List<String> q,qb,a,ab;



    public Recycler_ques(List<String> ques, List<String> ans, List<String> ques_by, List<String> ans_by,String name,String sub,Context co) {
        this.ques=ques;
        this.context=co;
        this.name=name;
        this.subject=sub;
        this.ans=ans;
        this.ques_by=ques_by;
        this.ans_by=ans_by;

        q=new ArrayList<String>();
        qb=new ArrayList<String>();
        a=new ArrayList<String>();
        ab=new ArrayList<String>();

        q=ques;
        qb=ques_by;
        a=ans;
        ab=ans_by;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ques_ans, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Recycler_ques.ViewHolder holder, int position) {

        holder.tv1.setText(ques.get(position));
        holder.tv3.setText(ques_by.get(position));
        //holder.tv1.setText("hello how r u");
        if(!Objects.equals(ans.get(position), "null")) {
            holder.tv2.setText(ans.get(position));
        }
        else
        {
            holder.ans.setVisibility(View.GONE);
        }


       // Toast.makeText(context,ques.get(position),Toast.LENGTH_SHORT).show();
        holder.tv3.setText(ques_by.get(position));
        if(!Objects.equals(ans_by.get(position), "null")) {
            holder.tv4.setText(ans_by.get(position));
           // Toast.makeText(context,ans_by.get(position),Toast.LENGTH_SHORT).show();
           // holder.tv4.setText("Fine");
        }

       /* holder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent=new Intent(context,Ask_ques.class);
                intent.putExtra("name",name);
                intent.putExtra("subject",subject);
                context.startActivity(intent);



            }
        });*/





    }

    @Override
    public int getItemCount() {
        return q.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv1, tv2, tv3, tv4;
        public ProgressBar p;
        public FloatingActionButton fab;
        public LinearLayout linearLayout;
        public CardView ans;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tv1 = (TextView) itemView.findViewById(R.id.questions);
            this.tv2 = (TextView) itemView.findViewById(R.id.answer);
            this.tv3 = (TextView) itemView.findViewById(R.id.question_by);

            this.ans=(CardView)itemView.findViewById(R.id.ans_card);

          //  Toast.makeText(context,"id",Toast.LENGTH_SHORT).show();
            this.tv4 = (TextView) itemView.findViewById(R.id.answer_by);
           // this.fab=(FloatingActionButton)itemView.findViewById(R.id.ask_ques);
            this.linearLayout=(LinearLayout)itemView.findViewById(R.id.ques_ans);


        }
    }

    }

