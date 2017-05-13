package com.example.satyam.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        WebView browser = (WebView) findViewById(R.id.admin);
        browser.loadUrl("http://192.168.43.21/phpmyadmin");


/*        Intent intentUrl = new Intent(Intent.ACTION_VIEW);
        Uri uri=null;
        intentUrl.setDataAndType(uri, "application/pdf");
        intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentUrl);*/
    }
}
