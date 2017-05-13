package com.example.satyam.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satyam on 10/1/16.
 */
public class Recycler_notes_faculty extends RecyclerView.Adapter<Recycler_notes_faculty.ViewHolder> {
    public List<String> date, subj, un,subs;
    List<String> d, s, u,s2;
    Context context;
    String unik,unique;

    public Recycler_notes_faculty(List<String> date, List<String> subject, List<String> unit,List<String> sub,String unik, Context co) {

        this.date = date;
        this.subj = subject;
        this.un = unit;
        this.context = co;
        this.subs=sub;
        this.unik=unik;


        d = new ArrayList<String>();
        s = new ArrayList<String>();
        u = new ArrayList<String>();
        s2=new ArrayList<String>();

        d = date;
        s = subj;
        u = un;
        s2=subs;
        unique=unik;
    }

    @Override
    public Recycler_notes_faculty.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(Recycler_notes_faculty.ViewHolder holder, int position) {
        holder.sub.setText(s.get(position));

        holder.un.setText(u.get(position));
        holder.date.setText(d.get(position));

    }

    @Override
    public int getItemCount() {
        return d.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sub;
        public TextView un;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);

            this.sub=(TextView)itemView.findViewById(R.id.subject);
            this.un=(TextView)itemView.findViewById(R.id.unit);
            this.date=(TextView)itemView.findViewById(R.id.date);
            }
    }
}
