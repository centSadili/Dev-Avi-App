package com.example.design;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 3000; // 3 seconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        getWindow().setStatusBarColor(ContextCompat.getColor(SplashScreen.this,R.color.statusbar));

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Start the MainActivity after the splash screen delay
                Intent intent = new Intent(SplashScreen.this, com.example.design.MainActivity.class);
                startActivity(intent);
                finish(); // Finish the splash screen activity
            }
        };

        // Create a Timer object
        Timer timer = new Timer();

        // Schedule the TimerTask to run after the splash screen delay
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
}