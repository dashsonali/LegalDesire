package com.official.user.legaldesire;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class SplashActivity extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = findViewById(R.id.logo);
        final RelativeLayout rl = findViewById(R.id.splash);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein_effect);
        imageView.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isOnline())
                {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Snackbar snackbar = Snackbar.make(rl, "No Internet Connection",
                            Snackbar.LENGTH_INDEFINITE).setAction("Retry",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recreate();
                                }
                            });

                    snackbar.show();

                }

            }
        },2000);



    }

    private boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
       }
    }

