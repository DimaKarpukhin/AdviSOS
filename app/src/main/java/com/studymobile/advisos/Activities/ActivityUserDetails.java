package com.studymobile.advisos.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;
import com.studymobile.advisos.Models.User;
import com.studymobile.advisos.R;
import com.studymobile.advisos.Services.InputValidation;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityUserDetails extends AppCompatActivity implements View.OnClickListener
{
    private static final String EXTRA_PHOTO_URI_STR = "profilePhoto";
    private static final String EXTRA_PHONE_STR = "phone";
    private static final String EXTRA_EMAIL_STR = "email";
    private static final String EXTRA_FIRST_NAME_STR = "firstName";
    private static final String EXTRA_LAST_NAME_STR = "lastName";
    private static final String AUTH_CONTEXT = "auth_context";
    private static final String PHONE_AUTH = "phone";
    private static final String PASSWORD_AUTH = "password";
    private static final String FACEBOOK_AUTH = "facebook";
    private static final String GOOGLE_AUTH = "google";
    private static final String DEFAULT = "default";
    private static final int IMG_REQ = 1;
    private static final int REQ_CODE = 2;

    private Uri m_ProfileImgURI;
    private String m_ProfileImgLink;
    private CircleImageView m_ProfileImgView;
    private ImageButton m_BtnNext;
    private EditText m_FieldPhoneNumber;
    private EditText m_FieldEmail;
    private EditText m_FieldFirstName;
    private EditText m_FieldFamilyName;
    private CountryCodePicker m_CountryCodePicker;

    private String m_InternationalPhoneNumber = null;
    private String m_Email = null;
    private String m_FirstName = null;
    private String m_FamilyName = null;
    private String m_AuthContext = null;

    private FirebaseAuth m_Auth;
    private FirebaseUser m_CurrentUser;
    private FirebaseDatabase m_Database;
    private FirebaseStorage m_Storage;

    private boolean m_IsImgPicked = false;

    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_user_details);
        setFirebaseData();
        setActivityContent();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
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
            if(isUserDetailsCompleted())
            {
                if (m_IsImgPicked)
                {
                    uploadImgToStorage(m_FamilyName + m_FirstName);
                }else if (m_AuthContext.equals(PHONE_AUTH)
                        || m_AuthContext.equals(PASSWORD_AUTH)) {
                    uploadImgToStorage(DEFAULT);
                }

                populateDatabase();
                startExpertConfigActivity();
            }
        }
        else if (id == m_ProfileImgView.getId())
        {
            addProfileImage();
        }
    }

    private void populateDatabase()
    {
        final User user = new User();
        DatabaseReference databaseRef = m_Database.getReference("Users");

        if(m_CurrentUser != null)
        {
            user.setAuthContext(m_AuthContext);
            user.setFirstName(m_FirstName);
            user.setFamilyName(m_FamilyName);
            user.setEmail(m_Email);
            user.setPhone(m_InternationalPhoneNumber);
            user.setImgLink(m_ProfileImgLink);
            databaseRef.child(m_CurrentUser.getUid()).setValue(user);
        }
    }

    private void uploadImgToStorage(String i_Image)
    {
        StorageReference imagePath = m_Storage.getReference().child("Images/Avatars");
        if(m_ProfileImgURI != null)
        {
            final StorageReference imageRef = imagePath.child(i_Image);
            imageRef.putFile(m_ProfileImgURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            m_ProfileImgLink = String.valueOf(uri);
                            populateDatabase();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ActivityUserDetails.this,
                            "ERROR:\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void addProfileImage()
    {
        if(Build.VERSION.SDK_INT >= 24)
        {
            requestPermission();
        }
        else
        {
            openGallery();
        }
    }

    private void requestPermission()
    {
        if(ContextCompat.checkSelfPermission(ActivityUserDetails.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    ActivityUserDetails.this, Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(ActivityUserDetails.this,
                        "Please accept for required permission ", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(ActivityUserDetails.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_CODE);
            }
        }
        else
        {
            openGallery();
        }
    }

    private void openGallery()
    {
        Intent IntentGallery =new Intent(Intent.ACTION_GET_CONTENT);
        IntentGallery.setType("image/*");
        startActivityForResult(IntentGallery,IMG_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == IMG_REQ && data != null)
        {
            m_ProfileImgURI = data.getData();
            Picasso.get().load(m_ProfileImgURI).rotate(90f).into(m_ProfileImgView);
            m_IsImgPicked = true;

        }
    }

    private boolean isUserDetailsCompleted()
    {
        String errorMsgEmptyField = "The field is required";
        String errorMsgInvalidInput = "Invalid input";
        m_FirstName = m_FieldFirstName.getText().toString();
        m_FamilyName = m_FieldFamilyName.getText().toString();
        m_Email = m_FieldEmail.getText().toString();
        m_InternationalPhoneNumber = m_CountryCodePicker.getFullNumberWithPlus();

        if(m_FirstName.isEmpty()) {
            m_FieldFirstName.setError(errorMsgEmptyField);
            return false;
        }
        else if(m_FamilyName.isEmpty()) {
            m_FieldFamilyName.setError(errorMsgEmptyField);
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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        m_ProfileImgView = findViewById(R.id.img_profile_photo_of_user_details);
        m_ProfileImgView.setOnClickListener(ActivityUserDetails.this);
        m_BtnNext = findViewById(R.id.btn_next_of_user_details);
        m_BtnNext.setOnClickListener(ActivityUserDetails.this);
        m_FieldFirstName = findViewById(R.id.field_first_name_of_user_details);
        m_FieldFamilyName = findViewById(R.id.field_last_name_of_user_details);
        m_FieldEmail = findViewById(R.id.field_email_of_user_details);
        m_FieldPhoneNumber = findViewById(R.id.field_phone_num_of_user_details);
        m_CountryCodePicker =  findViewById(R.id.picker_of_country_code_user_details);
        m_CountryCodePicker.registerCarrierNumberEditText(m_FieldPhoneNumber);

        getIntentExtras();
    }

    @SuppressLint("ResourceType")
    private void getIntentExtras()
    {
        Intent intent = getIntent();
        String improveQuality = "?height=500";
        if(intent != null)
        {
            m_AuthContext = intent.getStringExtra(AUTH_CONTEXT);
            m_FirstName = intent.getStringExtra(EXTRA_FIRST_NAME_STR);
            m_FamilyName = intent.getStringExtra(EXTRA_LAST_NAME_STR);
            m_Email = intent.getStringExtra(EXTRA_EMAIL_STR);
            m_InternationalPhoneNumber = intent.getStringExtra(EXTRA_PHONE_STR);
            m_ProfileImgLink = intent.getStringExtra(EXTRA_PHOTO_URI_STR);
            //m_ProfileImgURI = Uri.parse(getResources().getDrawable(R.id.img_profile_photo_of_user_details).toString());
            if(m_ProfileImgLink != null && !m_ProfileImgLink.isEmpty()) {
                m_ProfileImgLink = m_ProfileImgLink + improveQuality;
                Picasso.get().load(m_ProfileImgLink).into(m_ProfileImgView);
                m_ProfileImgURI = Uri.parse(m_ProfileImgLink);

            }
            m_ProfileImgURI = Uri.parse("android.resource://"
                    + getApplicationContext().getPackageName()
                    + "/drawable/img_add_a_photo_blue_light");
            m_FieldFirstName.setText(m_FirstName);
            m_FieldFamilyName.setText(m_FamilyName);
            m_FieldEmail.setText(m_Email);
            m_FieldPhoneNumber.setText(m_InternationalPhoneNumber);
        }
    }

    private void setFirebaseData()
    {
        m_Auth = FirebaseAuth.getInstance();
        m_CurrentUser = m_Auth.getCurrentUser();
        m_Database = FirebaseDatabase.getInstance();
        m_Storage = FirebaseStorage.getInstance();
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
