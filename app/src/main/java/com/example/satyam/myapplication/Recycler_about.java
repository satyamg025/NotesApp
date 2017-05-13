package com.example.satyam.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by satyam on 9/28/16.
 */
public class Recycler_about extends RecyclerView.Adapter<Recycler_about.ViewHolder> {
    public Context context;

    public Recycler_about(Context notes) {
        this.context=notes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_about, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        viewHolder.tv.setClickable(true);
        viewHolder.tv.setMovementMethod(LinkMovementMethod.getInstance());
        //String text = "<a href='https://www.linkedin.com/in/satyam-gupta-061445126'> LinkedIn </a>";
        //viewHolder.tv.setText(Html.fromHtml(text));





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
            this.tv=(TextView)itemView.findViewById(R.id.link_sat);
            this.image=(ImageView)itemView.findViewById(R.id.satyam);
        }
    }

}


