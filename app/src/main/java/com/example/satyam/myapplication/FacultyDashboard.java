package com.example.satyam.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.satyam.myapplication.Model2.JSONDownloader2;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FacultyDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    int i = 0;
    Spinner sub, unit;
    String subj;
    int units;
    String m_chosen;
    String err=null;

    List<String> un = null;
    Button upload;
    Button select;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    String filepath;
    String uploadUnit;
    String uploadSubject;
    String unik;
    String updescription;
    File file;
    Toolbar toolbar;
    String filename;
    String responseString = null;

    EditText description;
    String ques2ans,name;
    private Animation animShow, animHide;


    Button notificaton;
    String uni,su;


    TextView tv1,tv2,select_file,qu2an;
    List<String> subject=new ArrayList<String>();

    private String upload_path;
    long filesize;
    ProgressDialog progressDialog;
    Bitmap bitmap;
    final static String UPLOAD_SERVER_URI = "http://192.168.0.106/upload_notes.php";


    public static final String UPLOAD_URL = "http://192.168.43.21/upload_notes.php";
    public static final String UPLOAD_KEY = "image";


    private int PICK_IMAGE_REQUEST = 1;
    File imageFile;
    private Uri filePath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_dashboard);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle("Dashboard");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        notificaton=(Button)findViewById(R.id.notification_answer);


        qu2an=(TextView)findViewById(R.id.ques_to_ans2);


        tv1=(TextView)findViewById(R.id.sf);
        tv2=(TextView)findViewById(R.id.selected_file);





        select_file=(TextView)findViewById(R.id.selected_file);

       // tv1.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        //tv2.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        Intent intent = getIntent();
        uploadUnit = "4";
        uploadSubject = "DSUC(NCS-301)";
        List<String> subjects=new ArrayList<String>();

        subjects.add("Select Subject");
        subject=intent.getExtras().getStringArrayList("subject");


        assert subject != null;
        for(int i = 0; i<subject.size(); i++)
        {
            // Toast.makeText(faculty_activity.this,subject.get(i),Toast.LENGTH_SHORT).show();
            if(!Objects.equals(subject.get(i), "null")) {
                subjects.add(subject.get(i));
            }
        }

        unik = intent.getExtras().getString("unik");
        name=intent.getExtras().getString("name");
        sub = (Spinner) findViewById(R.id.subject);

        upload = (Button) findViewById(R.id.upload);
        select = (Button) findViewById(R.id.select_file);
        select.setOnClickListener(this);
        upload.setOnClickListener(this);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subjects);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sub.setAdapter(dataAdapter);





        notificaton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view==notificaton) {
                    Spinner cllg_names;
                    cllg_names=(Spinner)findViewById(R.id.spinner);
                    String jsonURL="http://192.168.43.21/test1.php";

                    new JSONDownloader2(FacultyDashboard.this, jsonURL, cllg_names).execute();
                    Integer po = sub.getSelectedItemPosition();
                    String su = sub.getItemAtPosition(po).toString();
                    if(Objects.equals(su, "Select Subject"))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FacultyDashboard.this);
                        builder.setMessage("Please select some subject").setTitle("Message")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(FacultyDashboard.this,FacultyDashboard.class);
                                        intent.putExtra("name", name);
                                        intent.putExtra("subject", (ArrayList<String>) subject);
                                        intent.putExtra("unik",unik);
                                        // intent.putExtra("description",description);
                                        // intent.putExtra("college",cllg);
                                        finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.pull_left,R.anim.push_right);

                                        // do nothing
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();


                    }
                    else {


                        Intent intent = new Intent(FacultyDashboard.this, answer_ques.class);
                        intent.putExtra("fac_unik", unik);

                        intent.putExtra("name", name);
                        intent.putExtra("subjects", (ArrayList<String>) subject);
                        intent.putExtra("unik",unik);

                        // Integer po = sub.getSelectedItemPosition();
                        //String su = sub.getItemAtPosition(po).toString();
                        intent.putExtra("subject", su);
                        startActivity(intent);
                    }
                }
            }
        });



        unit = (Spinner) findViewById(R.id.unit);


        un = new ArrayList<String>();
        un.add("Select Unit");


        sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                qu2an.setText(null);
                if(un.size()>0)
                {
                    un.clear();
                    un.add("Select Unit");
                }
                Integer pos = sub.getSelectedItemPosition();
                if (pos != 0) {

                    subj = String.valueOf(sub.getItemAtPosition(pos));
                    //  Toast.makeText(faculty_activity.this, subj, Toast.LENGTH_LONG).show();

                    getunit(subj);




                }
                else
                {
                    un.clear();
                    un.add("Select Unit");

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                un.add("Select Unit");

            }
        });


        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(FacultyDashboard.this, android.R.layout.simple_spinner_item, un);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit.setAdapter(dataAdapter2);


    }

    @Override
    public void onBackPressed() {
       /* final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);
        ImageButton hide=(ImageButton)findViewById(R.id.hide_popup_button);

        if(popup.isActivated())
        {
            popup.startAnimation( animHide );
            //hid.setEnabled(true);
            hide.setEnabled(false);
            popup.setVisibility(View.GONE);


        }*/
        //else{


        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent=new Intent(FacultyDashboard.this,MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();

       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*///}
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.upload_notes) {
           // final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

            //popup.setVisibility(View.GONE);

            Spinner cllg_names;
            cllg_names=(Spinner)findViewById(R.id.spinner);
            String jsonURL="http://192.168.43.21/test1.php";

            new JSONDownloader2(FacultyDashboard.this, jsonURL, cllg_names).execute();

            Intent intent = new Intent(FacultyDashboard.this,FacultyDashboard.class);
             intent.putExtra("name", name);
            intent.putExtra("subject", (ArrayList<String>) subject);
            intent.putExtra("unik",unik);
           // intent.putExtra("description",description);
           // intent.putExtra("college",cllg);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.pull_left,R.anim.push_right);
        } else if (id == R.id.uploaded) {
//            final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

            //popup.setVisibility(View.GONE);

            Spinner cllg_names;
            cllg_names=(Spinner)findViewById(R.id.spinner);
            String jsonURL="http://192.168.43.21/test1.php";

            new JSONDownloader2(FacultyDashboard.this, jsonURL, cllg_names).execute();

            Intent intent = new Intent(FacultyDashboard.this,UploadedNotes.class);
            intent.putExtra("name", name);
            intent.putExtra("subject", (ArrayList<String>) subject);
            intent.putExtra("unik",unik);
            // intent.putExtra("description",description);
            // intent.putExtra("college",cllg);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.pull_left,R.anim.push_right);



        }  else if (id == R.id.about) {
//            final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

//            popup.setVisibility(View.GONE);

            notificaton.setVisibility(View.GONE);
            qu2an.setVisibility(View.GONE);
            RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.fac);
            relativeLayout.removeAllViews();
           // units.setVisibility(View.GONE);
            //fab.setVisibility(View.GONE);
            RecyclerView mrecyclerView;
            LinearLayoutManager mlinearLayoutManager;
            mrecyclerView=(RecyclerView)findViewById(R.id.recycler_about);
            mrecyclerView.setHasFixedSize(true);
            mlinearLayoutManager=new LinearLayoutManager(FacultyDashboard.this);
            mrecyclerView.setLayoutManager(mlinearLayoutManager);
            RecyclerView.Adapter mAdapter;
            //if(faculties!=null) {
            mAdapter = new Recycler_about(FacultyDashboard.this);
            mrecyclerView.setAdapter(mAdapter);



        } else if (id == R.id.suggestion) {
//            final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

  //          popup.setVisibility(View.GONE);

            notificaton.setVisibility(View.GONE);
            qu2an.setVisibility(View.GONE);
            RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.fac);
            relativeLayout.removeAllViews();

                RecyclerView mrecyclerView;
                LinearLayoutManager mlinearLayoutManager;
                mrecyclerView=(RecyclerView)findViewById(R.id.recycler_about);
                mrecyclerView.setHasFixedSize(true);
                mlinearLayoutManager=new LinearLayoutManager(FacultyDashboard.this);
                mrecyclerView.setLayoutManager(mlinearLayoutManager);
                RecyclerView.Adapter mAdapter;
                //if(faculties!=null) {
                mAdapter = new Recycler_suggestion(unik,this);
                mrecyclerView.setAdapter(mAdapter);




            }
        else if(id==R.id.ques)
        {
//            final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

  //          popup.setVisibility(View.GONE);

            Spinner cllg_names;
            cllg_names=(Spinner)findViewById(R.id.spinner);
            String jsonURL="http://192.168.43.21/test1.php";

            new JSONDownloader2(FacultyDashboard
                    .this, jsonURL, cllg_names).execute();

            Intent intent = new Intent(FacultyDashboard.this, answer_ques.class);
            intent.putExtra("fac_unik", unik);
            Integer po = sub.getSelectedItemPosition();
            String su = sub.getItemAtPosition(po).toString();
            if(Objects.equals(su, "Select Subject"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(FacultyDashboard.this);
                builder.setMessage("Please select some subject").setTitle("Message")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(FacultyDashboard.this,FacultyDashboard.class);
                                intent.putExtra("name", name);
                                intent.putExtra("subject", (ArrayList<String>) subject);
                                intent.putExtra("unik",unik);
                                // intent.putExtra("description",description);
                                // intent.putExtra("college",cllg);
                                finish();
                                startActivity(intent);
                                overridePendingTransition(R.anim.pull_left,R.anim.push_right);

                                // do nothing
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
            else {


                intent.putExtra("subject", su);
                startActivity(intent);
            }
        }

        else if(id==R.id.logout){
    //        final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

      //      popup.setVisibility(View.GONE);



            new android.support.v7.app.AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent=new Intent(FacultyDashboard.this,MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }else if(id==R.id.share)
        {
//            final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

            //popup.setVisibility(View.GONE);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out this app at: https://play.google.com/store/apps/NotesApp");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
           // Toast.makeText(com.example.satyam.myapplication.FacultyDashboard.this,"Share",Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.like)
        {
            //final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

            //Intent intent=new Intent(this,Like.class);
            //startActivity(intent);

            //  LinearLayout linearLayout=(LinearLayout)findViewById(R.id.ll5);
            //linearLayout=null;

//            initPopup();

          /*  ImageButton fb=(ImageButton)findViewById(R.id.fb);
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
            });*/

  /*          hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup.startAnimation( animHide );
                    //hid.setEnabled(true);
                    hide.setEnabled(false);
                    popup.setVisibility(View.GONE);

                }
            });
*/
             Toast.makeText(com.example.satyam.myapplication.FacultyDashboard.this,"Like",Toast.LENGTH_SHORT).show();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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






    public void getunit(final String subject) {
        class UnitAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(FacultyDashboard.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String sub = params[0];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("subject", sub));

                String result = null;

                try {
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

                if (!Objects.equals(s, "null")) {

                    try {
                        JSONArray ja = new JSONArray(s);
                        JSONObject jo;

                        for (int i = 0; i < ja.length(); i++) {
                            jo = ja.getJSONObject(0);
                            String unit = jo.getString("UNITS");

                            // Toast.makeText(faculty_activity.this, unit, Toast.LENGTH_LONG).show();
                            units = Integer.parseInt(unit);

                            jo=ja.getJSONObject(1);
                            String q2a=jo.getString("count");
                            ques2ans=q2a;
                            if(!Objects.equals(ques2ans, "0"))
                            {
                                qu2an.setText(ques2ans);
                            }



                        }
                        for (int j = 0; j < units; j++) {
                            un.add(String.valueOf(j + 1));
                        }
                        un.add("Others");
                   /*     ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(faculty_activity.this, android.R.layout.simple_spinner_item, un);
                        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        unit.setAdapter(dataAdapter2);
*/

                        //  Toast.makeText(faculty_activity.this, String.valueOf(units), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        }
        UnitAsync la = new UnitAsync();
        la.execute(subject);
    }

    @Override
    public void onClick(View view) {
        if (view == select) {

            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select File"),
                    1);



        }

        if (view == upload) {
            Spinner cllg_names;
            cllg_names=(Spinner)findViewById(R.id.spinner);
            String jsonURL="http://192.168.43.21/test1.php";

            new JSONDownloader2(FacultyDashboard.this, jsonURL, cllg_names).execute();
            description=(EditText)findViewById(R.id.desc);
            updescription=description.getText().toString();
            Integer pos = sub.getSelectedItemPosition();
            Integer pos2 = unit.getSelectedItemPosition();

            if (Objects.equals(select_file.getText().toString(), "") || pos == 0 || pos2 == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FacultyDashboard.this);
                builder.setMessage("Subjet or unit not selected").setTitle("Message")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
            else {


                if (Objects.equals(updescription, "")) {
                    new android.support.v7.app.AlertDialog.Builder(FacultyDashboard.this)
                            .setMessage("Continue without description...")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    updescription = "[/]";
                                    uploadFile(filepath, filename);

                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

                } else {
                    uploadFile(filepath, filename);

                }
            }


            //new HttpFileUpload(filename,filepath,"All the best","3","DLD(NCS-30)",this);
            //new ImageUploadTask();
            //  Upload();
            //filename=filename.replaceAll("\\s+","");
            //filepath=filepath.replaceAll("\\s+","");

           // upload.setEnabled(false);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    File myFile = new File(uri.toString());
                    filepath = getFilPath(uri);

                    File sdCardRoot = Environment.getExternalStorageDirectory();
                    File yourDir = new File(sdCardRoot, filepath);
                    filename = yourDir.getName();
                    //  Toast.makeText(faculty_activity.this, filename, Toast.LENGTH_LONG).show();
                    select_file.setText(filename);
                    //Toast.makeText(faculty_activity.this, filepath, Toast.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public String getFilPath(Uri uri) {
/*
        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);*/

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(this, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn( contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                }


                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn( contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }




    public String getDataColumn(Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }







    private void uploadFile(final String filePath, final String fileName) {
        class UF extends AsyncTask<String, String, String> {

            InputStream inputStream;


            @Override
            protected String doInBackground(String... params) {

                String fp = params[0];
                String fn = params[1];
                try {

                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://192.168.43.21/upload.php");

                    File file = new File(fp);
                    // String fp2=fp.replaceAll("\\s+","");
                    //File file1=new File(fp2);
                    //  file.renameTo(file1);

                    FileBody fileBody = new FileBody(file);
                    MultipartEntity multipartEntity = new MultipartEntity(
                            HttpMultipartMode.BROWSER_COMPATIBLE);

                    multipartEntity.addPart("file", fileBody);
                    httpPost.setEntity(multipartEntity);

                    HttpResponse httpResponse = httpClient.execute(httpPost);


                    HttpEntity entity = httpResponse.getEntity();
                    int statusCode = httpResponse.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(entity);
                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }

                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                } catch (IOException e) {
                    responseString = e.toString();
                }

                return responseString;

            }

            @Override
            protected void onPostExecute(String result) {
                Log.e("TAG", "Response from server: " + result);
                //      showAlert(result);

                super.onPostExecute(result);
                String s = result.trim();
                JSONArray ja = null;
                try {
                    ja = new JSONArray(s);

                    JSONObject jo;
                    for (int i = 0; i < ja.length(); i++) {
                        jo=ja.getJSONObject(i);
                         err=jo.getString("error");


                    }
                    if (!Objects.equals(err, "true")) {
                        Integer po=sub.getSelectedItemPosition();
                        Integer po2=unit.getSelectedItemPosition();
                         su=sub.getItemAtPosition(po).toString();
                         uni=unit.getItemAtPosition(po2).toString();
                       // description=(EditText)findViewById(R.id.desc);

                        //updescription=description.getText().toString();
                        Spinner cllg_names;
                        cllg_names=(Spinner)findViewById(R.id.spinner);
                        String jsonURL="http://192.168.43.21/test1.php";

                        new JSONDownloader2(FacultyDashboard.this, jsonURL, cllg_names).execute();









                        uploadDetails(filename,uni,su,updescription);

                        filename=null;
                        filepath=null;
                        description.setHint("Description");
                        select_file.setText("Not Selected");

                        sub.setSelection(0);
                        unit.setSelection(0);





                    }
                    else {
                        Toast.makeText(FacultyDashboard.this,"Unable to upload",Toast.LENGTH_SHORT).show();
                        filename=null;
                        filepath=null;
                        description.setText("");
                        select_file.setText("Not Selected");

                        sub.setSelection(0);
                        unit.setSelection(0);



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }



        UF l = new UF();
        l.execute(filePath,fileName);


    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FacultyDashboard.this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        //alert.show();
    }




    public void uploadDetails(final String filename,final String uni,final String subject,final String desc) {

        class UDAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(FacultyDashboard.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String fn = params[0];
                String un = params[1];
                String su = params[2];
                String desc = params[3];


                InputStream is = null;


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("subject", su));
                nameValuePairs.add(new BasicNameValuePair("unik", unik));
                nameValuePairs.add(new BasicNameValuePair("description", desc));
                nameValuePairs.add(new BasicNameValuePair("unit", un));
                nameValuePairs.add(new BasicNameValuePair("file_name", fn));

                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://192.168.43.21/upload_notes.php");
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
                Log.e("Tag", s.toString());

                if (!Objects.equals(s, "null")) {
                    //ques.setText("");
                    Toast.makeText(FacultyDashboard.this, "Document Posted Successfully", Toast.LENGTH_SHORT).show();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FacultyDashboard.this);
                    builder.setMessage("Failed To Post... \n Please Try again....").setTitle("Error")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // do nothing
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }



        UDAsync la = new UDAsync();
        la.execute(filename,uni,subject,desc);


    }

}


