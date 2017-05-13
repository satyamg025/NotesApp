package com.example.satyam.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.Objects;

public class select_subject extends AppCompatActivity {
    Context c;
    String college, sem1, branch1;
    TextView tv;
    SharedPreferences pref;
    Button btn;
    Spinner sem, subject, branch;
    String stu_name;
    String username, password, unit,sub,name,unik;

    String subject1, subject2, subject3, subject4, subject5, subject6, subject7, subject8, subject9, subject10;
    String jsonURL = "http://192.168.0.100/get_sub2.php";

    ArrayList<String> subjects = new ArrayList<>();

    Toolbar toolbar;
    Integer pos = -1;
    TextView st;
    Button no_not;
    TextView no_note;
    String subj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.c = c;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject);
        Intent intent = getIntent();

        st=(TextView)findViewById(R.id.name);
        college = intent.getExtras().getString("college");
        branch1 = intent.getExtras().getString("branch");
        sem1 = intent.getExtras().getString("sem");
        username = intent.getExtras().getString("username");
        password = intent.getExtras().getString("password");
        unik=intent.getExtras().getString("unik");
        stu_name=intent.getExtras().getString("name");

        no_not=(Button)findViewById(R.id.no_not);
        no_note=(TextView)findViewById(R.id.no_note);
        no_note.setText("");
       // st.setText("Welcome"+ stu_name);


        toolbar=(Toolbar)findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Subject");

        name=intent.getExtras().getString("name");
        subject1 = intent.getExtras().getString("subject1");
        subject2 = intent.getExtras().getString("subject2");
        subject3 = intent.getExtras().getString("subject3");
        subject4 = intent.getExtras().getString("subject4");
        subject5 = intent.getExtras().getString("subject5");
        subject6 = intent.getExtras().getString("subject6");
        subject7 = intent.getExtras().getString("subject7");

        subject8 = intent.getExtras().getString("subject8");
        subject9 = intent.getExtras().getString("subject9");
        subject10 = intent.getExtras().getString("subject10");


