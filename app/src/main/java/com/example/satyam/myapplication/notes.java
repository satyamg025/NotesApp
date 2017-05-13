package com.example.satyam.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class notes extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;

    private ProgressDialog pDialog;
    private Animation animShow, animHide;


    public static final int progress_bar_type = 0;
    Context c;
    String jsonURL,sr,unit_name;

    FloatingActionButton fab;




    RecyclerView mrecyclerView;
    LinearLayoutManager mlinearLayoutManager;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;

    RecyclerView mrecyclerView2;
    LinearLayoutManager mlinearLayoutManager2;
    private RecyclerView.Adapter adapter2;
    private RecyclerView.LayoutManager layoutManager2;

    List<String> ques=new ArrayList<String>(),ques_by=new ArrayList<String>(),ans=new ArrayList<String>(),ans_by=new ArrayList<String>();
    List<String> rated_by=new ArrayList<String>();
    List<String> description_note=new ArrayList<String>();

    TextView tv1,tv2;
    String un,unik;
    String subject;
    //String[] faculties={};
    //String[] colleges={};
    List<String> faculties=new ArrayList<String>(),colleges=new ArrayList<String>(),notes=new ArrayList<String>(),description=new ArrayList<String>(),paper=new ArrayList<String>();
    // String[] notes;

    List<String> rating=new ArrayList<String>(),fac_unik=new ArrayList<String>(),uni2=new ArrayList<String>();
    Spinner units;

    String unit,name;

    private String url;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar20);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notes");


        final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

        popup.setVisibility(View.GONE);




        fab=(FloatingActionButton)findViewById(R.id.ask_ques);

        fab.setVisibility(View.GONE);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        jsonURL="http://192.168.43.21/test1.php";


        Intent intent=getIntent();

        unit=intent.getExtras().getString("units");
        unik=intent.getExtras().getString("unik");
        name=intent.getExtras().getString("name");
        subject=intent.getExtras().getString("subject");
        units=(Spinner)findViewById(R.id.units5);
        final List<String> uni=new ArrayList<String>();
        units.setPrompt("SELECT UNIT");
        for(int i=0;i<Integer.parseInt(unit);i++)
        {
            uni2.add("Unit "+(i+1));

        }
        uni2.add("Others");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.item,uni2);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        units.setAdapter(adapter);
        units.setOnItemSelectedListener(onItemSelectedListener);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                //return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                // return false;
            }
        }


    }


    private void initPopup() {

        final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

        // Hide the popup initially.....
        //  popup.setVisibility(View.GONE);

        animShow = AnimationUtils.loadAnimation( this, R.anim.popup_show);
        animHide = AnimationUtils.loadAnimation( this, R.anim.popup_hide);

       // final ImageButton showButton = (ImageButton) findViewById(R.id.show_popup_button);
        final ImageButton   hideButton = (ImageButton) findViewById(R.id.hide_popup_button);
        //showButton.setOnClickListener(new View.OnClickListener() {
        //  public void onClick(View view) {
        popup.setVisibility(View.VISIBLE);
        popup.startAnimation( animShow );
        //showButton.setEnabled(false);
        hideButton.setEnabled(true);
        //}});

       /* hideButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popup.startAnimation( animHide );
                showButton.setEnabled(true);
                hideButton.setEnabled(false);
                popup.setVisibility(View.GONE);
            }});
*/
      /*  final TextView locationName = (TextView) findViewById(R.id.site_name);
        final TextView locationDescription = (TextView) findViewById(R.id.site_description);

        locationName.setText("CoderzHeaven");
        locationDescription.setText("Heaven of all working codes"
                + " A place where you can ask, share & even shout for code! Let’s share a wide range of technology here." +
                " From this site you will get a lot of working examples in your favorite programming languages!." +
                " Always remember we are only one comment away from you… Let’s shorten the distance between your doubts and your answers…");
*/
    }



    @Override
    public void onBackPressed() {
        final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);
        ImageButton hide=(ImageButton)findViewById(R.id.hide_popup_button);

        if(popup.isActivated())
        {
            popup.startAnimation( animHide );
            //hid.setEnabled(true);
            hide.setEnabled(false);
            popup.setVisibility(View.GONE);


        }
        else {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
       /* if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }*/
        DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(menuItem);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.pqp) {
            final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

            popup.setVisibility(View.GONE);


            LinearLayout ll=(LinearLayout) findViewById(R.id.ll5);
            ll.setVisibility(View.VISIBLE);



            units.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            Spinner cllg_names;
            cllg_names=(Spinner)findViewById(R.id.spinner);
            String jsonURL="http://192.168.43.21/test1.php";

            new JSONDownloader2(notes.this, jsonURL, cllg_names).execute();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar20);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Ques Papers");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);



            ques_paper(subject);


            // Handle the camera action
        } else if (id == R.id.about) {
            final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

            popup.setVisibility(View.GONE);

            LinearLayout ll=(LinearLayout) findViewById(R.id.ll5);
            ll.setVisibility(View.VISIBLE);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar20);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("About");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            units.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            mrecyclerView=(RecyclerView)findViewById(R.id.recycler2);
            mrecyclerView.setHasFixedSize(true);
            mlinearLayoutManager=new LinearLayoutManager(notes.this);
            mrecyclerView.setLayoutManager(mlinearLayoutManager);
            RecyclerView.Adapter mAdapter;
            //if(faculties!=null) {
            mAdapter = new Recycler_about(notes.this);
            mrecyclerView.setAdapter(mAdapter);



        } else if (id == R.id.suggestion) {
            final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

            popup.setVisibility(View.GONE);

            LinearLayout ll=(LinearLayout) findViewById(R.id.ll5);
            ll.setVisibility(View.VISIBLE);

            units.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            Spinner cllg_names;
            cllg_names=(Spinner)findViewById(R.id.spinner);
            String jsonURL="http://192.168.43.21/test1.php";

            new JSONDownloader2(notes.this, jsonURL, cllg_names).execute();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar20);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Suggestion");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);



            mrecyclerView=(RecyclerView)findViewById(R.id.recycler2);
            mrecyclerView.setHasFixedSize(true);
            mlinearLayoutManager=new LinearLayoutManager(notes.this);
            mrecyclerView.setLayoutManager(mlinearLayoutManager);
            RecyclerView.Adapter mAdapter;
            //if(faculties!=null) {
            mAdapter = new Recycler_suggestion(unik,this);
            mrecyclerView.setAdapter(mAdapter);



        } else if (id == R.id.ques) {
            final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

            popup.setVisibility(View.GONE);

            LinearLayout ll=(LinearLayout) findViewById(R.id.ll5);
            ll.setVisibility(View.VISIBLE);


            ques.clear();
            ques_by.clear();
            ans_by.clear();
            ans.clear();
          //  LinearLayout ll=(LinearLayout)findViewById(R.id.ll5);
           // ll.removeAllViews();


            units.setVisibility(View.GONE);
            Spinner cllg_names;
            cllg_names=(Spinner)findViewById(R.id.spinner);
            String jsonURL="http://192.168.43.21/test1.php";

            new JSONDownloader2(notes.this, jsonURL, cllg_names).execute();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar20);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Ques Ans");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);




            ques_ans(subject);
            fab.setVisibility(View.VISIBLE);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(com.example.satyam.myapplication.notes.this,Ask_ques.class);
                    intent.putExtra("name",name);
                    intent.putExtra("subject",subject);
                    intent.putExtra("units",unit);
                    intent.putExtra("unik",unik);
                    startActivity(intent);

                }
            });



        }
        else if(id==R.id.notes){
            final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

            popup.setVisibility(View.GONE);

            LinearLayout ll=(LinearLayout) findViewById(R.id.ll5);
            ll.setVisibility(View.VISIBLE);

            fab.setVisibility(View.GONE);
            Spinner cllg_names;
            cllg_names=(Spinner)findViewById(R.id.spinner);
            String jsonURL="http://192.168.43.21/test1.php";

            new JSONDownloader2(notes.this, jsonURL, cllg_names).execute();

            Intent intent=new Intent(notes.this,notes.class);
            intent.putExtra("subject",subject);
            intent.putExtra("units",unit);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.pull_left,R.anim.push_right);



        }
        else if(id==R.id.logout)
        {
            final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

            popup.setVisibility(View.GONE);



            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent=new Intent(notes.this,MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }else if(id==R.id.share)
        {
            final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

            popup.setVisibility(View.GONE);

            LinearLayout ll=(LinearLayout) findViewById(R.id.ll5);
            ll.setVisibility(View.VISIBLE);

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out this app at: https://play.google.com/store/apps/NotesApp");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            //Toast.makeText(com.example.satyam.myapplication.notes.this,"Share",Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.like)
        {
            final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

            //Intent intent=new Intent(this,Like.class);
            //startActivity(intent);

          //  LinearLayout linearLayout=(LinearLayout)findViewById(R.id.ll5);
            //linearLayout=null;

            initPopup();

            ImageButton fb=(ImageButton)findViewById(R.id.fb);
            ImageButton go=(ImageButton)findViewById(R.id.google);
            ImageButton link=(ImageButton)findViewById(R.id.linkedin);
            ImageButton tw=(ImageButton)findViewById(R.id.twitter);
            final ImageButton hide=(ImageButton)findViewById(R.id.hide_popup_button);

            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("http://www.facebook.com");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                }
            });

            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("http://www.plus.google.com");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                }
            });

            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("http://www.linkedin.com");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                }
            });

            tw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("http://www.twitter.com");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                }
            });

            hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup.startAnimation( animHide );
                    //hid.setEnabled(true);
                    hide.setEnabled(false);
                    popup.setVisibility(View.GONE);

                }
            });

            // Toast.makeText(com.example.satyam.myapplication.notes.this,"Like",Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }












    public void ques_paper(final String sub) {
        class QPAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(com.example.satyam.myapplication.notes.this, "Please wait", "Loading...");
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
                            "http://192.168.43.21/prev_ques.php");
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
                             desc = jo.getString("DESCRIPTION");
                           // Toast.makeText(com.example.satyam.myapplication.notes.this,desc,Toast.LENGTH_SHORT).show();

                           // paper.add(jo.getString("QUES_PAPERS"));
                            ques_paper = jo.getString("QUES_PAPERS");
                            //String cl = jo.getString("COLLEGE");
                            //notes[i]=jo.getString("NOTES");

                                description.add(desc);
                           // Toast.makeText(com.example.satyam.myapplication.notes.this,ques_paper,Toast.LENGTH_SHORT).show();

                           // if (ques_paper != null) {
                            //assert paper!=null;
                                pap.add(ques_paper);
                            //}


                        }

                        paper=pap;

                        if(paper!=null) {

                            mrecyclerView = (RecyclerView) findViewById(R.id.recycler2);
                            mrecyclerView.setHasFixedSize(true);
                            mlinearLayoutManager = new LinearLayoutManager(notes.this);
                            mrecyclerView.setLayoutManager(mlinearLayoutManager);
                            RecyclerView.Adapter mAdapter;
                            //if(faculties!=null) {
                            mAdapter = new Recycler_previous(description, paper, notes.this);
                            mrecyclerView.setAdapter(mAdapter);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    mrecyclerView=(RecyclerView)findViewById(R.id.recycler2);
                    mrecyclerView.setHasFixedSize(true);
                    mlinearLayoutManager=new LinearLayoutManager(notes.this);
                    mrecyclerView.setLayoutManager(mlinearLayoutManager);
                    RecyclerView.Adapter mAdapter;
                    //if(faculties!=null) {
                    mAdapter = new no_notes(com.example.satyam.myapplication.notes.this);
                    mrecyclerView.setAdapter(mAdapter);


                }
            }
        }

        QPAsync la = new QPAsync();
        la.execute(sub);

    }






    public void ques_ans(final String sub) {
        class QAAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(com.example.satyam.myapplication.notes.this, "Please wait", "Loading...");
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
                        if(ques.size()>0) {
                            mrecyclerView = (RecyclerView) findViewById(R.id.recycler2);
                            mrecyclerView.setHasFixedSize(true);
                            mlinearLayoutManager = new LinearLayoutManager(notes.this);
                            mrecyclerView.setLayoutManager(mlinearLayoutManager);
                            RecyclerView.Adapter mAdapter;
                            //if(faculties!=null) {
                            mAdapter = new Recycler_ques(ques, ans, ques_by, ans_by, name, subject, com.example.satyam.myapplication.notes.this);

                            mrecyclerView.setAdapter(mAdapter);
                        }


                        else{
                            fab.setVisibility(View.VISIBLE);
                            mrecyclerView=(RecyclerView)findViewById(R.id.recycler2);
                            mrecyclerView.setHasFixedSize(true);
                            mlinearLayoutManager=new LinearLayoutManager(notes.this);
                            mrecyclerView.setLayoutManager(mlinearLayoutManager);
                            RecyclerView.Adapter mAdapter;
                            //if(faculties!=null) {
                            mAdapter = new no_notes(com.example.satyam.myapplication.notes.this);
                            mrecyclerView.setAdapter(mAdapter);


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    fab.setVisibility(View.VISIBLE);
                    mrecyclerView=(RecyclerView)findViewById(R.id.recycler2);
                    mrecyclerView.setHasFixedSize(true);
                    mlinearLayoutManager=new LinearLayoutManager(notes.this);
                    mrecyclerView.setLayoutManager(mlinearLayoutManager);
                    RecyclerView.Adapter mAdapter;
                    //if(faculties!=null) {
                    mAdapter = new no_notes(com.example.satyam.myapplication.notes.this);
                    mrecyclerView.setAdapter(mAdapter);



                }
            }
        }

        QAAsync la = new QAAsync();
        la.execute(sub);

    }











    AdapterView.OnItemSelectedListener onItemSelectedListener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


            Integer pos=units.getSelectedItemPosition();
            if(pos==(uni2.size()-1)) {


                un = units.getItemAtPosition(pos).toString();
            }
            else
            {
                un=String.valueOf(pos+1);
            }
            //sr= String.valueOf(pos+1);
            faculties=new ArrayList<String>();
            colleges=new ArrayList<String>();
            notes=new ArrayList<String>();

            description_note.clear();
            faculties.clear();
            fac_unik.clear();
            notes.clear();
            colleges.clear();
            //Toast.makeText(NotesActivity.this,sr,Toast.LENGTH_LONG).show();
            notes(subject,un);



        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    public void notes(final String sub,final String unit){
        class NotesAsync extends AsyncTask<String,Void,String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                description.clear();
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(notes.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String subjct = params[0];
                String unt = params[1];
                //String clg=params[2];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("subject", subjct));
                nameValuePairs.add(new BasicNameValuePair("unit", unt));
                nameValuePairs.add(new BasicNameValuePair("unik", unik));

                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://192.168.43.21/faculty.php");
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
                Log.e("TAG",s);

               // Toast.makeText(notes.this,s,Toast.LENGTH_LONG).show();

                if(!Objects.equals(s, "null"))
                {

                    try
                    {
                        JSONArray ja=new JSONArray(s);
                        JSONObject jo;
                        /*jo=ja.getJSONObject(0);

                        unit_name=(jo.getString("UNIT_NAME"));
                        */

                        for (int i=0;i<ja.length();i=i+2) {

                            jo = ja.getJSONObject(i);
                            String note=jo.getString("NOTES");
                            String de=(jo.getString("DESCRIPTION"));

                            if(Objects.equals(de, ""))
                            {
                                description_note.add("[]");

                            }
                            else {
                                description_note.add(de);
                            }

                            jo=ja.getJSONObject(i+1);

                            String fac=jo.getString("NAME");
                            String fac_un=jo.getString("UNIQUE_ID");
                            String cl=jo.getString("COLLEGE");
                            //notes[i]=jo.getString("NOTES");
                            if(fac!=null)
                            {
                                faculties.add(fac);
                            }
                            if(cl!=null)
                            {
                                colleges.add(cl);
                            }
                            if(note!=null)
                            {
                                notes.add(note);
                            }
                            fac_unik.add(fac_un);

                        }
                        mrecyclerView=(RecyclerView)findViewById(R.id.recycler2);
                        mrecyclerView.setHasFixedSize(true);
                        mlinearLayoutManager=new LinearLayoutManager(notes.this);
                        mrecyclerView.setLayoutManager(mlinearLayoutManager);
                        RecyclerView.Adapter mAdapter;
                        //if(faculties!=null) {
                        mAdapter = new RecyclerAdapter(faculties, colleges,description_note, notes,fac_unik,unik,subject,un, notes.this);
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

                    mrecyclerView2=(RecyclerView)findViewById(R.id.recycler2);
                    mrecyclerView2.setHasFixedSize(true);
                    mlinearLayoutManager2=new LinearLayoutManager(notes.this);
                    mrecyclerView2.setLayoutManager(mlinearLayoutManager2);
                    RecyclerView.Adapter mAdapter2;
                    //if(faculties!=null) {
                    mAdapter2 = new no_notes(notes.this);
                    mrecyclerView2.setAdapter(mAdapter2);

                  //  Toast.makeText(com.example.satyam.myapplication.notes.this,"Note r not Available",Toast.LENGTH_LONG).show();
                }
            }
        }

        NotesAsync la = new NotesAsync();
        la.execute(sub,unit);

    }




}



