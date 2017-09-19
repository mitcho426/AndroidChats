package com.example.mitchell.mychat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    //Display this screen for 3 seconds
                    sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                finally {
                    //Send user to Login Activity
                    Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }
            }
        };

        thread.start();
    }

    private int count = 5;




}
