package com.studymobile.advisos.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.studymobile.advisos.R;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitySplashScreen extends AppCompatActivity
{
    FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        if(mCurrentUser != null)
        {
            startHomeScreenActivity();
        }
        else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    startRegistrationActivity();
                    finish();
                }
            }, 3000);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if(mCurrentUser != null)
        {
            startHomeScreenActivity();
        }
    }

    private void startRegistrationActivity()
    {
        Intent IntentRegistration = new Intent
                (ActivitySplashScreen.this, ActivityRegistration.class);
        startActivity(IntentRegistration);
    }

    private void startHomeScreenActivity()
    {
        Intent IntentHomeScreen = new Intent
                (ActivitySplashScreen.this, ActivityHomeScreen.class);
        startActivity(IntentHomeScreen);
    }
}

