package com.example.satyam.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by satyam on 9/28/16.
 */
public class no_notes extends RecyclerView.Adapter<no_notes.ViewHolder> {
Context context;

    public no_notes(Context cn) {
        context=cn;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.no_notes, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {


    }
    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            //this.image=(ImageView)itemView.findViewById(R.id.img10);
           // this.tv=(TextView)itemView.findViewById(R.id.txt);
        }
    }

    }





