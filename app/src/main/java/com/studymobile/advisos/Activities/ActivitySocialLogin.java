package com.studymobile.advisos.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.studymobile.advisos.R;

import java.util.Objects;

public class ActivitySocialLogin extends AppCompatActivity implements View.OnClickListener
{
    private Button m_BtnGoogle;
    private Button m_BtnFacebook;

    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_social_login);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        m_BtnGoogle = findViewById(R.id.btn_google_login);
        m_BtnGoogle.setOnClickListener(ActivitySocialLogin.this);
        m_BtnFacebook = findViewById(R.id.btn_facebook_login);
        m_BtnFacebook.setOnClickListener(ActivitySocialLogin.this);
    }

    @Override
    public void onClick(View i_View) {
        int id = i_View.getId();
        if (id == m_BtnFacebook.getId() || id == m_BtnGoogle.getId())
        {
            sendUserToUserDetailsActivity();
        }
    }

    private void sendUserToUserDetailsActivity()
    {
        Intent IntentUserDetails = new Intent
                (ActivitySocialLogin.this, ActivityUserDetails.class);
        startActivity(IntentUserDetails);
    }
}
