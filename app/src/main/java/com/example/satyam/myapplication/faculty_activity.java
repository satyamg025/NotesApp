package com.example.satyam.myapplication;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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


//import com.android.internal.http.multipart.MultipartEntity;

//import com.android.internal.http.multipart.MultipartEntity;

public class faculty_activity extends AppCompatActivity implements View.OnClickListener {
    //String subject[] = {};
    int i = 0;
    Spinner sub, unit;
    String subj;
    int units;
    String m_chosen;
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
    String ques2ans;

    Button notificaton;


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
        setContentView(R.layout.activity_faculty_activity);

        notificaton=(Button)findViewById(R.id.notification_answer);

        toolbar = (Toolbar) findViewById(R.id.toolbar6);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Faculty Activity");

       // qu2an=(TextView)findViewById(R.id.ques_to_ans);


        tv1=(TextView)findViewById(R.id.sf);
        tv2=(TextView)findViewById(R.id.selected_file);





        select_file=(TextView)findViewById(R.id.selected_file);

        tv1.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv2.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        Intent intent = getIntent();
        uploadUnit = "4";
        uploadSubject = "DSUC(NCS-301)";
        List<String> subjects=new ArrayList<String>();

        subjects.add("Select Subject");
        subject=intent.getExtras().getStringArrayList("subject");


        for(int i=0;i<subject.size();i++)
        {
           // Toast.makeText(faculty_activity.this,subject.get(i),Toast.LENGTH_SHORT).show();
            subjects.add(subject.get(i));
        }

        unik = intent.getExtras().getString("unik");
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
                    Intent intent = new Intent(faculty_activity.this, answer_ques.class);
                    intent.putExtra("fac_unik", unik);
                    Integer po = sub.getSelectedItemPosition();
                    String su = sub.getItemAtPosition(po).toString();
                    intent.putExtra("subject", su);
                    startActivity(intent);
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


            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(faculty_activity.this, android.R.layout.simple_spinner_item, un);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            unit.setAdapter(dataAdapter2);




    }

    public void getunit(final String subject) {
        class UnitAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(faculty_activity.this, "Please wait", "Loading...");
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

                if (s!= "null") {

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
                            if(ques2ans!="0")
                            {
                                qu2an.setText(ques2ans);
                            }



                        }
                        for (int j = 0; j < units; j++) {
                            un.add(String.valueOf(j + 1));
                        }
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);

        }
    }



    @Override
    public void onClick(View view) {
        if (view == select) {

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
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select File"),
                    1);
            Toast.makeText(faculty_activity.this,filename,Toast.LENGTH_SHORT).show();
            Toast.makeText(faculty_activity.this,filepath,Toast.LENGTH_SHORT).show();





        }

        if (view == upload) {
            //new HttpFileUpload(filename,filepath,"All the best","3","DLD(NCS-30)",this);
            //new ImageUploadTask();
            //  Upload();
           //filename=filename.replaceAll("\\s+","");
           // filepath=filepath.replaceAll("\\s+","");
            uploadFile(filepath, filename);
            Integer po=sub.getSelectedItemPosition();
            Integer po2=unit.getSelectedItemPosition();
            String su=sub.getItemAtPosition(po).toString();
            String un=unit.getItemAtPosition(po2).toString();
            description=(EditText)findViewById(R.id.desc);
            updescription=description.getText().toString();

            uploadDetails(filename,un,su,updescription);
            upload.setEnabled(false);


        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    File myFile = new File(uri.toString());
                    Toast.makeText(faculty_activity.this,uri.toString(),Toast.LENGTH_SHORT).show();
                   // filepath = getPDFPath(uri);

                    File sdCardRoot = Environment.getExternalStorageDirectory();
                    File yourDir = new File(sdCardRoot, filepath);
                    filename = yourDir.getName();
                    select_file.setText(filename);
                    //Toast.makeText(faculty_activity.this, filepath, Toast.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public String getPDFPath(Uri uri) {

        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private void uploadFile(final String filePath, final String fileName) {
        class UF extends AsyncTask<String, String, String> {

            InputStream inputStream;


            @Override
            protected String doInBackground(String... params) {

                String fp = params[0];
                String fn = params[1];
                Toast.makeText(faculty_activity.this, fp, Toast.LENGTH_LONG).show();

                try {

                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://192.168.43.21/upload.php");

                    file=new File(fp);


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
                showAlert(result);

                super.onPostExecute(result);
            }

        }
        UF l = new UF();
        l.execute(filePath,fileName);


    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }




    public void uploadDetails(final String filename,final String uni,final String subject,final String desc) {
        Integer pos = sub.getSelectedItemPosition();
        Integer pos2 = unit.getSelectedItemPosition();

        if (select_file.getText().toString()== "" || pos == 0 || pos2 == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Can't upload").setTitle("Error")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // do nothing
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }


            class UDAsync extends AsyncTask<String, Void, String> {

                private Dialog loadingDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loadingDialog = ProgressDialog.show(faculty_activity.this, "Please wait", "Loading...");
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

                    if (s!= "null") {
                        //ques.setText("");
                        Toast.makeText(faculty_activity.this, "Notes Posted Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(faculty_activity.this);
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

















