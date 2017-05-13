package com.example.satyam.myapplication;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.satyam.myapplication.Model2.JSONDownloader2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by satyam on 9/28/16.
 */
public class Recycler_previous extends RecyclerView.Adapter<Recycler_previous.ViewHolder2> {

    Context context;
    String file,file2;
    NotificationManager mNotifyManager=null;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    final int id=1;

    Notification.Builder mBuilder=null;
    ProgressDialog progressDialog;

    String sub;
    public List<String> paper2=new ArrayList<String>();
    public List<String> description2=new ArrayList<String>();
    List<String> p;
    List<String> d;



    public Recycler_previous(List<String> description, List<String> paper, Context co) {
        super();
//        Toast.makeText(co,"sdf",Toast.LENGTH_SHORT).show();
        description2=description;
        paper2=paper;

        this.context=co;

        d=new ArrayList<String>();
        p=new ArrayList<String>();
        // n=new ArrayList<String>();

        d=description2;
        p=paper2;
        //n=notes;
    }

    @Override

    public ViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ques_paper, parent, false);
        //ViewHolder viewHolder = new ViewHolder(v);
        ViewHolder2 viewholder=new ViewHolder2(v);
   //     Toast.makeText(context,"jjh2",Toast.LENGTH_SHORT).show();

        return viewholder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder2 viewHolder, final int i) {

        viewHolder.tv.setText(d.get(i));


        SpannableString content = new SpannableString("Download");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        viewHolder.qus_paper.setText(content);

      //  Toast.makeText(context,p.get(i).replaceAll(" ","%20"),Toast.LENGTH_SHORT).show();
        // viewHolder.itemView2.setText(c.get(i));
     //   Toast.makeText(context, "jjh", Toast.LENGTH_SHORT).show();
        //viewHolder.qus_paper.setImageResource(R.drawable.ic_file_download_black_24dp);
        // viewHolder.faq.setImageResource(R.drawable.ic_question_answer_black_24dp);
        // viewHolder.p.getProgressDrawable();

        viewHolder.qus_paper.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View view) {
                for(int i=0;i<p.size();i++)
                {  int pos=viewHolder.getAdapterPosition();
                    //Toast.makeText(context,n.get(pos),Toast.LENGTH_SHORT).show();
                    file=p.get(pos);

                    file=file.replaceAll(" ","%20");
                    String jsonURL="http://192.168.43.21/test1.php";

                    new JSONDownloader2(context, jsonURL, viewHolder.cllg_names).execute();



                    download(file);

                    /*if(Objects.equals(d.get(i), des))
                    {
                        file3=p.get(i);
                        file2=file3.replaceAll(" ","%20");
                        file2=file2+".pdf";
                        Toast.makeText(context,file2,Toast.LENGTH_SHORT).show();
                        String jsonURL="http://192.168.43.21/test1.php";

                        new JSONDownloader2(context, jsonURL, viewHolder.cllg_names).execute();

                        download(file2);

                    }*/
                }
               /* File pdfFile = new File(Environment.getExternalStorageDirectory() + "/previous year ques papers/" + file2);  // -> filename = maven.pdf
                Uri path = Uri.fromFile(pdfFile);
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(path, "application/pdf");
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try{
                    context.startActivity(pdfIntent);
                }catch(ActivityNotFoundException e){
                    Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                }
*/


            /*String url = "http://192.168.43.21/"+file+".doc";
            String ext = Environment.getExternalStorageDirectory().toString();
            File folder = new File(ext, "pdf");
            folder.mkdir();
            File fi = new File(folder, file);
            try

            {
                fi.createNewFile();

            }

            catch(IOException e)

            {
                e.printStackTrace();
            }

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Download.DownloadFile(url,fi,context);

            Toast.makeText(context,"Downloading...",Toast.LENGTH_LONG).show();

            // showPdf();

                // showPdf();
            }
*/}

        });


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


    /*public void view(View v)
    {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/previous year ques papers/" + file);  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            context.startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }*/



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
               // progressDialog.dismiss();
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
                Toast.makeText(context,"Downloading....",Toast.LENGTH_SHORT).show();
                //onCreateDialog(DIALOG_DOWNLOAD_PROGRESS);
                //progressDialog.show();
                // showDialog(DIALOG_DOWNLOAD_PROGRESS);
            }

            @Override
            protected void onProgressUpdate(String... values) {
                // TODO Auto-generated method stub
                //Log.d("ANDRO_ASYNC", values[0]);
                //progressDialog.setProgress(Integer.parseInt(values[0]));
            }

        }


        DDAsync la = new DDAsync();
        la.execute(file);
    }





    @Override
    public int getItemCount() {
        return p.size();
    }

    class ViewHolder2 extends RecyclerView.ViewHolder{
        public TextView tv,qus_paper;
        public Spinner cllg_names;

        public ProgressBar p;
        public ViewHolder2(View itemView) {
            super(itemView);
            this.cllg_names=(Spinner)itemView.findViewById(R.id.spinner);

            this.tv = (TextView)itemView.findViewById(R.id.description);
            // this.itemView2=(TextView)itemView.findViewById(R.id.college_name);
            this.qus_paper=(TextView) itemView.findViewById(R.id.download_paper);
            // this.faq=(ImageButton)itemView.findViewById(R.id.faq);
            //this.p=(ProgressBar)itemView.findViewById(R.id.progressBar);
        }

    }
}