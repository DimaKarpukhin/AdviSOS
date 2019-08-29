package com.studymobile.advisos.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.studymobile.advisos.R;
import com.studymobile.advisos.Services.InputValidation;

import java.util.Objects;

public class ActivityPasswordLogin extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "PasswordAuth";
    private static final String EXTRA_EMAIL_STR = "email";
    private static final String AUTH_CONTEXT = "auth_context";
    private static final String PASSWORD_AUTH = "password";

    private ImageButton m_BtnNext;
    private TextView m_LinkSignUp;
    private TextView m_LinkResetPassword;
    private EditText m_FieldEmail;
    private EditText m_FieldPassword;

    private ProgressDialog m_LoadingBar;
    private String m_Email;
    private String m_Password;

    private FirebaseAuth m_Auth;

    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_password_login);
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

        if(id == m_BtnNext.getId())
        {
            if(isValidEmail() && isValidPassword())
            {
                showLoadingBar("User authentication","Wait please...");
                login();
            }
        }
        else if (id == m_LinkSignUp.getId())
        {
            startPasswordSignUpActivity();
        }
        else if (id == m_LinkResetPassword.getId())
        {
            startResetPasswordActivity();
        }
    }

    private boolean isValidEmail()
    {
        m_Email = m_FieldEmail.getText().toString();

        if (m_Email.isEmpty())
        {
            m_FieldEmail.setError("The field is required");
            return false;
        }
        else if(!InputValidation.IsValidEmail(m_Email))
        {
            m_FieldEmail.setError("Invalid email address");
            return false;
        }

        return true;
    }

    private boolean isValidPassword()
    {
        m_Password = m_FieldPassword.getText().toString();

        if (m_Password.isEmpty())
        {
            m_FieldPassword.setError("This field is required");
            return false;
        }
        else if(!InputValidation.IsValidPassword(m_Password))
        {
            m_FieldPassword.setError("Password to weak");
            Toast.makeText(ActivityPasswordLogin.this,
                    "Password must contain at list 6 characters without white spaces",
                    Toast.LENGTH_SHORT).show();
//            Snackbar.make(findViewById(android.R.id.content),
//                    "Password must contain at list 6 characters without white spaces",
//                    Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showLoadingBar(String i_Title, String i_Message)
    {
        m_LoadingBar = new ProgressDialog(this);
        m_LoadingBar.setTitle(i_Title);
        m_LoadingBar.setMessage(i_Message);
        m_LoadingBar.setCanceledOnTouchOutside(true);
        m_LoadingBar.show();
    }

    private void login()
    {
        m_Auth.signInWithEmailAndPassword(m_Email, m_Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> i_Task)
                    {
                        if (i_Task.isSuccessful())
                        {

                            Log.d(TAG, "sigInWithEmail:success");
                            startUserDetailsActivity();
                        }  else {
                            Log.e(TAG, "sigInWithEmail:failure", i_Task.getException());
                            String msg = Objects.requireNonNull(i_Task.getException()).getLocalizedMessage();
                            Toast.makeText(ActivityPasswordLogin.this,
                                    "Authentication failed:\n" + msg, Toast.LENGTH_SHORT).show();
//                            Snackbar.make(findViewById(android.R.id.content), "Authentication failed:\n" + msg,
//                                    Snackbar.LENGTH_LONG).show();
                        }

                        m_LoadingBar.dismiss();
                    }
                });
    }

    private void setActivityContent()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        m_FieldEmail = findViewById(R.id.field_email_of_password_login);
        m_FieldPassword = findViewById(R.id.field_password_of_password_login);
        m_BtnNext = findViewById(R.id.btn_next_of_password_login);
        m_BtnNext.setOnClickListener(ActivityPasswordLogin.this);
        m_LinkSignUp = findViewById(R.id.link_create_new_account);
        m_LinkSignUp.setOnClickListener(ActivityPasswordLogin.this);
        m_LinkResetPassword = findViewById(R.id.link_forgot_password);
        m_LinkResetPassword.setOnClickListener(ActivityPasswordLogin.this);

        getIntentExtras();
    }

    private void getIntentExtras()
    {
        Intent intent = getIntent();
        if(intent != null)
        {
            m_Email = intent.getStringExtra(EXTRA_EMAIL_STR);
            m_FieldEmail.setText(m_Email);
        }
    }


    private void setFirebaseData()
    {
        m_Auth = FirebaseAuth.getInstance();
    }

    private void startUserDetailsActivity()
    {
        Intent IntentUserDetails = new Intent
                (ActivityPasswordLogin.this, ActivityUserDetails.class);
        IntentUserDetails.putExtra(EXTRA_EMAIL_STR, m_Email);
        IntentUserDetails.putExtra(AUTH_CONTEXT, PASSWORD_AUTH);

        startActivity(IntentUserDetails);
    }

    private void startPasswordSignUpActivity()
    {
        Intent IntentPasswordSignUp = new Intent
                (ActivityPasswordLogin.this, ActivityPasswordSignUp.class);
        startActivity(IntentPasswordSignUp);
    }

    private void startResetPasswordActivity()
    {
        Intent IntentResetPassword = new Intent
                (ActivityPasswordLogin.this, ActivityResetPassword.class);
        startActivity(IntentResetPassword);
    }
}
