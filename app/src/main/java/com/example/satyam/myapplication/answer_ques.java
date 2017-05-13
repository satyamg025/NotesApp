package com.example.satyam.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class answer_ques extends AppCompatActivity {


    RecyclerView mrecyclerView;
    LinearLayoutManager mlinearLayoutManager;

    String fac_unik,subject,name;

    List<String> subjects=new ArrayList<String>();

    List<String> ques=new ArrayList<String>(),ques_by=new ArrayList<String>(),ans=new ArrayList<String>(),ans_by=new ArrayList<String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_ques);
        Toolbar toolbar;
        toolbar=(Toolbar)findViewById(R.id.toolbar6);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Questionaire");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // ActionBar actionBar=getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        fac_unik=intent.getExtras().getString("fac_unik");
        subject=intent.getExtras().getString("subject");

        subjects=intent.getExtras().getStringArrayList("subjects");
        name=intent.getExtras().getString("name");

        ques_ans(subject);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            Intent intent=new Intent(this,FacultyDashboard.class);
            intent.putExtra("unik",fac_unik);

            intent.putExtra("subject",(ArrayList<String>)subjects );
            intent.putExtra("name",name);
            startActivity(intent);
            //finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }



    public void ques_ans(final String sub) {
        class QAAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(com.example.satyam.myapplication.answer_ques.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String subjct = params[0];
                //String unt = params[1];
                //String clg=params[2];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("subject", subjct));
                //nameValuePairs.add(new BasicNameValuePair("unit", unt));
                //nameValuePairs.add(new BasicNameValuePair("college", college));

                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://192.168.43.21/ques_ans.php");
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

                Log.e("hf",result.toString());
                return result;
            }


            @Override
            protected void onPostExecute(String result) {
                String s = result.trim();
                loadingDialog.dismiss();
                String desc="",ques_paper="";
                List<String> pap=new ArrayList<String>();
                // Toast.makeText(NotesActivity.this, s, Toast.LENGTH_LONG).show();

                if (!Objects.equals(s, "null")) {

                    try {
                        JSONArray ja = new JSONArray(s);
                        JSONObject jo;
                        for (int i = 0; i < ja.length(); i++) {

                            jo = ja.getJSONObject(i);
                            ques.add(jo.getString("QUES"));
                            ans.add(jo.getString("ANS"));
                            ques_by.add(jo.getString("QUESBY"));
                            ans_by.add(jo.getString("FAC_NAME"));




                        }
                        mrecyclerView=(RecyclerView)findViewById(R.id.recycler_ans_ques);
                        mrecyclerView.setHasFixedSize(true);
                        mlinearLayoutManager=new LinearLayoutManager(answer_ques.this);
                        mrecyclerView.setLayoutManager(mlinearLayoutManager);
                        RecyclerView.Adapter mAdapter;
                        //if(faculties!=null) {
                        mAdapter = new Recycler_ans_ques(ques,ans,ques_by,ans_by,fac_unik,subject,subjects, com.example.satyam.myapplication.answer_ques.this);
                        mrecyclerView.setAdapter(mAdapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    mrecyclerView=(RecyclerView)findViewById(R.id.recycler_ans_ques);
                    mrecyclerView.setHasFixedSize(true);
                    mlinearLayoutManager=new LinearLayoutManager(answer_ques.this);
                    mrecyclerView.setLayoutManager(mlinearLayoutManager);
                    RecyclerView.Adapter mAdapter;
                    //if(faculties!=null) {
                    mAdapter = new no_notes(com.example.satyam.myapplication.answer_ques.this);
                    mrecyclerView.setAdapter(mAdapter);


                }
            }
        }

        QAAsync la = new QAAsync();
        la.execute(sub);

    }





}
