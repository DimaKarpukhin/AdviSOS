package com.studymobile.advisos.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.studymobile.advisos.R;

import java.util.Objects;

public class ActivityPasswordLogin extends AppCompatActivity implements View.OnClickListener
{
    private ImageButton m_BtnNext;
    private TextView m_LinkSignUp;
    private TextView m_LinkResetPassword;

    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_password_login);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        m_BtnNext = findViewById(R.id.btn_next_of_password_login);
        m_BtnNext.setOnClickListener(ActivityPasswordLogin.this);
        m_LinkSignUp = findViewById(R.id.link_create_new_account);
        m_LinkSignUp.setOnClickListener(ActivityPasswordLogin.this);
        m_LinkResetPassword = findViewById(R.id.link_forgot_password);
        m_LinkResetPassword.setOnClickListener(ActivityPasswordLogin.this);
    }

    @Override
    public void onClick(View i_View)
    {
        int id = i_View.getId();

        if(id == m_BtnNext.getId())
        {
            sendUserToUserDetailsActivity();
        }
        else if (id == m_LinkSignUp.getId())
        {
            moveToPasswordSignUpActivity();
        }
        else if (id == m_LinkResetPassword.getId())
        {
            moveToResetPasswordActivity();
        }
    }

    private void moveToPasswordSignUpActivity()
    {
        Intent IntentPasswordSignUp = new Intent
                (ActivityPasswordLogin.this, ActivityPasswordSignUp.class);
        startActivity(IntentPasswordSignUp);
    }

    private void moveToResetPasswordActivity()
    {
        Intent IntentResetPassword = new Intent
                (ActivityPasswordLogin.this, ActivityResetPassword.class);
        startActivity(IntentResetPassword);
    }

    private void sendUserToUserDetailsActivity()
    {
        Intent IntentUserDetails = new Intent
                (ActivityPasswordLogin.this, ActivityUserDetails.class);
        startActivity(IntentUserDetails);
    }
}
