package com.studymobile.advisos.Activities;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.studymobile.advisos.R;
import com.studymobile.advisos.Services.InputValidation;

import java.util.Objects;

public class ActivityResetPassword extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "ResetPassword";
    private static final String EXTRA_EMAIL_STR = "email";

    private ImageButton m_BtnNext;
    private EditText m_FieldEmail;
    private String m_Email;

    private FirebaseAuth m_Auth;

    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_reset_password);
        setActivityContent();
        setFirebaseData();
    }

    @Override
    public void onClick(View i_View)
    {
        int id = i_View.getId();

        if(id == m_BtnNext.getId())
        {
            if(isValidEmail())
            {
                sendEmailToResetUserPassword();
            }
        }
    }

    private boolean isValidEmail()
    {
        m_Email = m_FieldEmail.getText().toString();

        if (m_Email.isEmpty())
        {
            m_FieldEmail.setError("The field can't be empty");
            return false;
        }
        else if(!InputValidation.IsValidEmail(m_Email))
        {
            m_FieldEmail.setError("Invalid email address");
            return false;
        }

        return true;
    }

    private void sendEmailToResetUserPassword()
    {
        m_Auth.sendPasswordResetEmail(m_Email)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> i_Task)
                        {
                            if(i_Task.isSuccessful())
                            {
                                Log.d(TAG, "loginWithPassword:success");
                                Snackbar.make(findViewById(android.R.id.content),
                                        "Please check your email address to reset your password",
                                        Snackbar.LENGTH_SHORT).show();
                                startPasswordLoginActivity();
                            }
                            else{
                                Log.e(TAG, "loginWithPassword:failure");
                                String msg = Objects.requireNonNull(i_Task.getException()).getLocalizedMessage();
                                Snackbar.make(findViewById(android.R.id.content),
                                        "ERROR:\n" + msg, Snackbar.LENGTH_SHORT).show();

                            }
                        }
                    });
    }

    private void setActivityContent()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        m_BtnNext = findViewById(R.id.btn_next_of_reset_password);
        m_BtnNext.setOnClickListener(ActivityResetPassword.this);
        m_FieldEmail = findViewById(R.id.field_email_of_reset_password);
    }

    private void setFirebaseData()
    {
        m_Auth = FirebaseAuth.getInstance();
    }

    private void startPasswordLoginActivity()
    {
        Intent IntentResetPassword = new Intent
                (ActivityResetPassword.this, ActivityPasswordLogin.class);
        IntentResetPassword.putExtra(EXTRA_EMAIL_STR, m_Email);
        startActivity(IntentResetPassword);
    }
}
