package com.example.satyam.myapplication.Model2;

/**
 * Created by satyam on 9/7/16.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParser2 extends AsyncTask<Void,Void,Boolean>{
    Context c;
    String jsonData;
    List<String> cllg=new ArrayList<String>();
    ProgressDialog pd;
    ArrayList<String> users=new ArrayList<>();
    public JSONParser2(Context c, String jsonData, List<String> colleges) {
        this.c = c;
        this.jsonData = jsonData;
        this.cllg=colleges;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Parse JSON");
        pd.setMessage("Parsing...Please wait");
        pd.show();
    }
    @Override
    protected Boolean doInBackground(Void... voids) {
        return this.parse();
    }
    @Override
    protected void onPostExecute(Boolean isParsed) {
        super.onPostExecute(isParsed);
        pd.dismiss();
        if(isParsed)
        {
            //BIND

        }else
        {
            Toast.makeText(c, "Unable To Parse,Check Your Log output", Toast.LENGTH_SHORT).show();
        }
    }
    private Boolean parse()
    {
        try
        {
            JSONArray ja=new JSONArray(jsonData);
            JSONObject jo;
            users.clear();
            //users.add("Select College");
            //sp.setPrompt("Select College");
            for (int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                String name=jo.getString("COLLEGE");
                cllg.add(name);


            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}