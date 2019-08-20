package com.studymobile.advisos.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.studymobile.advisos.R;

import java.util.Objects;

public class ActivityRegistration extends AppCompatActivity implements View.OnClickListener
{
    private Button m_BtnEmailPasswordLogin;
    private Button m_BtnPhoneNumLogin;
    private Button m_BtnSocialLogin;

    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_registration);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        m_BtnEmailPasswordLogin = findViewById(R.id.btn_email_password_connection);
        m_BtnEmailPasswordLogin.setOnClickListener(ActivityRegistration.this);
        m_BtnSocialLogin = findViewById(R.id.btn_social_connection);
        m_BtnSocialLogin.setOnClickListener(ActivityRegistration.this);
        m_BtnPhoneNumLogin = findViewById(R.id.btn_phone_num_connection);
        m_BtnPhoneNumLogin.setOnClickListener(ActivityRegistration.this);
    }

    @Override
    public void onClick(View i_View)
    {
        int id = i_View.getId();
        if(id == m_BtnEmailPasswordLogin.getId())
        {
            startPasswordLoginActivity();
        }
        else if(id == m_BtnSocialLogin.getId())
        {
            startSocialLoginActivity();
        }
        else if(id == m_BtnPhoneNumLogin.getId())
        {
            startPhoneNumLoginActivity();
        }
    }

    private void startPasswordLoginActivity()
    {
        Intent IntentPasswordLogin = new Intent
                (ActivityRegistration.this, ActivityPasswordLogin.class);
        startActivity(IntentPasswordLogin);
    }

    private void startSocialLoginActivity()
    {
        Intent IntentSocialLogin = new Intent
                (ActivityRegistration.this, ActivitySocialLogin.class);
        startActivity(IntentSocialLogin);
    }

    private void startPhoneNumLoginActivity()
    {
        Intent IntentPhoneNumLogin = new Intent
                (ActivityRegistration.this, ActivityPhoneNumLogin.class);
        startActivity(IntentPhoneNumLogin);
    }
}
