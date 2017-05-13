package com.example.satyam.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.satyam.myapplication.Model2.JSONDownloader2;

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

public class UploadedNotes extends AppCompatActivity {
    String unik;
    RecyclerView mrecyclerView;
    LinearLayoutManager mlinearLayoutManager;
    List<String> date,subject,unit,sub;
    FloatingActionButton btn;

    TextView fn;
    Spinner cllg_names;
    String fac_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_notes);

        Toolbar toolbar;
        toolbar=(Toolbar)findViewById(R.id.toolbar12);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Uploads");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getActionBar().setDisplayHomeAsUpEnabled(true);

       fn=(TextView)findViewById(R.id.fac_name);
        date=new  ArrayList<String>();
        subject=new  ArrayList<String>();
        unit=new  ArrayList<String>();
        sub=new ArrayList<String>();

       // btn=(FloatingActionButton)findViewById(R.id.uploadbtn);

        Intent intent=getIntent();
        unik=intent.getExtras().getString("unik");
        sub=intent.getExtras().getStringArrayList("subject");
        fac_name=intent.getExtras().getString("name");//
        fn.setText("Welcome "+ fac_name);
        String jsonURL="http://192.168.43.21/test1.php";


        new JSONDownloader2(UploadedNotes.this, jsonURL,cllg_names).execute();


        notes(unik);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            Spinner cllg_names;
            cllg_names=(Spinner)findViewById(R.id.spinner);
            String jsonURL="http://192.168.43.21/test1.php";

            new JSONDownloader2(UploadedNotes.this, jsonURL, cllg_names).execute();

            Intent intent = new Intent(UploadedNotes.this,FacultyDashboard.class);
            intent.putExtra("name", fac_name);
            intent.putExtra("subject", (ArrayList<String>) sub);
            intent.putExtra("unik",unik);
            // intent.putExtra("description",description);
            // intent.putExtra("college",cllg);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.pull_left,R.anim.push_right);

        }
        return super.onOptionsItemSelected(menuItem);
    }



    public void notes(final String unique){
        class NotesAsync extends AsyncTask<String,Void,String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(UploadedNotes.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String unique_id = params[0];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("unik", unique_id));

                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://192.168.43.21/notes_faculty.php");
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
            protected void onPostExecute(String  result){
                String s = result.trim();
                loadingDialog.dismiss();

                // Toast.makeText(notes.this,s,Toast.LENGTH_LONG).show();

                if(s!= "null")
                {

                    try
                    {
                        JSONArray ja=new JSONArray(s);
                        JSONObject jo;
                        for (int i=0;i<ja.length();i++) {

                            jo = ja.getJSONObject(i);
                            date.add(jo.getString("UPLOADED_DATE"));
                            subject.add(jo.getString("SUBJECT"));
                            unit.add(jo.getString("UNIT"));
                        }
                        mrecyclerView=(RecyclerView)findViewById(R.id.recycler_uploaded_notes);
                        mrecyclerView.setHasFixedSize(true);
                        mlinearLayoutManager=new LinearLayoutManager(UploadedNotes.this);
                        mrecyclerView.setLayoutManager(mlinearLayoutManager);
                        RecyclerView.Adapter mAdapter;
                        //if(faculties!=null) {
                        mAdapter = new Recycler_notes_faculty(date,subject,unit,sub,unique, UploadedNotes.this);
                        mrecyclerView.setAdapter(mAdapter);


                        //}
                        //else
                        //{
                        //}

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                else {

                    //  Toast.makeText(com.example.satyam.myapplication.notes.this,"Note r not Available",Toast.LENGTH_LONG).show();
                }
            }
        }

        NotesAsync la = new NotesAsync();
        la.execute(unique);

    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(UploadedNotes.this,FacultyDashboard.class);
        intent.putExtra("unik",unik);
        intent.putExtra("subject",(ArrayList<String>)sub);
        intent.putExtra("name",fac_name);
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.pull_left,R.anim.push_right);
    }


}
