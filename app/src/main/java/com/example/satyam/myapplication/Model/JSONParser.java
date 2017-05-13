package com.example.satyam.myapplication.Model;

/**
 * Created by satyam on 9/7/16.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
public class JSONParser extends AsyncTask<Void,Void,Boolean>{
    Context c;
    String jsonData;
    Spinner sp;
    ProgressDialog pd;
    ArrayList<String> users=new ArrayList<>();
    public JSONParser(Context c, String jsonData, Spinner sp) {
        this.c = c;
        this.jsonData = jsonData;
        this.sp = sp;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);

        users.add("Select College");


        /* for(int i = 0; i< faculties.size(); i++)
            {
                f.add(faculties[i]);
                c.add(colleges[i]);
            }*/
            /*mItems = new ArrayList<String>();
            mItems.add("Amazing Spiderman 2");
            mItems.add("The Guardians of Galaxy");
            mItems.add("What If");
            mItems.a4s3d2f1

            mItems.add("The Hunger Game");
            mItems.add("X-men: Days of Future Past");
            mItems.add("The Lego Movie");
            mItems.add("How to Train Your Dragon 2");
            mItems.add("Maleficent");
            mItems.add("22 Jump Street");
            mItems.add("The Maze Runner");
            mItems.add("Horrible Bosses 2");
            mItems.add("Night at the Museum 3");*/

        pd.setTitle("Parse JSON");
        pd.setMessage("Parsing...Please wait");
        pd.show();
    }
    @Override
    protected Boolean doInBackground(Void... voids)
    {
        return this.parse();
    }
    @Override
    protected void onPostExecute(Boolean isParsed) {
        super.onPostExecute(isParsed);
        pd.dismiss();
        if(isParsed)
        {
            //BIND
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,users);
            sp.setAdapter(adapter);
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                     }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
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
            //users.clear();
           // users.add("Select College");
            //sp.setPrompt("Select College");
            for (int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                String name=jo.getString("COLLEGE");
                users.add(name);


            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}