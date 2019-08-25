package com.studymobile.advisos.Activities;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.studymobile.advisos.R;

import java.util.Objects;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;

public class ActivitySocialLogin extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "SocialAuth";
    private static final String EXTRA_FIRST_NAME_STR = "firstName";
    private static final String EXTRA_LAST_NAME_STR = "lastName";
    private static final String EXTRA_PHONE_STR = "phone";
    private static final String EXTRA_EMAIL_STR = "email";
    private static  final int RC_SIGN_IN = 9001;

    private Button m_BtnGoogleLogin;
    private Button m_BtnFacebookLogin;
    private ProgressDialog m_LoadingBar;

    private FirebaseAuth m_Auth;
    private FirebaseUser m_CurrentUser;

    private GoogleSignInClient m_GoogleSignInClient;
    private CallbackManager m_FacebookCallbackManager;
    private LoginButton m_HiddenFacebookBtn;

    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_social_login);
        setActivityContent();
        setFirebaseData();
        setGoogleData();
        setFacebookData();
    }

    @Override
    public void onClick(View i_View) {
        int id = i_View.getId();

        if (id == m_BtnGoogleLogin.getId())
        {
            signInWithGoogle();
        }
        else if (id == m_BtnFacebookLogin.getId())
        {
            m_HiddenFacebookBtn.performClick();
        }
    }

    //GOOGLE SIGN IN:
    private void setGoogleData()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        m_GoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInWithGoogle()
    {
        showLoadingBar("Google authentication", "Wait please...");
        Intent signInIntent = m_GoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void showLoadingBar(String i_Title, String i_Message)
    {
        m_LoadingBar = new ProgressDialog(this);
        m_LoadingBar.setTitle(i_Title);
        m_LoadingBar.setMessage(i_Message);
        m_LoadingBar.setCanceledOnTouchOutside(true);
        m_LoadingBar.show();
    }

    @Override
    public void onActivityResult(int i_RequestCode, int i_ResultCode, Intent i_Intent)
    {
        super.onActivityResult(i_RequestCode, i_ResultCode, i_Intent);

        if (i_RequestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = com.google.android.gms.auth.
                    api.signin.GoogleSignIn.getSignedInAccountFromIntent(i_Intent);
            try
            {
                if(task.isSuccessful())
                {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    makeFirebaseAuthWithGoogle(Objects.requireNonNull(account));
                }

                m_LoadingBar.dismiss();
            }
            catch (ApiException e)
            {
                // Google Sign In failed, update UI appropriately
                String msg = Objects.requireNonNull(task.getException()).getLocalizedMessage();
                Snackbar.make(findViewById(android.R.id.content),
                        "Google authentication failed:\n" + msg, Snackbar.LENGTH_LONG).show();
                m_LoadingBar.dismiss();
            }
        }
        else {
            //Try to login with facebook
            m_FacebookCallbackManager.onActivityResult(i_RequestCode, i_ResultCode, i_Intent);
        }
    }

    private void makeFirebaseAuthWithGoogle(GoogleSignInAccount i_Account)
    {
        Log.d(TAG, "firebaseAuthWithGoogle:" + i_Account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(i_Account.getIdToken(), null);
        m_Auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> i_Task)
                    {
                        if (i_Task.isSuccessful())
                        {
                            Log.d(TAG, "googleCredential:success");
                            startUserDetailsActivity();
                        } else {
                            Log.e(TAG, "googleCredential:failure", i_Task.getException());
                            String msg = Objects.requireNonNull(i_Task.getException()).getLocalizedMessage();
                            Snackbar.make(findViewById(android.R.id.content),
                                    "Authentication with Google failed:\n" + msg, Snackbar.LENGTH_SHORT).show();
                        }

                        m_LoadingBar.dismiss();
                    }
                });
    }

    //FACEBOOK LOGIN:
    private void setFacebookData()
    {
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        m_HiddenFacebookBtn = findViewById(R.id.hidden_facebook_btn);
        m_HiddenFacebookBtn.setPermissions("email", "public_profile");
        m_HiddenFacebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View i_View) {
                signInWithFacebook();
            }
        });
    }

    private void signInWithFacebook()
    {
        showLoadingBar("Facebook authentication", "Wait please...");
        m_FacebookCallbackManager = CallbackManager.Factory.create();
        m_HiddenFacebookBtn.registerCallback(m_FacebookCallbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult i_LoginResult)
            {
                Log.d(TAG, "facebook:onSuccess:" + i_LoginResult);
                handleFacebookAccessToken(i_LoginResult.getAccessToken());
            }
            @Override
            public void onCancel()
            {
                Log.d(TAG, "facebook:onCancel");
                m_LoadingBar.dismiss();
            }

            @Override
            public void onError(FacebookException e)
            {
                Log.d(TAG, "facebook:onError", e);
                String msg = Objects.requireNonNull(e.getLocalizedMessage());
                Snackbar.make(findViewById(android.R.id.content),
                        "facebook auth error:\n" + msg, Snackbar.LENGTH_SHORT).show();
                m_LoadingBar.dismiss();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken i_Token)
    {
        AuthCredential credential = FacebookAuthProvider.getCredential(i_Token.getToken());
        m_Auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> i_Task) {
                        if (i_Task.isSuccessful())
                        {
                            Log.d(TAG, "facebookCredential:success");
                            startUserDetailsActivity();

                        } else {
                            Log.e(TAG, "facebookCredential:failure", i_Task.getException());
                            String msg = Objects.requireNonNull(i_Task.getException()).getLocalizedMessage();
                            Snackbar.make(findViewById(android.R.id.content),
                                    "Authentication with Facebook failed:\n" + msg, Snackbar.LENGTH_SHORT).show();
                        }

                        m_LoadingBar.dismiss();
                    }
                });
    }

    private void setActivityContent()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        m_BtnGoogleLogin = findViewById(R.id.btn_google_login);
        m_BtnGoogleLogin.setOnClickListener(ActivitySocialLogin.this);
        m_BtnFacebookLogin = findViewById(R.id.btn_facebook_login);
        m_BtnFacebookLogin.setOnClickListener(ActivitySocialLogin.this);
    }

    private void setFirebaseData()
    {
        m_Auth = FirebaseAuth.getInstance();
        m_CurrentUser = m_Auth.getCurrentUser();
    }

    private void startUserDetailsActivity()
    {
        Intent IntentUserDetails = new Intent
                (ActivitySocialLogin.this, ActivityUserDetails.class);
        String email = m_CurrentUser.getEmail();
        String phoneNumber = m_CurrentUser.getPhoneNumber();
        String fullName = m_CurrentUser.getDisplayName();
        String firstName = "";
        String lastName = "";
        int indexOfSpace;

        if(fullName != null)
        {
            indexOfSpace = fullName.indexOf(" ");
            if(indexOfSpace != -1)
            {
                firstName = fullName.substring(0, indexOfSpace);
                lastName = fullName.substring(fullName.lastIndexOf(" ") + 1);
            }

            IntentUserDetails.putExtra(EXTRA_FIRST_NAME_STR, firstName);
            IntentUserDetails.putExtra(EXTRA_LAST_NAME_STR, lastName);
        }

        IntentUserDetails.putExtra(EXTRA_EMAIL_STR, email);
        IntentUserDetails.putExtra(EXTRA_PHONE_STR, phoneNumber);
        startActivity(IntentUserDetails);
    }
}