/*        pref=getSharedPreferences("Session",MODE_PRIVATE);
        final String branch2=pref.getString("branch","");
        final String sem2=pref.getString("sem","");
        sem1=sem2;
        branch1=branch2;
  */
        //tv = (TextView) findViewById(R.id.cllg_name);
        //tv.setText(college);

        addOnItemsSem();
        addOnItemsBranch();
        addOnItemsSubject();
        addListenerOnButton();


        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int pos=subject.getSelectedItemPosition();
                 subj=subject.getItemAtPosition(pos).toString();
                no_of_note(sem1,subj);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        no_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=subject.getSelectedItemPosition();
                subj=subject.getItemAtPosition(pos).toString();

                if(no_note.getText().toString()!="") {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(select_subject.this);
                    builder.setMessage(no_note.getText().toString()+" new updates for "+ subj ).setTitle("No of Notes")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // do nothing
                                }
                            });
                    android.app.AlertDialog alert = builder.create();
                    alert.show();
                }
                else
                {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(select_subject.this);
                    builder.setMessage("No new updates for "+ subj ).setTitle("No of Notes")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // do nothing
                                }
                            });
                    android.app.AlertDialog alert = builder.create();
                    alert.show();


                }

            }
        });



    }

    private void addOnItemsSem() {
        sem = (Spinner) findViewById(R.id.sem);
        List<String> sems = new ArrayList<String>();
        sems.add(sem1);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sems);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sem.setAdapter(dataAdapter);

    }


    private void addOnItemsBranch() {
        branch = (Spinner) findViewById(R.id.branch);

        List<String> branches = new ArrayList<String>();
        branches.add(branch1);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, branches);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(dataAdapter);


    }


    private void addOnItemsSubject() {
        subject = (Spinner) findViewById(R.id.subject);

        List<String> subjects = new ArrayList<String>();
        if (subject1!= "null") {
            subjects.add(subject1);
        }
        if (subject2!= "null") {
            subjects.add(subject2);
        }
        if (subject3!="null") {
            subjects.add(subject3);
        }
        if (subject4!= "null") {
            subjects.add(subject4);
        }
        if (subject5!="null") {
            subjects.add(subject5);
        }
        if (subject6!= "null") {
            subjects.add(subject6);
        }
        if (subject7!= "null") {
            subjects.add(subject7);
        }
        if (subject8!="null") {
            subjects.add(subject8);
        }
        if (subject9!= "null") {
            subjects.add(subject9);
        }
        if (subject10!="null") {
            subjects.add(subject10);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subjects);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject.setAdapter(dataAdapter);


    }




    public void addListenerOnButton() {

        //spinner1 = (Spinner) findViewById(R.id.spinner1);
        subject = (Spinner) findViewById(R.id.subject);
        btn = (Button) findViewById(R.id.notes);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Integer pos=subject.getSelectedItemPosition();

                sub=subject.getItemAtPosition(pos).toString();
                Spinner cllg_names;
                cllg_names=(Spinner)findViewById(R.id.spinner);
                String jsonURL="http://192.168.43.21/test1.php";

                new JSONDownloader2(select_subject.this, jsonURL, cllg_names).execute();


                unit(sub);

                /*Toast.makeText(MainActivity.this,
                        "OnClickListener : " +

                                "\nSpinner 2 : "+ String.valueOf(cllg_names.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();*/
            }

        });
    }



    public void unit(final String sub){
        class UnitAsync extends AsyncTask<String,String,String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(select_subject.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String subject = params[0];
                //String pass = params[1];
                //String clg=params[2];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("subject", subject));
                //nameValuePairs.add(new BasicNameValuePair("password", pass));
                //nameValuePairs.add(new BasicNameValuePair("college", college));

                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://192.168.43.21/unit.php");
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
                //String s = result.trim();
                loadingDialog.dismiss();

                if(result!= "null")
                {

                    try
                    {
                        JSONArray ja=new JSONArray(result);
                        JSONObject jo;
                        for (int i=0;i<ja.length();i++) {

                            jo = ja.getJSONObject(0);
                            String units = jo.getString("UNITS");
                            Log.e("TAG",units);


                            Intent intent=new Intent(select_subject.this,notes.class);
                            intent.putExtra("subject",sub);
                            intent.putExtra("unik",unik);
                            intent.putExtra("units",units);
                            intent.putExtra("name",name);
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.pull_left,R.anim.push_right);


                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                else {
                    }
            }
        }

        UnitAsync la = new UnitAsync();
        la.execute(sub);

    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent=new Intent(select_subject.this,MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }





    public void no_of_note(final String sem,final String sub) {
        no_note.setText("");
        class NoN extends AsyncTask<String, String, String> {



            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(select_subject.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String sem = params[0];
                String sub = params[1];
                //String pass = params[1];
                //String clg=params[2];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("sem", sem));
                nameValuePairs.add(new BasicNameValuePair("subject", sub));
                //nameValuePairs.add(new BasicNameValuePair("college", college));

                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://192.168.43.21/number.php");
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
                //String s = result.trim();
                loadingDialog.dismiss();

                if (result!= "null") {

                    try {
                        JSONArray ja = new JSONArray(result);
                        JSONObject jo;
                        for (int i = 0; i < ja.length(); i++) {

                            jo = ja.getJSONObject(0);
                            String re=jo.getString("COUNT");
                            if(re!="0")
                            {
                                no_note.setText(re);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }


            NoN la = new NoN();
            la.execute(sem,sub);

        }








   /* @Override
    public void onClick(View view) {
        if(view==loginbtn)
        {
            Intent intent=new Intent(this,select_subject.class);
            intent.putExtra("college",college);
            startActivity(intent);
        }
    }*/
}
