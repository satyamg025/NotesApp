package com.example.satyam.myapplication;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.satyam.myapplication.Model2.JSONDownloader2;

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
import java.util.Objects;

public class Your_ans extends AppCompatActivity {
    String ques, ans, fac_unik, subject, answers = null;
    TextView ques_asked;
    EditText answer;
    FloatingActionButton fab;
    List<String> subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_ans);

        Toolbar toolbar;
        toolbar=(Toolbar)findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Answer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        ques = intent.getExtras().getString("ques");
        ans = intent.getExtras().getString("ans");
        fac_unik = intent.getExtras().getString("fac_unik");
        subject = intent.getExtras().getString("subject");
        subjects=intent.getExtras().getStringArrayList("subjects");

        ques_asked = (TextView) findViewById(R.id.fac_ques);
        ques_asked.setText(ques);
        fab=(FloatingActionButton)findViewById(R.id.submit_ans);

        answer = (EditText) findViewById(R.id.fac_ans);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view==fab) {
                    answers = answer.getText().toString();
                  //  Toast.makeText(Your_ans.this,ans,Toast.LENGTH_SHORT).show();


                    if (answers != null) {
                        Spinner cllg_names;
                        cllg_names=(Spinner)findViewById(R.id.spinner);
                        String jsonURL="http://192.168.43.21/test1.php";

                        new JSONDownloader2(Your_ans.this, jsonURL, cllg_names).execute();

                        submit_ans(subject, fac_unik, ques, ans, answers);
                    } else {
                        Toast.makeText(Your_ans.this, "Plzz write something", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            //finish();
            Intent intent=new Intent(this,answer_ques.class);
            intent.putExtra("fac_unik",fac_unik);
            intent.putExtra("subject",subject);
            intent.putExtra("subjects",(ArrayList<String>)subjects);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(menuItem);
    }



    public void submit_ans(final String subject, final String fac_unik, final String ques, final String ans1, final String ans2) {
        class SAAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(Your_ans.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String sub = params[0];
                String facu = params[1];
                String ques = params[2];
                String an1 = params[3];
                String an2 = params[4];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("subject", sub));
                nameValuePairs.add(new BasicNameValuePair("fac_unik", facu));
                nameValuePairs.add(new BasicNameValuePair("ques", ques));
                nameValuePairs.add(new BasicNameValuePair("ans1", an1));
                nameValuePairs.add(new BasicNameValuePair("ans2", an2));

                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://192.168.43.21/submitans.php");
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


            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            protected void onPostExecute(String result) {
                String s = result.trim();
                loadingDialog.dismiss();

                Log.e("TAG", s);
                if(!Objects.equals(s, "null"))
                {
                    answer.setText("");
                    Toast.makeText(Your_ans.this,"Your Answer Posted",Toast.LENGTH_SHORT).show();
                }
            }
        }
        SAAsync la=new SAAsync();
        la.execute(subject,fac_unik,ques,ans1,ans2);
    }
}