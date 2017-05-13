package com.example.satyam.myapplication;

import android.content.Context;
import android.os.StrictMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by satyam on 9/18/16.
 */
public class Download {



    public static void DownloadFile(String fileURL, File directory, Context cn) {
        try {


            FileOutputStream f = new FileOutputStream(directory);
            URL u = new URL(fileURL);


            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            c.connect();

            InputStream in = c.getInputStream();


            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

