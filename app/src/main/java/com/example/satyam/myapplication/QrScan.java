package com.example.satyam.myapplication;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QrScan extends AppCompatActivity {
    SurfaceView cameraView;
    TextView barcodeInfo;
    EditText usr, pass;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    String college, acc_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);


        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        // barcodeInfo = (TextView)findViewById(R.id.code_info);
        // usr=(EditText)findViewById(R.id.usr);
        //pass=(EditText)findViewById(R.id.pass);

        Intent intent = getIntent();
        college = intent.getExtras().getString("college");
        acc_type = intent.getExtras().getString("acc_type");

        barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                //return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                // return false;
            }
        }


        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {


                try {

                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {

//                    barcodeInfo.post(new Runnable() {
//Use the post method of the TextView
                    String data=barcodes.valueAt(0).displayValue;

                            String[] parts=data.split("\\s+");
                            String user=parts[0];
                            String pwd =parts[1];
                            if(user!=null && pwd!=null)
                            {
                                Intent intent=new Intent(QrScan.this,login_activity.class);
                                intent.putExtra("college",college);
                                intent.putExtra("acc_type",acc_type);
                                intent.putExtra("username",user);
                                intent.putExtra("password",pwd);
                                startActivity(intent);
                                overridePendingTransition(R.anim.pull_left,R.anim.push_right);

                            }


                }
            }
        });

    }

}

