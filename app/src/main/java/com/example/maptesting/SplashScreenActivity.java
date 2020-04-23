package com.example.maptesting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView imageView = findViewById( R.id.imagelogo );
        Animation animation = AnimationUtils.loadAnimation( getApplicationContext(),R.anim.fade );
        imageView.startAnimation( animation );
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            recreate();
            return;
        }
        Thread timer = new Thread(  ) {
            @Override
            public void run() {
                try {
                    sleep( 5000 );

//                    SharedPreferences prefs = getSharedPreferences("Log", MODE_PRIVATE);
//                    boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
//                    boolean cat = prefs.getBoolean("cat", false);
//                    if (isLoggedIn) {
//                        if(cat){
//                            Intent intent = new Intent(getApplicationContext(), HelperActivity.class );
//                            startActivity( intent );
//                            finish();
//                        }
//                        else{
//                            Intent intent = new Intent(getApplicationContext(), VictumHelpActivity.class );
//                            startActivity( intent );
//                            finish();
//                        }
//                    } else {
//                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class );
//                        startActivity( intent );
//                        finish();
//                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class );
                    startActivity( intent );
                    finish();
                    super.run();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        timer.start();
    }
}
