package com.example.yosk7.drivertaxiapp.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.example.yosk7.drivertaxiapp.R;

public class PreMain extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pre_main);
// set timer to show this activity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(PreMain.this, LoginActivity.class));
                finish(); // close this activity
            }
        }, 2000);




    }






}