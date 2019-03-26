package com.capivas.mybeers.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.capivas.mybeers.R;

public class SplashScreenActivity extends AppCompatActivity {
    private static final long SPLASH_SCREEN_DELAY = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goToHomeScreen();
            }
        }, SPLASH_SCREEN_DELAY);
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
