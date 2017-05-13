package com.example.satyam.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;

public class Like extends Activity {

    private Animation animShow, animHide;

    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        setContentView(R.layout.activity_like);
        initPopup();
    }

    private void initPopup() {

       /* final SlidingPanel popup = (SlidingPanel) findViewById(R.id.popup_window);

        // Hide the popup initially.....
      //  popup.setVisibility(View.GONE);

        animShow = AnimationUtils.loadAnimation( this, R.anim.popup_show);
        animHide = AnimationUtils.loadAnimation( this, R.anim.popup_hide);

        final ImageButton   showButton = (ImageButton) findViewById(R.id.show_popup_button);
        final ImageButton   hideButton = (ImageButton) findViewById(R.id.hide_popup_button);
        //showButton.setOnClickListener(new View.OnClickListener() {
          //  public void onClick(View view) {
                popup.setVisibility(View.VISIBLE);
                popup.startAnimation( animShow );
                showButton.setEnabled(false);
                hideButton.setEnabled(true);
            //}});

       /* hideButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popup.startAnimation( animHide );
                showButton.setEnabled(true);
                hideButton.setEnabled(false);
                popup.setVisibility(View.GONE);
            }});
*/
       /* final TextView locationName = (TextView) findViewById(R.id.site_name);
        final TextView locationDescription =
        (TextView) findViewById(R.id.site_description);

        locationName.setText("CoderzHeaven");
        locationDescription.setText("Heaven of all working codes"
                + " A place where you can ask, share & even shout for code! Let’s share a wide range of technology here." +
                " From this site you will get a lot of working examples in your favorite programming languages!." +
                " Always remember we are only one comment away from you… Let’s shorten the distance between your doubts and your answers…");

    }*/
    }
}

