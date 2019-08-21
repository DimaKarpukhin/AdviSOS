package com.studymobile.advisos.Activities;

import android.content.Intent;
import android.os.Parcelable;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.studymobile.advisos.R;
import com.studymobile.advisos.Services.InputValidation;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ActivityPhoneNumLogin extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "PhoneAuth";
    private static final String EXTRA_VERIFICATION_ID_STR = "verification";
    private static final String EXTRA_TOKEN_PARCELABLE = "token";
    private static final String EXTRA_PHONE_STR = "phone";

    private ImageButton m_BtnNext;
    private EditText m_FieldPhoneNumber;
    private CountryCodePicker m_CountryCodePicker;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            m_VerificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken m_ResendToken;
    private String m_VerificationId;
    private String m_InternationalPhoneNumber;

    private FirebaseAuth m_Auth;

    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_phone_num_login);
        setActivityContent();
        setFirebaseData();

    }

//    @Override
//    public void onStart()
//    {
//        super.onStart();
//        FirebaseUser currentUser = m_Auth.getCurrentUser();
//
//        if(currentUser!= null)
//        {
//            startHomeActivity();
//        }
//    }

    @Override
    public void onClick(View i_View)
    {
        int id = i_View.getId();

        if(id == m_BtnNext.getId())
        {
            m_InternationalPhoneNumber = m_CountryCodePicker.getFullNumberWithPlus();
            if (isValidPhoneNumber())
            {
                sendVerificationCode();
            }
        }
    }

    private boolean isValidPhoneNumber()
    {
        String localPhoneNumber = m_FieldPhoneNumber.getText().toString();

        if (localPhoneNumber.isEmpty())
        {
            m_FieldPhoneNumber.setError("This field can't be empty");
            return false;
        }
        else if(!InputValidation.IsValidPhoneNumber(m_InternationalPhoneNumber))
        {
            m_FieldPhoneNumber.setError("Invalid phone number");
            return false;
        }

        return  true;
    }

    private void sendVerificationCode()
    {
        setUpVerificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                m_InternationalPhoneNumber,  // Phone number to verify
                60,                        // Timeout duration
                TimeUnit.SECONDS,            // Unit of timeout
                this,                  // Activity (for callback binding)
                m_VerificationCallbacks);
    }

    private void setUpVerificationCallbacks()
    {
        m_VerificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
                {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential i_Credential) {
                        Log.d(TAG, "onVerificationCompleted:" + i_Credential);
                        signInWithPhoneAuthCredential(i_Credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException)
                        {
                            // Invalid request
                            Log.e(TAG, "Invalid credential: " + e.getLocalizedMessage());
                            m_FieldPhoneNumber.setError("Invalid phone number.");
                        }
                        else if (e instanceof FirebaseTooManyRequestsException)
                        {
                            // SMS quota exceeded
                            Log.e(TAG, "SMS Quota exceeded.");
                            Snackbar.make(findViewById(android.R.id.content), "SMS quota exceeded.",
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCodeSent(String i_VerificationId,
                                           PhoneAuthProvider.ForceResendingToken i_Token) {
                        m_VerificationId = i_VerificationId;
                        m_ResendToken = i_Token;
                        startCodeVerificationActivity();

                    }
                };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential i_Credential)
    {
        m_Auth.signInWithCredential(i_Credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> i_Task) {
                        if (i_Task.isSuccessful())
                        {
                            startUserDetailsActivity();

                        } else {
                            if (i_Task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                Snackbar.make(findViewById(android.R.id.content),
                                        i_Task.getException().getLocalizedMessage(),
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void setActivityContent()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        m_FieldPhoneNumber = findViewById(R.id.field_phone_of_phone_num_login);
        m_CountryCodePicker =  findViewById(R.id.picker_of_country_code);
        m_CountryCodePicker.registerCarrierNumberEditText(m_FieldPhoneNumber);
        m_BtnNext = findViewById(R.id.btn_next_of_phone_num_login);
        m_BtnNext.setOnClickListener(ActivityPhoneNumLogin.this);
    }

    private void setFirebaseData()
    {
        m_Auth = FirebaseAuth.getInstance();
    }

    private void startCodeVerificationActivity()
    {
        Intent IntentCodeVerification = new Intent
                (ActivityPhoneNumLogin.this, ActivityCodeVerification.class);
        IntentCodeVerification.putExtra(EXTRA_VERIFICATION_ID_STR, m_VerificationId);
        IntentCodeVerification.putExtra(EXTRA_TOKEN_PARCELABLE, (Parcelable)m_ResendToken);
        IntentCodeVerification.putExtra(EXTRA_PHONE_STR, m_InternationalPhoneNumber);
        startActivity(IntentCodeVerification);
    }

    private void startUserDetailsActivity()
    {
        Intent IntentUserDetails = new Intent
                (ActivityPhoneNumLogin.this, ActivityUserDetails.class);
        String localPhoneNumber = m_InternationalPhoneNumber.substring(4);
        IntentUserDetails.putExtra(EXTRA_PHONE_STR, localPhoneNumber);
        startActivity(IntentUserDetails);
    }

    private void startHomeActivity()
    {
        Intent IntentHome = new Intent
                (ActivityPhoneNumLogin.this, HomeActivity.class);
        startActivity(IntentHome);
    }
}
