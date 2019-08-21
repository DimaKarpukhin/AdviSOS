package com.studymobile.advisos.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.studymobile.advisos.R;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitySplashScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent IntentRegistration = new Intent
                        (ActivitySplashScreen.this, ActivityRegistration.class);
                startActivity(IntentRegistration);
                finish();
            }
        }, 3000);
    }
}

