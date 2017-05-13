package com.example.satyam.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.satyam.myapplication.Model2.JSONDownloader2;
import com.google.firebase.iid.FirebaseInstanceId;

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

public class login_activity extends AppCompatActivity  {

    TextView cllg_name;
    ImageView logo;
    Toolbar toolbar;
    Button loginbtn;
    String college;
    SharedPreferences pref;
    String pref_name="SESSION";
    String user,pass;
    TextView qr;

    private EditText usr;
    private EditText pwd;
    public static final String USER_NAME="USERNAME";

    String username;
    String password;
    Spinner acc;
    String acc_type;
    Integer pos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        toolbar=(Toolbar)findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");

        Intent intent=getIntent();
        college=intent.getExtras().getString("college");
        acc_type=intent.getExtras().getString("acc_type");
        user=intent.getExtras().getString("username");
        pass=intent.getExtras().getString("password");


        qr=(TextView)findViewById(R.id.qr);
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view==qr)
                {
                    Intent intent=new Intent(login_activity.this,QrScan.class);
                    intent.putExtra("college",college);
                    intent.putExtra("acc_type",acc_type);
                    finish();
                    startActivity(intent);


                }
            }
        });
        loginbtn=(Button)findViewById(R.id.login);
        usr=(EditText)findViewById(R.id.usr_name);
        pwd=(EditText)findViewById(R.id.password);

        usr.setTypeface(Typeface.SANS_SERIF);
        pwd.setTypeface(Typeface.SANS_SERIF);

            if(user!= "null" && pass!= "null")
            {
                usr.setText(user);
                pwd.setText(pass);
                loginbtn.performClick();
            }



    }

    public void invokeLogin(View view)
    {
        username=usr.getText().toString();
        password=pwd.getText().toString();

            if(acc_type== "Student Login")
            {
                Spinner cllg_names;
                cllg_names=(Spinner)findViewById(R.id.spinner);
                String jsonURL="http://192.168.43.21/test1.php";

                new JSONDownloader2(login_activity.this, jsonURL, cllg_names).execute();

                login(username,password);
                //onTokenRefresh("Student");

            }
            else if(acc_type== "Faculty Login")
            {
                Spinner cllg_names;
                cllg_names=(Spinner)findViewById(R.id.spinner);
                String jsonURL="http://192.168.43.21/test1.php";

                new JSONDownloader2(login_activity.this, jsonURL, cllg_names).execute();

                login2(username,password);
               // onTokenRefresh("Faculty");


                // Intent intent=new Intent(login_activity.this,faculty_activity.class);
                //startActivity(intent);
            }


    }




    public void login(final String username, final String password){
        class LoginAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(login_activity.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String uname = params[0];
                String pass = params[1];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", uname));
                nameValuePairs.add(new BasicNameValuePair("password", pass));

                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://192.168.43.21/hoja.php");
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

                if(s!="null")
                {

                    try
                    {
                        JSONArray ja=new JSONArray(s);
                        JSONObject jo;
                        for (int i=0;i<ja.length();i++)
                        {

                                jo = ja.getJSONObject(0);
                                 String sem = jo.getString("SEM");
                                String unik=jo.getString("UNIQUE_ID");
                                 String branch = jo.getString("BRANCH");
                                 String cllg = jo.getString("COLLEGE");
                            String name=jo.getString("NAME");

                                 jo=ja.getJSONObject(1);

                                String subject1 = jo.getString("SUBJECT1");

                                String subject2 = jo.getString("SUBJECT2");

                                String subject3 = jo.getString("SUBJECT3");

                                String subject4 = jo.getString("SUBJECT4");

                                String subject5 = jo.getString("SUBJECT5");

                                String subject6 = jo.getString("SUBJECT6");

                                String subject7 = jo.getString("SUBJECT7");

                                String subject8 = jo.getString("SUBJECT8");

                                String subject9 = jo.getString("SUBJECT9");

                                String subject10 = jo.getString("SUBJECT10");

                            if(cllg.equalsIgnoreCase(college)) {
                                Intent intent = new Intent(login_activity.this, select_subject.class);
                                intent.putExtra("name",name);
                                intent.putExtra("sem", sem);
                                intent.putExtra("branch", branch);
                                intent.putExtra("username",username);
                                intent.putExtra("college",college);
                                intent.putExtra("unik",unik);
                                intent.putExtra("password",password);
                                intent.putExtra("subject1",subject1);
                                intent.putExtra("subject2",subject2);
                                intent.putExtra("subject3",subject3);
                                intent.putExtra("subject4",subject4);
                                intent.putExtra("subject5",subject5);
                                intent.putExtra("subject6",subject6);
                                intent.putExtra("subject7",subject7);
                                intent.putExtra("subject8",subject8);
                                intent.putExtra("subject9",subject9);
                                intent.putExtra("subject10",subject10);
                                finish();
                                startActivity(intent);
                                overridePendingTransition(R.anim.pull_left,R.anim.push_right);

                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Wrong cllg selected",Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                else {
                    Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(username, password);

    }

    public void login2(final String username, final String password){
        class Login2Async extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(login_activity.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String uname = params[0];
                String pass = params[1];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", uname));
                nameValuePairs.add(new BasicNameValuePair("password", pass));

                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://192.168.43.21/faculty_login.php");
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

                if(s!="null")
                {

                    try
                    {
                        JSONArray ja=new JSONArray(s);
                        JSONObject jo;
                        String name=null,description=null,cllg="",unik="",subject1="",subject2="",subject3="",subject4="",subject5="";


                        for (int i=0;i<=ja.length();i++) {
                            if (i == 0) {
                                jo = ja.getJSONObject(i);
                                name = jo.getString("NAME");
                                description = jo.getString("DESCRIPTION");
                                cllg = jo.getString("COLLEGE");
                                unik = jo.getString("UNIQUE_ID");
                                subject1 = jo.getString("SUBJECT1");
                                subject2 = jo.getString("SUBJECT2");
                                subject3 = jo.getString("SUBJECT3");
                                subject4 = jo.getString("SUBJECT4");
                                subject5 = jo.getString("SUBJECT5");
                            }
                        }
                        List<String> subject=new ArrayList<String>();


                                Intent intent = new Intent(login_activity.this,FacultyDashboard.class);
                                intent.putExtra("name", name);
                                if(subject1!=null || subject1!="null") {
                                    subject.add(subject1);

                                }
                        if(subject2!=null ||subject2!="null") {
                            subject.add(subject2);
                        }
                        if(subject3!=null || subject3!="null") {
                            subject.add(subject3);
                        }
                        if(subject4!=null || subject4!="null") {
                            subject.add(subject4);
                        }
                        if(subject5!=null || subject5!="null") {
                            subject.add(subject5);
                        }
                                intent.putExtra("subject", (ArrayList<String>) subject);
                                intent.putExtra("unik",unik);
                                intent.putExtra("description",description);
                                intent.putExtra("college",cllg);
                                finish();
                                startActivity(intent);
                                 overridePendingTransition(R.anim.pull_left,R.anim.push_right);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                else {
                    Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                }
            }
        }

        Login2Async la = new Login2Async();
        la.execute(username, password);

    }
    @Override
   public void onBackPressed(){
        Intent intent=new Intent(login_activity.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_right,R.anim.push_left);

   }




    public void onTokenRefresh(String type) {


        String token = FirebaseInstanceId.getInstance().getToken();
        registerToken(token,type);
    }





    public void registerToken(final String token,final String type) {
        class SAAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... params) {
                String tok = params[0];
                String typ=params[1];
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("Token", tok));
                nameValuePairs.add(new BasicNameValuePair("Type",typ));

                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://192.168.0.104/mobile_registration.php");
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

                Log.e("TAG", s);
                if(s!="null")
                {
                    //answer.setText("");
                    //Toast.makeText(Your_ans.this,"Your Answer Posted",Toast.LENGTH_SHORT).show();
                }
            }
        }
        SAAsync la=new SAAsync();
        la.execute(token);
    }


}



