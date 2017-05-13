package com.example.satyam.myapplication;

import android.app.Dialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by satyam on 9/17/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    public List<String> faculties;
    public List<String> colleges;
    public List<String> fac_unik;
    public List<String> description;
    

    Context context;


    public String subject, unit;

    public String facunik;
    public String unik;
    public String unit_name;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private Button startBtn;
    //private NotificationManager mNotifyManager;
    private Notification.Builder build;
    //NotificationManager mNotificationHelper=null;
    final int id = 1;

    //Notification.Builder mNotificationHelper=null;

    private ProgressDialog mProgressDialog;

    public List<String> notes;
    public Float ratef = 0.0f, avg_rate = 0.0f;
    private static String file_url;
    String file;
    private ProgressDialog progressDialog;
    int progress_bar_type = 0;
    String n2, c2;


    List<String> f;
    List<String> fu;
    List<String> c;
    List<String> n;
    List<String> d;

    public RecyclerAdapter(List<String> fac, List<String> cllg,List<String> desc, List<String> note, List<String> fac_unik, String un, String subject, String unit, Context cn) {
        super();
        faculties = fac;
        colleges = cllg;
        notes = note;
        this.context = cn;
        this.unik = un;
      //  this.unit_name=unit_name;
        description=desc;

        this.fac_unik = fac_unik;
        this.subject = subject;
        this.unit = unit;

        f = new ArrayList<String>();
        c = new ArrayList<String>();
        n = new ArrayList<String>();
        fu = new ArrayList<String>();
        d=new ArrayList<String>();

        f = faculties;
        c = colleges;
        n = notes;
        fu = fac_unik;
        d=desc;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_faculty, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {


        facunik = fac_unik.get(i);


        viewHolder.itemView1.setText(f.get(i));
        viewHolder.itemView2.setText(c.get(i));
        viewHolder.download.setImageResource(R.drawable.ic_file_download_black_24dp);


/*        viewHolder.recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });*/


        if (avg_rate != null) {
            viewHolder.avg_rate.setText(String.valueOf(avg_rate) + "Rating");

        } else {
            viewHolder.avg_rate.setText(String.valueOf("0") + "Rating");
        }
        //Drawable progres= viewHolder.ratingBar.getProgressDrawable();
       // DrawableCompat.setTint(progres,Color.rgb());


        viewHolder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                String rating = String.valueOf(viewHolder.ratingBar.getRating());
                String jsonURL="http://192.168.43.21/test1.php";

                new JSONDownloader2(context, jsonURL, viewHolder.cllg_names).execute();


                rating(rating, unik, facunik);
                Toast.makeText(context, rating, Toast.LENGTH_SHORT).show();


            }
        });

        viewHolder.des.setText(d.get(i));
        // viewHolder.faq.setImageResource(R.drawable.ic_question_answer_black_24dp);
        // viewHolder.p.getProgressDrawable();


      /*  viewHolder.des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int po = viewHolder.getAdapterPosition();

                if (Objects.equals(d.get(po), "[/]")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(d.get(po)).setTitle("Description")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // do nothing
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();


                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(d.get(po)).setTitle("Description")
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
        });*/

        viewHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // int pos=viewHolder.recycler.getChildLayoutPosition(viewHolder.download);

               /*  String n2 = viewHolder.itemView1.getText().toString();
                String c2 = viewHolder.itemView2.getText().toString();
                for (int i = 0; i < f.size(); i++) {
                    if (Objects.equals(f.get(i), n2) && Objects.equals(c.get(i), c2)) {
                        file = n.get(i);
                        Toast.makeText(context,file,Toast.LENGTH_SHORT).show();
                        file=file.replaceAll(" ","%20");
                        break;
                    }
                }*/
               // file=n.get(pos);


                int pos=viewHolder.getAdapterPosition();
                //Toast.makeText(context,n.get(pos),Toast.LENGTH_SHORT).show();
                file=n.get(pos);

                file=file.replaceAll(" ","%20");
                String jsonURL="http://192.168.43.21/test1.php";

                new JSONDownloader2(context, jsonURL, viewHolder.cllg_names).execute();




                download(file);
            }

        });
        String jsonURL="http://192.168.43.21/test1.php";

        new JSONDownloader2(context, jsonURL, viewHolder.cllg_names).execute();


        getrating(facunik, unik, subject, unit);

        Log.e("TAG", String.valueOf(ratef + avg_rate));
        viewHolder.ratingBar.setRating(ratef);


    }


    @Deprecated
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Downloading file...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.show();
                return progressDialog;
            default:
                return null;
        }
    }


    public void download(final String file) {

        class DDAsync extends AsyncTask<String, String, String> {

            int count;


            @Override
            protected String doInBackground(String... strings) {
                try {

                    URL url = new URL("http://192.168.43.21/" + strings[0]);
                    URLConnection conexion = url.openConnection();
                    conexion.connect();

                    int lengthofFile = conexion.getContentLength();
                    Log.d("ANDRO_ASYNC", "Length of file: " + lengthofFile);

                    InputStream input = new BufferedInputStream(url.openStream());
                    String ext = Environment.getExternalStorageDirectory().toString();
                    File folder = new File(ext, "notes");
                    folder.mkdir();
                    File fi = new File(folder, file);
                    try {
                        fi.createNewFile();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    OutputStream output = new FileOutputStream(fi);

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        publishProgress("" + (int) ((total * 100) / lengthofFile));
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                } catch (Exception e) {
                    // TODO: handle exception
                }


                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                progressDialog.dismiss();
                // dismissDialog(DIALOG_DOWNLOAD_PROGRESS);

                File url = new File(Environment.getExternalStorageDirectory() + "/notes/" + file);  // -> filename = maven.pdf
                Uri uri = Uri.fromFile(url);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                    intent.setDataAndType(uri, "application/msword");
                } else if (url.toString().contains(".pdf")) {
                    intent.setDataAndType(uri, "application/pdf");
                } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                    intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                    intent.setDataAndType(uri, "application/vnd.ms-excel");
                } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                    intent.setDataAndType(uri, "application/x-wav");
                } else if (url.toString().contains(".rtf")) {
                    intent.setDataAndType(uri, "application/rtf");
                } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                    intent.setDataAndType(uri, "audio/x-wav");
                } else if (url.toString().contains(".gif")) {
                    intent.setDataAndType(uri, "image/gif");
                } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                    intent.setDataAndType(uri, "image/jpeg");
                } else if (url.toString().contains(".txt")) {
                    intent.setDataAndType(uri, "text/plain");
                } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                    intent.setDataAndType(uri, "video/*");
                } else {
                    intent.setDataAndType(uri, "*/*");
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                onCreateDialog(DIALOG_DOWNLOAD_PROGRESS);
                progressDialog.show();
                // showDialog(DIALOG_DOWNLOAD_PROGRESS);
            }

            @Override
            protected void onProgressUpdate(String... values) {
                // TODO Auto-generated method stub
                Log.d("ANDRO_ASYNC", values[0]);
                progressDialog.setProgress(Integer.parseInt(values[0]));
            }

        }


        DDAsync la = new DDAsync();
        la.execute(file);
    }


    public void rating(final String rate, final String unik, final String fac_un) {
        class RateAsync extends AsyncTask<String, String, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                String rate = params[0];
                String uni = params[1];
                String f_un = params[2];
                //String pass = params[1];
                //String clg=params[2];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("rate", rate));
                nameValuePairs.add(new BasicNameValuePair("unik", uni));
                nameValuePairs.add(new BasicNameValuePair("fac_unik", f_un));
                nameValuePairs.add(new BasicNameValuePair("subject", subject));
                nameValuePairs.add(new BasicNameValuePair("unit", unit));

                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://192.168.43.21/rating.php");
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

                if (result!="null") {
                    Toast.makeText(context, "Notes Rated Successfuly", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Unable to connect..... \n Please try after sometime", Toast.LENGTH_SHORT).show();
                }
            }
        }

        RateAsync la = new RateAsync();
        la.execute(rate, unik, fac_un);

    }


    public void getrating(final String facunik, final String unik, final String subject, final String unit) {


        class GetRateAsync extends AsyncTask<String, String, String> {

            private Dialog loadingDialog;
            float frate;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                String fu = params[0];
                String uni = params[1];
                String su = params[2];
                String un = params[3];


                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("unik", uni));
                nameValuePairs.add(new BasicNameValuePair("fac_unik", fu));
                nameValuePairs.add(new BasicNameValuePair("subject", su));
                nameValuePairs.add(new BasicNameValuePair("unit", un));

                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://192.168.43.21/getrating.php");
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
                //String s = result.trim();

                if (result!="null") {
                    String s = result.trim();
                    Log.e("TAG", s);

                    try {
                        JSONArray ja = new JSONArray(s);
                        JSONObject jo;

                        jo = ja.getJSONObject(0);

                        String rating = jo.getString("RATING");
//                            Toast.makeText(context,rating,Toast.LENGTH_SHORT).show();
//                            String avg_rating = jo.getString("AVG_RATE");
                        if (rating!= "null") {
                            ratef = Float.parseFloat(rating);
//                                Toast.makeText(context, rating, Toast.LENGTH_SHORT).show();
                            // avg_rate = Float.parseFloat(avg_rating);

                            Log.e("TAG", String.valueOf((ratef + avg_rate)));


                        }


                    } catch (JSONException e) {
//                        Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {


                }
                return result;

            }

        }
        GetRateAsync la = new GetRateAsync();
        la.execute(facunik, unik, subject, unit);

    }
















    /*public void showPdf()
    {
        File file=new File(Environment.getExternalStorageDirectory()+"/pdf/Read.pdf");
        PackageManager packageManager=getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        startActivity(intent);
    }*/





    /*@Override
    protected Dialog onCreateDialog(int id){
        switch(id){
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog=new ProgressDialog(NotesActivity.this);
        }
    }*/

    @Override
    public int getItemCount() {
        return f.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemView1, itemView2,des,uname;
        public ImageButton download;
        public ProgressBar p;
        public RatingBar ratingBar;
        public Spinner cllg_names;
        public TextView avg_rate;
        public RecyclerView recycler;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.recycler = (RecyclerView) itemView.findViewById(R.id.recycler2);
            this.itemView1 = (TextView) itemView.findViewById(R.id.faculty_name);
            this.itemView2 = (TextView) itemView.findViewById(R.id.college_name);
            this.download = (ImageButton) itemView.findViewById(R.id.download);
            this.ratingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);
            this.des=(TextView)itemView.findViewById(R.id.descr);
            //this.uname=(TextView)itemView.findViewById(R.id.unit_name);
//         this.download.setOnClickListener((View.OnClickListener) context);
            this.cllg_names=(Spinner)itemView.findViewById(R.id.spinner);

            this.avg_rate = (TextView) itemView.findViewById(R.id.avg_rating);
            //this.p=(ProgressBar)itemView.findViewById(R.id.progressBar);

           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=getAdapterPosition();
                    Toast.makeText(context,n.get(pos),Toast.LENGTH_SHORT).show();
                    file=n.get(pos);
                }
            });*/
        }

    }


}

