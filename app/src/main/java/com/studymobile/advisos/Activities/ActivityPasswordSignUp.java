package com.studymobile.advisos.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.studymobile.advisos.R;

import java.util.Objects;

public class ActivityPasswordSignUp extends AppCompatActivity implements View.OnClickListener
{
    private ImageButton m_BtnNext;
    private TextView m_LinkLogin;
    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_password_sign_up);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        m_BtnNext = findViewById(R.id.btn_next_of_password_sign_up);
        m_BtnNext.setOnClickListener(ActivityPasswordSignUp.this);
        m_LinkLogin = findViewById(R.id.link_already_have_an_account);
        m_LinkLogin.setOnClickListener(ActivityPasswordSignUp.this);

    }

    @Override
    public void onClick(View i_View)
    {
        int id = i_View.getId();
        if(id == m_BtnNext.getId())
        {
            sendUserToUserDetailsActivity();
        }
        else if (id == m_LinkLogin.getId())
        {
            moveToPasswordLoginActivity();
        }
    }

    private void moveToPasswordLoginActivity()
    {
        Intent IntentPasswordLogin = new Intent
                (ActivityPasswordSignUp.this, ActivityPasswordLogin.class);
        startActivity(IntentPasswordLogin);
    }

    private void sendUserToUserDetailsActivity()
    {
        Intent IntentUserDetails = new Intent
                (ActivityPasswordSignUp.this, ActivityUserDetails.class);
        startActivity(IntentUserDetails);
    }
}
