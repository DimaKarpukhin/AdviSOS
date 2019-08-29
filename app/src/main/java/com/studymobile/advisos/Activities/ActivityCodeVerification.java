package com.studymobile.advisos.Activities;

import android.content.Intent;

import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.studymobile.advisos.R;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ActivityCodeVerification extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "PhoneAuth";
    private static final String EXTRA_VERIFICATION_ID_STR = "verification";
    private static final String EXTRA_TOKEN_PARCELABLE = "token";
    private static final String EXTRA_PHONE_STR = "phone";
    private static final String AUTH_CONTEXT = "auth_context";
    private static final String PHONE_AUTH = "phone";


    private ImageButton m_BtnNext;
    private TextView m_LinkResendCode;
    private Pinview m_VerificationCodePicker;

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
        setContentView(R.layout.activity_code_verification);
        setActivityContent();
        setFirebaseData();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View i_View)
    {
        int id = i_View.getId();
        String code = m_VerificationCodePicker.getValue();

        if(id == m_BtnNext.getId())
        {
            if (isCodeLengthValid(code))
            {
                verifyPhoneNumberWithCode(code);
            }
        }
        else if(id == m_LinkResendCode.getId())
        {
          resendVerificationCode();
        }
    }

    private boolean isCodeLengthValid(String i_Code)
    {
        if (i_Code.length() < 6)
        {
            Toast.makeText(this, "The verification code is to short", Toast.LENGTH_SHORT).show();
//            Snackbar.make(findViewById(android.R.id.content),
//                    "The verification code is to short", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void resendVerificationCode()
    {
        setUpVerificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                m_InternationalPhoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                m_VerificationCallbacks,
                m_ResendToken);
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
                            Toast.makeText(ActivityCodeVerification.this,
                                    "Something went wrong", Toast.LENGTH_SHORT).show();
//                            Snackbar.make(findViewById(android.R.id.content), "Something went wrong",
//                                    Snackbar.LENGTH_SHORT).show();
                        }
                        else if (e instanceof FirebaseTooManyRequestsException)
                        {
                            // SMS quota exceeded
                            Log.e(TAG, "SMS Quota exceeded.");
                            Toast.makeText(ActivityCodeVerification.this,
                                    "SMS quota exceeded.", Toast.LENGTH_SHORT).show();
//                            Snackbar.make(findViewById(android.R.id.content), "SMS quota exceeded.",
//                                    Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCodeSent(String i_VerificationId,
                                           PhoneAuthProvider.ForceResendingToken i_Token) {
                        m_VerificationId = i_VerificationId;
                        m_ResendToken = i_Token;
                    }
                };
    }

    private void verifyPhoneNumberWithCode(String i_Code)
    {
        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(m_VerificationId, i_Code);
        signInWithPhoneAuthCredential(credential);
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
                                Toast.makeText(ActivityCodeVerification.this,
                                        i_Task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                                Snackbar.make(findViewById(android.R.id.content),
//                                        i_Task.getException().getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void setActivityContent()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        if(intent != null)
        {
            m_VerificationId = intent.getStringExtra(EXTRA_VERIFICATION_ID_STR);
            m_ResendToken = intent.getParcelableExtra(EXTRA_TOKEN_PARCELABLE);
            m_InternationalPhoneNumber = intent.getStringExtra(EXTRA_PHONE_STR);
        }

        m_VerificationCodePicker = findViewById(R.id.picker_of_code_verification);
        m_BtnNext = findViewById(R.id.btn_next_of_code_verification);
        m_BtnNext.setOnClickListener(ActivityCodeVerification.this);
        m_LinkResendCode = findViewById(R.id.link_resend_code);
        m_LinkResendCode.setOnClickListener(ActivityCodeVerification.this);
    }

    private void setFirebaseData()
    {
        m_Auth = FirebaseAuth.getInstance();
    }

    private void startUserDetailsActivity()
    {
        Intent IntentUserDetails = new Intent
                (ActivityCodeVerification.this, ActivityUserDetails.class);
        String localPhoneNumber = m_InternationalPhoneNumber.substring(4);
        IntentUserDetails.putExtra(EXTRA_PHONE_STR, localPhoneNumber);
        IntentUserDetails.putExtra(AUTH_CONTEXT, PHONE_AUTH);
        startActivity(IntentUserDetails);
    }
}
