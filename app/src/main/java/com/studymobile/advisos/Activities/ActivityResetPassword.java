package com.studymobile.advisos.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.studymobile.advisos.R;

import java.util.Objects;

public class ActivityResetPassword extends AppCompatActivity implements View.OnClickListener
{
    private ImageButton m_BtnNext;

    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        m_BtnNext = findViewById(R.id.btn_next_of_reset_password);
        m_BtnNext.setOnClickListener(ActivityResetPassword.this);
    }

    @Override
    public void onClick(View i_View)
    {
        int id = i_View.getId();
        if(id == m_BtnNext.getId())
        {
            sendUserToPasswordLoginActivity();
        }
    }

    private void sendUserToPasswordLoginActivity()
    {
        Intent IntentResetPassword = new Intent
                (ActivityResetPassword.this, ActivityPasswordLogin.class);
        startActivity(IntentResetPassword);
    }
}
