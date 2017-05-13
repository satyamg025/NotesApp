package com.example.satyam.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.satyam.myapplication.Model.JSONDownloader;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity  {

    Spinner cllg_names;

    Button btn;
    String jsonURL;
    List<String> acc=new ArrayList<String>();
    Spinner acc_type;
    Toolbar toolbar;
    boolean doubleBackToExitPressedOnce=false;


    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        jsonURL="http://192.168.43.21/test1.php";


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        acc_type=(Spinner)findViewById(R.id.account_type);
        toolbar=(Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("NoteBox");
        //toolbar.setTitle("Select College");


        addItemsOncllg_names();
        addListenerOnButton();
        acc.add("Select Account Type");
        acc.add("Faculty Login");
        acc.add("Student Login");
        acc.add("Admin Login");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, acc);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acc_type.setAdapter(dataAdapter);
       // addListenerOnSpinnerItemSelection();
    }
    public void checkNetwork(){
        if (!NetworkCheck.isNetworkAvailable(MainActivity.this)) {

           Toast.makeText(MainActivity.this,"Not connected to network",Toast.LENGTH_LONG).show();
            return;
        }

    }

    // add items into spinner dynamically
    public void addItemsOncllg_names() {

        cllg_names= (Spinner) findViewById(R.id.spinner);

        new JSONDownloader(MainActivity.this, jsonURL, cllg_names).execute();


        /*cllg_names = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("Krishna Institute of Engineering & Technology");
        list.add("Krishna Engineering College");
        list.add("Ajay Kumar Garg");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cllg_names.setAdapter(dataAdapter);*/


    }



    // get the selected dropdown list value
    public void addListenerOnButton() {

        //spinner1 = (Spinner) findViewById(R.id.spinner1);
        cllg_names = (Spinner) findViewById(R.id.spinner);
        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v == btn) {
                    Integer pos = cllg_names.getSelectedItemPosition();


                    Integer pos2 = acc_type.getSelectedItemPosition();
                    String college = cllg_names.getItemAtPosition(pos).toString();


                    String ac_tp = acc_type.getItemAtPosition(pos2).toString();
                    if (Objects.equals(ac_tp, "Admin Login")) {
                        Intent intent = new Intent(MainActivity.this, Admin.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.pull_left, R.anim.push_right);


                    } else {
                        if (pos2 == 0) {
                            Snackbar.make(findViewById(android.R.id.content), "Select acc type", Snackbar.LENGTH_LONG).setActionTextColor(Color.RED)
                                    .show();
                        } else if (pos == 0) {
                            Snackbar.make(findViewById(android.R.id.content), "Select College", Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.RED)
                                    .show();

                        } else {

                            Intent intent = new Intent(v.getContext(), login_activity.class);
                            intent.putExtra("college", college);
                            intent.putExtra("acc_type", ac_tp);
                            intent.putExtra("username", "null");
                            intent.putExtra("password", "null");
                            startActivity(intent);
                            overridePendingTransition(R.anim.pull_left, R.anim.push_right);
                        }

                    }
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
       /* if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                startActivity(intent);
                finish();
                System.exit(0);
                doubleBackToExitPressedOnce=false;


            }
        }, 2000);
        if(!doubleBackToExitPressedOnce)
        {

        }*/

        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
            android.os.Process.killProcess(android.os.Process.myPid());

            //System.exit(0);
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
}

