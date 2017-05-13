package com.example.satyam.myapplication.Game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.satyam.myapplication.R;



/**
 * Created by satyam on 10/9/16.
 */
public class GameStart extends AppCompatActivity implements View.OnClickListener{
    //image button
    private ImageButton buttonPlay;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game1);

        //setting the orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //getting the button
        buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);

        //adding a click listener
        buttonPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //starting game activity
        startActivity(new Intent(this, com.example.satyam.myapplication.Game.GameActivity.class));
    }
    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
            android.os.Process.killProcess(android.os.Process.myPid());

            // System.exit(0);
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();

    }

}
