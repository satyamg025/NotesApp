package com.example.satyam.myapplication.Model;

/**
 * Created by satyam on 9/7/16.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Spinner;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
public class JSONDownloader extends AsyncTask<Void,Void,String>{
    Context c;
    String jsonURL;
    Spinner sp;
    ProgressDialog pd;
    public JSONDownloader(Context c, String jsonURL, Spinner sp) {
        this.c = c;
        this.jsonURL = jsonURL;
        this.sp = sp;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Connecting....");
        //pd.setMessage("Downloading...Please wait");
        pd.show();
    }
    @Override
    protected String doInBackground(Void... voids) {
        return this.download();
    }
    @Override
    protected void onPostExecute(String jsonData) {
        super.onPostExecute(jsonData);
        pd.dismiss();
        if(jsonData.startsWith("Error"))
        {
            Intent intent=new Intent(c, com.example.satyam.myapplication.Game.GameStart.class);
            c.startActivity(intent);

            //Toast.makeText(c, jsonData, Toast.LENGTH_LONG).show();
        }else
        {
            //PARSE
            new JSONParser(c,jsonData,sp).execute();
        }
    }
    private String download()
    {
        Object connection=Connector.connect(jsonURL);
        if(connection.toString().startsWith("Error"))
        {
            return connection.toString();
        }
        try
        {
            HttpURLConnection con= (HttpURLConnection) connection;
            if(con.getResponseCode()== HttpURLConnection.HTTP_OK)
            {
                //GET INPUT FROM STREAM
                InputStream is=new BufferedInputStream(con.getInputStream());
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder jsonData=new StringBuilder();
                //READ
                while ((line=br.readLine()) != null)
                {
                    jsonData.append(line).append("\n");
                }
                //CLOSE RESOURCES
                br.close();

                is.close();
                //RETURN JSON
                return jsonData.toString();
            }else
            {
                return "Error "+con.getResponseMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error "+e.getMessage();
        }
    }
}