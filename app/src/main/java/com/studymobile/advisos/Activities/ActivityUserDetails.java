package com.studymobile.advisos.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
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

import static java.lang.System.exit;

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
    private static final String DEFAULT = "Default";
    private static final String RES = "android.resource://";
    private static final String DRAWABLE_DEFAULT = "/drawable/img_profile_picture";
    private static final int IMG_REQ = 1;
    private static final int REQ_CODE = 2;

    private Uri m_ProfileImgURI;
    private String m_ProfileImgLink;
    private CircleImageView m_ProfileImgView;
    private FloatingActionButton m_FabNext;
    private EditText m_FieldPhoneNumber;
    private EditText m_FieldEmail;
    private EditText m_FieldFirstName;
    private EditText m_FieldFamilyName;
    private CountryCodePicker m_CountryCodePicker;

    private Dialog m_PopupDialog;
    private Uri m_DialogImgURI;
    private CircleImageView m_DialogImgView;

    private String m_InternationalPhoneNumber = null;
    private String m_Email = null;
    private String m_FirstName = null;
    private String m_FamilyName = null;
    private String m_AuthContext = null;

    private FirebaseAuth m_Auth;
    private FirebaseUser m_CurrentUser;
    private FirebaseDatabase m_Database;
    private FirebaseStorage m_Storage;

    private boolean m_IsImgStored = false;
    private boolean m_IsImgPicked = false;
    private boolean m_IsSocialAvatarPicked = false;

    private String m_DeviceToken;


    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_user_details);
        setFirebaseData();
        setActivityContent();
        getDeviceToken();
        getIntentExtras();
        getUserPersonalDetailsFromDB();
        setPopupDialog();

    }

    private void getDeviceToken()
    {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult i_InstanceIdResult) {
                m_DeviceToken = i_InstanceIdResult.getToken();
            }
        });
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
        if(id == m_FabNext.getId())
        {
            if(isUserDetailsCompleted())
            {
                if (m_IsImgPicked)
                {
                    uploadImgToStorage(m_FamilyName + m_FirstName);
                }else if (!m_IsImgStored &&
                        (m_AuthContext.equals(PHONE_AUTH) || m_AuthContext.equals(PASSWORD_AUTH))) {
                    uploadImgToStorage(DEFAULT);
                }

                populateDatabase();
                startExpertSettingsActivity();
            }
        }
        else if (id == m_ProfileImgView.getId())
        {
            showPopupDialog();
        }

    }

    private void populateDatabase()
    {
        DatabaseReference databaseRef = m_Database.getReference("Users");

        if(m_CurrentUser != null)
        {
            databaseRef.child(m_CurrentUser.getUid())
                    .child("authContext").setValue(m_AuthContext);
            databaseRef.child(m_CurrentUser.getUid())
                    .child("firstName").setValue(m_FirstName);
            databaseRef.child(m_CurrentUser.getUid())
                    .child("familyName").setValue(m_FamilyName);
            databaseRef.child(m_CurrentUser.getUid())
                    .child("email").setValue(m_Email);
            databaseRef.child(m_CurrentUser.getUid())
                    .child("phone").setValue(m_InternationalPhoneNumber);
            databaseRef.child(m_CurrentUser.getUid())
                    .child("imgLink").setValue(m_ProfileImgLink);
            databaseRef.child(m_CurrentUser.getUid())
                    .child("deviceToken").setValue(m_DeviceToken);
        }
    }

    private void uploadImgToStorage(String i_Image)
    {
        StorageReference imagePath = m_Storage.getReference().child("Images/Profile pictures");
        if(m_ProfileImgURI != null)
        {
            final StorageReference imageRef = imagePath.child(i_Image);
            imageRef.putFile(m_ProfileImgURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    m_ProfileImgLink = String.valueOf(uri);
                                    populateDatabase();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ActivityUserDetails.this,
                            "ERROR$:\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setPopupDialog()
    {
        m_PopupDialog = new Dialog(ActivityUserDetails.this);
        m_PopupDialog.setContentView(R.layout.dialog_profile_picture);
        m_PopupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        m_DialogImgView = m_PopupDialog.findViewById(R.id.img_of_dialog_profile_picture);
        m_DialogImgURI = m_ProfileImgURI;
    }

    private void showPopupDialog()
    {
        if(m_IsSocialAvatarPicked){
            Picasso.get().load(m_ProfileImgLink).into(m_DialogImgView);
        }else {
            m_DialogImgView.setImageURI(m_ProfileImgURI);
        }
        m_PopupDialog.findViewById(R.id.btn_add_a_photo_of_dialog_profile_picture)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addProfileImage();
                    }
                });
        m_PopupDialog.findViewById(R.id.btn_ok_of_dialog_profile_picture)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        m_ProfileImgURI = m_DialogImgURI;
                        if(m_IsImgPicked)
                        {
                            m_IsSocialAvatarPicked = false;
                            m_ProfileImgView.setImageURI(m_ProfileImgURI);
                        }
                        else {
                            if (m_IsSocialAvatarPicked) {
                                Picasso.get().load(m_ProfileImgLink).into(m_ProfileImgView);
                            } else {
                                m_ProfileImgView.setImageURI(m_ProfileImgURI);
                            }
                        }
                        m_PopupDialog.dismiss();
                    }
                });
        m_PopupDialog.findViewById(R.id.btn_remove_of_dialog_profile_picture)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        confirmRemoving();
                    }
                });
        m_PopupDialog.findViewById(R.id.btn_close_of_dialog_profile_picture)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(m_IsImgPicked) {
                            if (m_IsSocialAvatarPicked) {
                                Picasso.get().load(m_ProfileImgLink).into(m_ProfileImgView);
                            } else {
                                m_ProfileImgView.setImageURI(m_ProfileImgURI);
                            }
                        }
                        m_PopupDialog.dismiss();
                    }
                });

        m_PopupDialog.show();
    }

    private void confirmRemoving()
    {
        final Dialog removeDialog = new Dialog(ActivityUserDetails.this);
        removeDialog.setContentView(R.layout.dialog_confirm_remove);

        removeDialog.findViewById(R.id.btn_remove_of_dialog_confirm_remove)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        m_DialogImgURI = Uri.parse(RES + getApplicationContext()
                                .getPackageName() + DRAWABLE_DEFAULT);
                        m_DialogImgView.setImageURI(m_DialogImgURI);
                        m_ProfileImgURI = m_DialogImgURI;
                        m_ProfileImgView.setImageURI(m_ProfileImgURI);
                        removeDialog.dismiss();
                        m_IsImgPicked = false;
                    }
                });
        removeDialog.findViewById(R.id.btn_cancel_of_dialog_confirm_remove)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeDialog.dismiss();
                    }
                });

        removeDialog.show();
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
            m_DialogImgURI = data.getData();
            m_DialogImgView.setImageURI(m_DialogImgURI);
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

    private void getUserPersonalDetailsFromDB()
    {
        DatabaseReference userId = m_Database.getReference("Users");
        userId.child(m_CurrentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot) {
                        if (i_DataSnapshot.exists())
                        {
                            User currentUser = i_DataSnapshot.getValue(User.class);
                            m_ProfileImgLink = currentUser.getImgLink();
                            m_IsImgStored = true;
                            m_AuthContext = currentUser.getAuthContext();
                            m_FirstName = currentUser.getFirstName();
                            m_FamilyName = currentUser.getFamilyName();
                            m_Email = currentUser.getEmail();
                            m_InternationalPhoneNumber = currentUser.getPhone();
                            updateUI();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError i_DataSnapshot) {}
                });
    }


    private void updateUI()
    {
        Picasso.get().load(m_ProfileImgLink).into(m_ProfileImgView);
        m_FieldFirstName.setText(m_FirstName);
        m_FieldFamilyName.setText(m_FamilyName);
        m_FieldEmail.setText(m_Email);
        m_FieldPhoneNumber.setText(m_InternationalPhoneNumber);
    }

    private void setActivityContent()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        m_ProfileImgView = findViewById(R.id.img_profile_photo_of_user_details);
        m_ProfileImgView.setOnClickListener(ActivityUserDetails.this);
        m_FabNext = findViewById(R.id.fab_next_of_user_details);
        m_FabNext.setOnClickListener(ActivityUserDetails.this);
        m_FieldFirstName = findViewById(R.id.field_first_name_of_user_details);
        m_FieldFamilyName = findViewById(R.id.field_last_name_of_user_details);
        m_FieldEmail = findViewById(R.id.field_email_of_user_details);
        m_FieldPhoneNumber = findViewById(R.id.field_phone_num_of_user_details);
        m_CountryCodePicker =  findViewById(R.id.picker_of_country_code_user_details);
        m_CountryCodePicker.registerCarrierNumberEditText(m_FieldPhoneNumber);

        m_ProfileImgURI = Uri.parse(RES + getApplicationContext().getPackageName()
                + DRAWABLE_DEFAULT);
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

            if(m_ProfileImgLink != null && !m_ProfileImgLink.isEmpty())
            {
                m_ProfileImgLink = m_ProfileImgLink + improveQuality;
                m_IsSocialAvatarPicked = true;
            }
            else{
                m_ProfileImgLink = RES + getApplicationContext().getPackageName()
                        + DRAWABLE_DEFAULT;
            }

            updateUI();
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

    private void startExpertSettingsActivity()
    {
        Intent IntentExpertConfig = new Intent
                (ActivityUserDetails.this, ActivityExpertSettings.class);
        startActivity(IntentExpertConfig);
    }
}
