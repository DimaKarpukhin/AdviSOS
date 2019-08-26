package com.studymobile.advisos.Activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.studymobile.advisos.R;
import com.studymobile.advisos.Services.InputValidation;

import java.util.Objects;

public class ActivityUserDetails extends AppCompatActivity implements View.OnClickListener
{
    private static final String EXTRA_PHONE_STR = "phone";
    private static final String EXTRA_EMAIL_STR = "email";
    private static final String EXTRA_FIRST_NAME_STR = "firstName";
    private static final String EXTRA_LAST_NAME_STR = "lastName";

    private ImageButton m_BtnNext;
    private ImageButton m_ProfilePicture;
    private EditText m_FieldPhoneNumber;
    private EditText m_FieldEmail;
    private EditText m_FieldFirstName;
    private EditText m_FieldLastName;
    private CountryCodePicker m_CountryCodePicker;

    private String m_InternationalPhoneNumber = null;
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
    protected void onStart()
    {
        super.onStart();
    }



    @Override
    public void onClick(View i_View)
    {
        int id = i_View.getId();
        if(id == m_BtnNext.getId())
        {
            if(isUserDetailsCompleted())
            {
                //startHomeActivity();
                startExpertConfigActivity();
            }
        }
        else if (id == m_ProfilePicture.getId())
        {

        }
    }

    private boolean isUserDetailsCompleted()
    {
        String errorMsgEmptyField = "The field can't be empty";
        String errorMsgInvalidInput = "Invalid input";
        m_FirstName = m_FieldFirstName.getText().toString();
        m_LastName = m_FieldLastName.getText().toString();
        m_Email = m_FieldEmail.getText().toString();
        m_InternationalPhoneNumber = m_CountryCodePicker.getFullNumberWithPlus();

        if(m_FirstName.isEmpty()) {
            m_FieldFirstName.setError(errorMsgEmptyField);
            return false;
        }
        else if(m_LastName.isEmpty()) {
            m_FieldLastName.setError(errorMsgEmptyField);
            return false;
        }
        else if(m_Email.isEmpty()) {
            m_FieldEmail.setError(errorMsgEmptyField);
            return false;
        }
        else if(!InputValidation.IsValidEmail(m_Email)) {
            m_FieldPhoneNumber.setError(errorMsgInvalidInput);
            return false;
        }
        else if(m_InternationalPhoneNumber.isEmpty()) {
            m_FieldPhoneNumber.setError(errorMsgEmptyField);
            return false;
        }
        else if(!InputValidation.IsValidPhoneNumber(m_InternationalPhoneNumber)) {
            m_FieldPhoneNumber.setError(errorMsgInvalidInput);
            return false;
        }

        return true;
    }

    private void setActivityContent()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        m_ProfilePicture = findViewById(R.id.btn_profile_photo_of_user_details);
        m_ProfilePicture.setOnClickListener(ActivityUserDetails.this);
        m_BtnNext = findViewById(R.id.btn_next_of_user_details);
        m_BtnNext.setOnClickListener(ActivityUserDetails.this);
        m_FieldFirstName = findViewById(R.id.field_first_name_of_user_details);
        m_FieldLastName = findViewById(R.id.field_last_name_of_user_details);
        m_FieldEmail = findViewById(R.id.field_email_of_user_details);
        m_FieldPhoneNumber = findViewById(R.id.field_phone_num_of_user_details);
        m_CountryCodePicker =  findViewById(R.id.picker_of_country_code_user_details);
        m_CountryCodePicker.registerCarrierNumberEditText(m_FieldPhoneNumber);

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
            m_InternationalPhoneNumber = intent.getStringExtra(EXTRA_PHONE_STR);

            m_FieldFirstName.setText(m_FirstName);
            m_FieldLastName.setText(m_LastName);
            m_FieldEmail.setText(m_Email);
            m_FieldPhoneNumber.setText(m_InternationalPhoneNumber);
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

    private void startExpertConfigActivity()
    {
        Intent IntentExpertConfig = new Intent
                (ActivityUserDetails.this, ActivityExpertSettings.class);
        startActivity(IntentExpertConfig);
    }
}
