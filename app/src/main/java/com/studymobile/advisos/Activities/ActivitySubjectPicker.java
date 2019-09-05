package com.studymobile.advisos.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.text.Editable;
import android.text.TextWatcher;
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
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.R;

import java.util.ArrayList;
import java.util.List;

public class ActivitySubjectPicker extends AppCompatActivity implements
        View.OnClickListener, TextWatcher, MaterialSearchBar.OnSearchActionListener
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

    private Dialog m_DialogCreateSubj;
    private Uri m_DialogImgURI;
    private CircleImageView m_DialogImgView;
    private EditText m_FieldSubjName;
    private EditText m_FieldSubjDescription;

    private Dialog m_DialogSubjList;
    private ImageButton m_FabCreateSubj;
    private ImageButton m_FabNext;
    private List<String> m_SuggestionsList;
    private MaterialSearchBar m_SearchBar;
    private RecyclerView m_RecyclerView;


    private boolean m_IsImgPicked = false;

    @Override
    protected void onCreate(Bundle i_SavedInstanceState) {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.dialog_subjects_list);
        setFirebaseData();
        setDialogSubjectList();
        setDialogCreateSubj();
    }

    @Override
    public void onClick(View i_View)
    {
        int id = i_View.getId();

     if(id == m_FabCreateSubj.getId())
        {
            showDialogCreateSubj();
        }
    }

    private void showDialogSubjList()
    {
//        buildSubjectsOptions();
//        populateSubjectsView();

        m_DialogSubjList.findViewById(R.id.fab_create_a_subject_of_dialog_subjects_list)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View i_View) {
                        showDialogCreateSubj();
                    }
                });

        m_DialogSubjList.findViewById(R.id.fab_next_of_dialog_subjects_list)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View i_View) {
                        m_DialogSubjList.dismiss();
                    }
                });

        m_DialogCreateSubj.show();
    }

    private void showDialogCreateSubj()
    {
        m_DialogImgView.setImageURI(m_DialogImgURI);

        m_DialogCreateSubj.findViewById(R.id.btn_add_a_photo_of_dialog_create_a_subj)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addProfileImage();
                    }
                });
        m_DialogCreateSubj.findViewById(R.id.btn_create_of_dialog_create_a_subj)
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

                            m_DialogCreateSubj.dismiss();
                        }
                    }
                });
        m_DialogCreateSubj.findViewById(R.id.btn_cancel_of_dialog_create_a_subj)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDefaultDialogCreateSubj();
                        m_DialogCreateSubj.dismiss();
                    }
                });

        m_DialogCreateSubj.show();
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
                                    pushNewSubjToDatabase();
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

    private void pushNewSubjToDatabase()
    {
        final Subject subject = new Subject();
        DatabaseReference databaseRef = m_Database.getReference("Subjects");
        String subjName = m_FieldSubjName.getText().toString();

        if(!isSubjectAlreadyExist(subjName)) {
            subject.setSubjectName(subjName);
            subject.setSubjectDescription(m_FieldSubjDescription.getText().toString());
            subject.setImgLink(m_DialogImgURI.toString());
//            databaseRef.child(subjName).setValue(subject)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> i_Task) {
//                            if (i_Task.isSuccessful()) {
//                                Toast.makeText(ActivitySubjectPicker.this,
//                                        "The subject is created.", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(ActivitySubjectPicker.this,
//                                        "Failure! Something was going wrong.", Toast.LENGTH_SHORT).show();
//                            }
//
//                            setDefaultPopupDialog();
//                        }
//                    });
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

                            setDefaultDialogCreateSubj();
                        }
                    });
        }

    }

    private void setDefaultDialogCreateSubj()
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

    private void setDialogSubjectList()
    {
        m_DialogSubjList = new Dialog(ActivitySubjectPicker.this);
        m_DialogSubjList.setContentView(R.layout.dialog_create_a_subject);
        m_DialogSubjList.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        m_SuggestionsList = new ArrayList<>();
        m_SearchBar = findViewById(R.id.search_bar);

        m_SearchBar.setCardViewElevation(10);
        m_SearchBar.setMaxSuggestionCount(5);
        m_SearchBar.addTextChangeListener(this);
        m_SearchBar.setOnSearchActionListener(this);

        m_RecyclerView = findViewById(R.id.recycler_view_of_dialog_subjects_list);
        m_RecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        m_RecyclerView.setLayoutManager(layoutManager);
    }

    private void setDialogCreateSubj()
    {
        m_DialogCreateSubj = new Dialog(ActivitySubjectPicker.this);
        m_DialogCreateSubj.setContentView(R.layout.dialog_create_a_subject);
        m_DialogCreateSubj.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        m_DialogImgView = m_DialogCreateSubj.findViewById(R.id.img_of_dialog_create_a_subj);
        m_DialogImgURI = Uri.parse(RES + getApplicationContext()
                .getPackageName() + DRAWABLE_DEFAULT);
        m_FieldSubjName = m_DialogCreateSubj.findViewById(R.id.field_subject_name_of_dialog_create_a_subj);
        m_FieldSubjDescription = m_DialogCreateSubj.findViewById(R.id.field_subject_description_of_dialog_create_a_subj);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}
