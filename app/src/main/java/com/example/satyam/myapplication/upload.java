package com.example.satyam.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class upload extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        WebView browser = (WebView) findViewById(R.id.webview);
        browser.loadUrl("http://192.168.43.21/notes.php");


        Intent intentUrl = new Intent(Intent.ACTION_VIEW);
        Uri uri=null;
        intentUrl.setDataAndType(uri, "application/pdf");
        intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       // faculty_activity.startActivity(intentUrl);
    }
}
