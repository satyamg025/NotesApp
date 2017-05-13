package com.example.satyam.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import java.util.Objects;

public class Ask_ques extends AppCompatActivity implements View.OnClickListener {
    EditText ques;
    String name;
    String subject;
    Button btn;
    String question,unit,unik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_ques);

        Toolbar toolbar;
        toolbar=(Toolbar)findViewById(R.id.toolbar16);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Question");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ques=(EditText)findViewById(R.id.your_ques);
        btn=(Button)findViewById(R.id.ask);

        Intent intent=getIntent();
        subject=intent.getExtras().getString("subject");
        name=intent.getExtras().getString("name");
        unit=intent.getExtras().getString("units");
        unik=intent.getExtras().getString("unik");
        btn.setOnClickListener(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
           // finish();
            Intent intent=new Intent(this,notes.class);
            intent.putExtra("units",unit);
            intent.putExtra("unik",unik);
            intent.putExtra("subject",subject);
            intent.putExtra("name",name);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(menuItem);
    }






    public void ask(final String subject, final String name,final String questions){
        class ASKAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(Ask_ques.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String sub = params[0];
                String name = params[1];
                String ques=params[2];

                //String clg=params[2];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("subject", sub));
                nameValuePairs.add(new BasicNameValuePair("name",name));
                nameValuePairs.add(new BasicNameValuePair("ques", ques));

                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://192.168.43.21/ask_ques.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }


            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
                loadingDialog.dismiss();
                Log.e("TAG",s);

                if(!Objects.equals(s, "null"))
                {
                    ques.setText("");
                    Toast.makeText(Ask_ques.this,"Ques Posted Successfully",Toast.LENGTH_SHORT).show();

                }


                else {
                    Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                }
            }
        }

        ASKAsync la = new ASKAsync();
        la.execute(subject,name,questions);

    }

    @Override
    public void onClick(View view) {
        question=ques.getText().toString();
        ask(subject,name,question);

    }
}
