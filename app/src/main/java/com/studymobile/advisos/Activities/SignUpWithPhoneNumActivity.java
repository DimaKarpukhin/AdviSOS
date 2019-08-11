package com.studymobile.advisos.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.studymobile.advisos.R;

import java.util.concurrent.TimeUnit;

public class SignUpWithPhoneNumActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "PhoneAuthActivity";


    private FirebaseAuth m_Auth;
    private FirebaseDatabase m_Database;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks m_Callbacks;

    private EditText m_FieldPhoneNumber, m_FieldCode;
    private Button m_BtnSignUp, m_BtnGo;

    private String m_VerificationId;
    private PhoneAuthProvider.ForceResendingToken m_ResendToken;


    //private boolean m_IsVerificationInProgress = false;
    //private ProgressDialog m_LoadingBar;


    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);

        setContent();
        setFirbaseData();
        setVerificationCallBacks();

        // Restore instance state
//        if (i_SavedInstanceState != null) {
//            onRestoreInstanceState(i_SavedInstanceState);
//        }

        //verifyPhoneNumber();
    }

    private void setContent()
    {
        setContentView(R.layout.activity_sign_up_with_phone_num);
        m_FieldPhoneNumber = findViewById(R.id.edit_txt_phone_sign_up);
        m_BtnSignUp = findViewById(R.id.btn_sign_up);
        m_BtnSignUp.setOnClickListener(SignUpWithPhoneNumActivity.this);
        m_FieldCode = findViewById(R.id.edit_txt_code);
        m_BtnGo = findViewById(R.id.btn_go);
        m_BtnGo.setOnClickListener(SignUpWithPhoneNumActivity.this);
        //m_Auth = FirebaseAuth.getInstance();
        //m_LoadingBar = new ProgressDialog(this);
    }

    private void setFirbaseData()
    {
        m_Auth = FirebaseAuth.getInstance();
        m_Database = FirebaseDatabase.getInstance();
    }

    private void setVerificationCallBacks()
    {
        m_Callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.e(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            private void setFirbaseData()
            {
                m_Auth = FirebaseAuth.getInstance();
                m_Database = FirebaseDatabase.getInstance();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.e(TAG, "onVerificationFailed", e);
                //m_LoadingBar.dismiss();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    m_FieldPhoneNumber.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }

                // Show a message and update the UI
                // ...
                Snackbar.make(findViewById(android.R.id.content), e.getLocalizedMessage(),
                        Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                //m_LoadingBar.dismiss();
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                m_VerificationId = verificationId;
                m_ResendToken = token;

            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        m_Auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");
                           // m_LoadingBar.dismiss();
                            //FirebaseUser user = task.getResult().getUser();
                            // ...
                            Intent LoginIntent = new Intent(SignUpWithPhoneNumActivity.this, HomeActivity.class);
                            startActivity(LoginIntent);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(SignUpWithPhoneNumActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View i_View)
    {
        int id = i_View.getId();

        if (id == m_BtnSignUp.getId())
        {
//            m_LoadingBar.setTitle("Phone Verification");
//            m_LoadingBar.setMessage("Wait please");
//            m_LoadingBar.setCanceledOnTouchOutside(false);
//            m_LoadingBar.show();
            String phoneNum = m_FieldPhoneNumber.getText().toString();
            createDatabaseUser(phoneNum);
            signUpWithPhoneNumber();
            //Intent LoginIntent = new Intent(SignUpWithPhoneNumActivity.this, HomeActivity.class);
            //startActivity(LoginIntent);
        }
        else if (id == m_BtnGo.getId())
        {

//            m_LoadingBar.setTitle("Code Verification");
//            m_LoadingBar.setMessage("Wait please");
//            m_LoadingBar.setCanceledOnTouchOutside(false);
//            m_LoadingBar.show();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(m_VerificationId, m_FieldCode.getText().toString());
            signInWithPhoneAuthCredential(credential);
            //Intent LoginIntent = new Intent(SignUpWithPhoneNumActivity.this, HomeActivity.class);
            //startActivity(LoginIntent);
        }
    }

    private void createDatabaseUser(String phoneNum)
    {
        FirebaseUser currentUser = m_Auth.getCurrentUser();

        DatabaseReference DBref = m_Database.getReference("Users");
        DBref.child(currentUser.getUid()).setValue(phoneNum);
    }


    private boolean isValidPhoneNumber(String i_PhoneNumber) {
        if (TextUtils.isEmpty(i_PhoneNumber)) {
            m_FieldPhoneNumber.setError("Invalid phone number.");
            return false;
        }

        return true;
    }
    private void signUpWithPhoneNumber()
    {
        String phoneNum = m_FieldPhoneNumber.getText().toString();
        if(isValidPhoneNumber(phoneNum)) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNum,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,     // Unit of timeout
                    SignUpWithPhoneNumActivity.this,           // Activity (for callback binding)
                    m_Callbacks);         // OnVerificationStateChangedCallbacks

            //m_IsVerificationInProgress = true;
            // startPhoneNumberVerification(phoneNum);
        }
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = m_Auth.getCurrentUser();

        // [START_EXCLUDE]

        //startPhoneNumberVerification(m_FieldPhoneNumber.getText().toString());

        // [END_EXCLUDE]
    }
    // [END on_start_check_user]

//    @Override
//    protected void onSaveInstanceState(Bundle i_OutState) {
//        super.onSaveInstanceState(i_OutState);
//        i_OutState.putBoolean(KEY_VERIFY_IN_PROGRESS, m_IsVerificationInProgress);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle i_SavedInstanceState) {
//        super.onRestoreInstanceState(i_SavedInstanceState);
//        m_IsVerificationInProgress = i_SavedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
//    }
//

    //    private void verifyPhoneNumber()
//    {
//        m_Callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential credential) {
//                // This callback will be invoked in two situations:
//                // 1 - Instant verification. In some cases the phone number can be instantly
//                //     verified without needing to send or enter a verification code.
//                // 2 - Auto-retrieval. On some devices Google Play services can automatically
//                //     detect the incoming verification SMS and perform verification without
//                //     user action.
//                Log.d(TAG, "onVerificationCompleted:" + credential);
//
//                //signInWithPhoneAuthCredential(credential);
//            }
//
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                // This callback is invoked in an invalid request for verification is made,
//                // for instance if the the phone number format is not valid.
//                Log.w(TAG, "onVerificationFailed", e);
//
//                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                    // Invalid request
//                    // ...
//                    m_FieldPhoneNumber.setError("Invalid phone number.");
//                } else if (e instanceof FirebaseTooManyRequestsException) {
//                    // The SMS quota for the project has been exceeded
//                    // ...
//                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
//                            Snackbar.LENGTH_SHORT).show();
//                }
//
//                // Show a message and update the UI
//                // ...
//                Snackbar.make(findViewById(android.R.id.content), e.getLocalizedMessage(),
//                        Snackbar.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCodeSent(String verificationId,
//                                   PhoneAuthProvider.ForceResendingToken token) {
//                // The SMS verification code has been sent to the provided phone number, we
//                // now need to ask the user to enter the code and then construct a credential
//                // by combining the code with a verification ID.
//                Log.d(TAG, "onCodeSent:" + verificationId);
//
//                // Save verification ID and resending token so we can use them later
//                m_VerificationId = verificationId;
//                m_ResendToken = token;
//
//                // ...
//            }
//        };
//    }
//
//    private void verifyPhoneNumberWithCode(String verificationId, String code) {
//        // [START verify_with_code]
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
//        // [END verify_with_code]
//        signInWithPhoneAuthCredential(credential);
//    }

//    private void startPhoneNumberVerification(String i_PhoneNumber) {
//        // [START start_phone_auth]
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                i_PhoneNumber,        // Phone number to verify
//                60,                 // Timeout duration
//                TimeUnit.SECONDS,     // Unit of timeout
//                SignUpWithPhoneNumActivity.this,           // Activity (for callback binding)
//                m_Callbacks);         // OnVerificationStateChangedCallbacks
//        // [END start_phone_auth]
//
//        m_IsVerificationInProgress = true;
//
//
//        m_Callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential credential) {
//                // This callback will be invoked in two situations:
//                // 1 - Instant verification. In some cases the phone number can be instantly
//                //     verified without needing to send or enter a verification code.
//                // 2 - Auto-retrieval. On some devices Google Play services can automatically
//                //     detect the incoming verification SMS and perform verification without
//                //     user action.
//                Log.d(TAG, "onVerificationCompleted:" + credential);
//
//                //signInWithPhoneAuthCredential(credential);
//            }
//
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                // This callback is invoked in an invalid request for verification is made,
//                // for instance if the the phone number format is not valid.
//                Log.w(TAG, "onVerificationFailed", e);
//
//                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                    // Invalid request
//                    // ...
//                    m_FieldPhoneNumber.setError("Invalid phone number.");
//                } else if (e instanceof FirebaseTooManyRequestsException) {
//                    // The SMS quota for the project has been exceeded
//                    // ...
//                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
//                            Snackbar.LENGTH_SHORT).show();
//                }
//
//                // Show a message and update the UI
//                // ...
//                Snackbar.make(findViewById(android.R.id.content), e.getLocalizedMessage(),
//                        Snackbar.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCodeSent(String verificationId,
//                                   PhoneAuthProvider.ForceResendingToken token) {
//                // The SMS verification code has been sent to the provided phone number, we
//                // now need to ask the user to enter the code and then construct a credential
//                // by combining the code with a verification ID.
//                Log.d(TAG, "onCodeSent:" + verificationId);
//
//                // Save verification ID and resending token so we can use them later
//                m_VerificationId = verificationId;
//                m_ResendToken = token;
//
//                // ...
//            }
//        };
//        //verifyPhoneNumber();
//    }

}
