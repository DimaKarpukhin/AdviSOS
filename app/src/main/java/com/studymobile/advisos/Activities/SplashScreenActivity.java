package com.studymobile.advisos.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import com.studymobile.advisos.R;

public class SplashScreenActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent activitySignUpWithPhoneNum = new Intent
                        (SplashScreenActivity.this, SignUpWithPhoneNumActivity.class);
                startActivity(activitySignUpWithPhoneNum);
                finish();
            }
        }, 3000);
    }
}

