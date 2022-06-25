package com.example.rjmusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();


        Thread thread = new Thread(){

            public void run(){
                try {
                    sleep(2000);

                }
                catch (Exception e){
                    e.printStackTrace();

                }
                finally {
                    Intent intent =new Intent(splash.this ,MainActivity.class);
                    startActivity(intent);
                    finish();

                }

            }


        };

        thread.start();
    }
}