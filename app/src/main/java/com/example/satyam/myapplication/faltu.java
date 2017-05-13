package com.example.satyam.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

public class faltu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faltu);


        Spinner cllg_names;
        cllg_names=(Spinner)findViewById(R.id.spinner);
        String jsonURL="http://192.168.43.21/test1.php";

        new com.example.satyam.myapplication.Model3.JSONDownloader(faltu.this, jsonURL, cllg_names).execute();

    }
}
