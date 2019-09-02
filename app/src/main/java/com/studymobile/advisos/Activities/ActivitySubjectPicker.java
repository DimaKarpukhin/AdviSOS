package com.studymobile.advisos.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.R;

public class ActivitySubjectPicker extends AppCompatActivity implements View.OnClickListener
{
    private static final String RES = "android.resource://";
    private static final String DRAWABLE_DEFAULT = "/drawable/img_advisos";
    private static final String ADVISOS = "Advisos";
    private static final int IMG_REQ = 1;
    private static final int REQ_CODE = 2;

    private FirebaseAuth m_Auth;
    private FirebaseUser m_CurrentUser;
    private FirebaseDatabase m_Database;
    private FirebaseStorage m_Storage;

    private ImageButton m_FabCreateSubj;

    private Dialog m_PopupDialog;
    private Uri m_DialogImgURI;
    private CircleImageView m_DialogImgView;
    private EditText m_FieldSubjName;
    private EditText m_FieldSubjDescription;

    private boolean m_IsImgPicked = false;

    @Override
    protected void onCreate(Bundle i_SavedInstanceState) {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_subject_picker);
        setFirebaseData();
        setActivityContent();
    }

    @Override
    public void onClick(View i_View)
    {
        int id = i_View.getId();

        if(id == m_FabCreateSubj.getId())
        {
            showPopupDialog();
        }
    }

    private void showPopupDialog()
    {
        m_DialogImgView.setImageURI(m_DialogImgURI);

        m_PopupDialog.findViewById(R.id.btn_add_a_photo_of_dialog_create_a_subj)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addProfileImage();
                    }
                });
        m_PopupDialog.findViewById(R.id.btn_create_of_dialog_create_a_subj)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(m_FieldSubjName.getText() == null
                                || m_FieldSubjName.getText().toString().isEmpty())
                        {
                            m_FieldSubjName.setError("The field is required");
                        }
                        else {
                            if(m_IsImgPicked){
                                uploadImgToStorage( m_FieldSubjName.getText().toString());
                            }else{
                                uploadImgToStorage(ADVISOS);
                            }

                            m_PopupDialog.dismiss();
                        }
                    }
                });
        m_PopupDialog.findViewById(R.id.btn_cancel_of_dialog_create_a_subj)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDefaultPopupDialog();
                        m_PopupDialog.dismiss();
                    }
                });

        m_PopupDialog.show();
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
        if(ContextCompat.checkSelfPermission(ActivitySubjectPicker.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    ActivitySubjectPicker.this, Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(ActivitySubjectPicker.this,
                        "Please accept for required permission ", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(ActivitySubjectPicker.this,
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
        Intent IntentGallery = new Intent(Intent.ACTION_GET_CONTENT);
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

    private void uploadImgToStorage(String i_Image)
    {
        StorageReference imagePath = m_Storage.getReference().child("Images/Subjects");
        if(m_DialogImgURI != null)
        {
            final StorageReference imageRef = imagePath.child(i_Image);
            imageRef.putFile(m_DialogImgURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    m_DialogImgURI = uri;
                                    populateDatabase();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ActivitySubjectPicker.this,
                            "ERROR:\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void populateDatabase()
    {
        final Subject subject = new Subject();
        DatabaseReference databaseRef = m_Database.getReference("Subjects");
        String subjName = m_FieldSubjName.getText().toString();

        if(!isSubjectAlreadyExist(subjName)) {
            subject.setName(subjName);
            subject.setDescription(m_FieldSubjDescription.getText().toString());
            subject.setImgLink(m_DialogImgURI.toString());
            databaseRef.child(subjName).setValue(subject)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> i_Task) {
                            if (i_Task.isSuccessful()) {
                                Toast.makeText(ActivitySubjectPicker.this,
                                        "The subject is created.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ActivitySubjectPicker.this,
                                        "Failure! Something was going wrong.", Toast.LENGTH_SHORT).show();
                            }

                            setDefaultPopupDialog();
                        }
                    });
            databaseRef.push().setValue(subject)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> i_Task) {
                            if (i_Task.isSuccessful()) {
                                Toast.makeText(ActivitySubjectPicker.this,
                                        "The subject is created.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ActivitySubjectPicker.this,
                                        "Failure! Something was going wrong.", Toast.LENGTH_SHORT).show();
                            }

                            //setDefaultPopupDialog();
                        }
                    });
        }

    }

    private void setDefaultPopupDialog()
    {
        m_DialogImgURI = Uri.parse(RES + getApplicationContext()
                .getPackageName() + DRAWABLE_DEFAULT);
        m_FieldSubjDescription.getText().clear();
        m_FieldSubjName.getText().clear();
    }

    private boolean isSubjectAlreadyExist(String i_SubjName)
    {
        boolean result = false;
        //TODO

        return result;
    }



    private void setFirebaseData()
    {
        m_Auth = FirebaseAuth.getInstance();
        m_CurrentUser = m_Auth.getCurrentUser();
        m_Database = FirebaseDatabase.getInstance();
        m_Storage = FirebaseStorage.getInstance();
    }

    private void setActivityContent()
    {
        m_FabCreateSubj = findViewById(R.id.fab_create_a_subject_of_subject_picker);
        m_FabCreateSubj.setOnClickListener(this);

        setPopupDialog();
    }

    private void setPopupDialog()
    {
        m_PopupDialog = new Dialog(ActivitySubjectPicker.this);
        m_PopupDialog.setContentView(R.layout.dialog_create_a_subject);
        m_PopupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        m_DialogImgView = m_PopupDialog.findViewById(R.id.img_of_dialog_create_a_subj);
        m_DialogImgURI = Uri.parse(RES + getApplicationContext()
                .getPackageName() + DRAWABLE_DEFAULT);
        m_FieldSubjName = m_PopupDialog.findViewById(R.id.field_subject_name_of_dialog_create_a_subj);
        m_FieldSubjDescription = m_PopupDialog.findViewById(R.id.field_subject_description_of_dialog_create_a_subj);
    }
}
