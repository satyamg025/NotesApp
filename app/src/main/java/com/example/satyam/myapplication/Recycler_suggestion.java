package com.example.satyam.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by satyam on 9/28/16.
 */
public class Recycler_suggestion extends RecyclerView.Adapter<Recycler_suggestion.ViewHolder> {

String sub;
    String suggestion;
    String unik;
    Context context;

    public Recycler_suggestion(String unik, Context context) {
        this.unik=unik;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_suggestion, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        viewHolder.btn_sug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggestion=viewHolder.sug.getText().toString();

                viewHolder.sug.setText("");
                submit_sugg(suggestion,unik);

            }
        });


    }


    public void submit_sugg(final String suggestion, final String unik) {
        class SAAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(context, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String sug = params[0];
                String un = params[1];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("suggestion", sug));
                nameValuePairs.add(new BasicNameValuePair("unik", un));

                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://192.168.43.21/suggestion.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }


            @Override
            protected void onPostExecute(String result) {
                String s = result.trim();
                loadingDialog.dismiss();


                    Toast.makeText(context,"Thanks for your suggestion",Toast.LENGTH_SHORT).show();

            }
        }
        SAAsync la=new SAAsync();
        la.execute(suggestion,unik);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public EditText sug;
        public Button btn_sug;

        public ViewHolder(View itemView) {
            super(itemView);

            sug=(EditText)itemView.findViewById(R.id.et_sug);
            btn_sug=(Button)itemView.findViewById(R.id.btn_sug);
            //this.image=(ImageView)itemView.findViewById(R.id.img10);
            // this.tv=(TextView)itemView.findViewById(R.id.txt);
        }
    }

}
