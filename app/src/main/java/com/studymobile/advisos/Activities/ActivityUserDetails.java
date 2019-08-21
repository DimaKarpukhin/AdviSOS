package com.studymobile.advisos.Activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.studymobile.advisos.R;

import java.util.Objects;

public class ActivityUserDetails extends AppCompatActivity implements View.OnClickListener
{
    private static final String EXTRA_PHONE_STR = "phone";
    private static final String EXTRA_EMAIL_STR = "email";
    private static final String EXTRA_FIRST_NAME_STR = "firstName";
    private static final String EXTRA_LAST_NAME_STR = "lastName";

    private ImageButton m_BtnNext;
    private EditText m_FieldPhoneNumber;
    private EditText m_FieldEmail;
    private EditText m_FieldFirstName;
    private EditText m_FieldLastName;

    private String m_PhoneNumber = null;
    private String m_Email = null;
    private String m_FirstName = null;
    private String m_LastName = null;

    private FirebaseAuth m_Auth;
    private FirebaseDatabase m_DataBase;

    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_user_details);
        setActivityContent();
        setFirebaseData();
    }

    @Override
    public void onClick(View i_View)
    {
        int id = i_View.getId();
        if(id == m_BtnNext.getId())
        {
            if(isUserDetailsCompleted())
            {
                startHomeActivity();
            }
        }
    }

    private boolean isUserDetailsCompleted()
    {
        String errorMessage = "The field can't be empty";
        m_FirstName = m_FieldFirstName.getText().toString();
        m_LastName = m_FieldLastName.getText().toString();
        m_Email = m_FieldEmail.getText().toString();
        m_PhoneNumber = m_FieldPhoneNumber.getText().toString();

        if(m_FirstName.isEmpty())
        {
            m_FieldFirstName.setError(errorMessage);
            return false;
        }
        else if(m_LastName.isEmpty())
        {
            m_FieldLastName.setError(errorMessage);
            return false;
        }
        else if(m_Email.isEmpty())
        {
            m_FieldEmail.setError(errorMessage);
            return false;
        }
        else if(m_PhoneNumber.isEmpty())
        {
            m_FieldPhoneNumber.setError(errorMessage);
            return false;
        }

        return true;
    }

    private void setActivityContent()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        m_BtnNext = findViewById(R.id.btn_next_of_user_details);
        m_BtnNext.setOnClickListener(ActivityUserDetails.this);
        m_FieldPhoneNumber = findViewById(R.id.field_phone_num_of_user_details);
        m_FieldEmail = findViewById(R.id.field_email_of_user_details);
        m_FieldFirstName = findViewById(R.id.field_first_name_of_user_details);
        m_FieldLastName = findViewById(R.id.field_last_name_of_user_details);

        getIntentExtras();
    }

    private void getIntentExtras()
    {
        Intent intent = getIntent();
        if(intent != null)
        {
            m_FirstName = intent.getStringExtra(EXTRA_FIRST_NAME_STR);
            m_LastName = intent.getStringExtra(EXTRA_LAST_NAME_STR);
            m_Email = intent.getStringExtra(EXTRA_EMAIL_STR);
            m_PhoneNumber = intent.getStringExtra(EXTRA_PHONE_STR);

            m_FieldFirstName.setText(m_FirstName);
            m_FieldLastName.setText(m_LastName);
            m_FieldEmail.setText(m_Email);
            m_FieldPhoneNumber.setText(m_PhoneNumber);
        }
    }

    private void setFirebaseData()
    {
        m_Auth = FirebaseAuth.getInstance();
        m_DataBase = FirebaseDatabase.getInstance();
    }

    private void startHomeActivity()
    {
        Intent IntentHome = new Intent
                (ActivityUserDetails.this, HomeActivity.class);
        startActivity(IntentHome);
    }
}
