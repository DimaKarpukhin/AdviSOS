package com.studymobile.advisos.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.R;

import java.util.Objects;

public class ActivitySubjectActionManager extends AppCompatActivity implements View.OnClickListener
{

    private static final String SUBJECT_NAME = "subject_name";

    private EditText m_FieldTopic;
    private Button m_BtnAskAdvice;
    private ImageButton m_BtnAddImage;
    private ImageButton m_BtnRestore;
    private ImageButton m_BtnBack;
    private ImageView m_ImgSubject;
    private TextView m_TxtSubjectName;
    private TextView m_TxtSubjectDescription;
    private Subject m_CurrentSubject;
    private String m_SubjectName;

    private FirebaseDatabase m_Database;
    private DatabaseReference m_SubjectsRef;
    private FirebaseAuth m_Auth;

    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_subject_action_manager);
        setFirebaseData();
        setActivityContent();

        if(getIntent() != null)
        {
            m_SubjectName = getIntent().getStringExtra(SUBJECT_NAME);
        }
        if(!m_SubjectName.isEmpty())
        {
            loadSubjectDetails(m_SubjectName);
        }else {
            //Toast.makeText(this, "ERROR: There is no instance in database" ,Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(android.R.id.content),
                    "ERROR: There is no instance in database", Snackbar.LENGTH_SHORT).show();
        }
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

        if (id == m_BtnAddImage.getId())
        {

        }
        else if (id == m_BtnRestore.getId())
        {

        }
        else if (id == m_BtnBack.getId())
        {
            startHomeScreenActivity();
        }
        else if (id == m_BtnAskAdvice.getId())
        {
            checkIfTopicExists();
        }
    }

    private void checkIfTopicExists()
    {
        final Dialog confirmDialog = new Dialog(ActivitySubjectActionManager.this);
        confirmDialog.setContentView(R.layout.dialog_confirm);

        String title = "Similar topics were discussed earlier";
        String rightBtnTxt = "Start new";
        String leftBtnTxt = "View similar";

        ImageButton closeBtn = confirmDialog.findViewById(R.id.btn_close_of_dialog_confirm);
        TextView fieldTitle = confirmDialog.findViewById(R.id.txt_title_of_dialog_confirm);
        TextView rightBtn = confirmDialog.findViewById(R.id.btn_right_of_dialog_confirm);
        TextView leftBtn = confirmDialog.findViewById(R.id.btn_left_of_dialog_confirm);

        closeBtn.setVisibility(View.VISIBLE);
        fieldTitle.setText(title);
        rightBtn.setText(rightBtnTxt);
        leftBtn.setText(leftBtnTxt);

        rightBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startCreateChatRoomActivity();
                    }
                });
        leftBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });

        confirmDialog.show();
    }

    private void setFirebaseData()
    {
        m_Database = FirebaseDatabase.getInstance();
        m_SubjectsRef = m_Database.getReference("Subjects");
        m_Auth = FirebaseAuth.getInstance();
    }

    private void setActivityContent()
    {
        m_FieldTopic = findViewById(R.id.field_topic_action_manager);
        m_ImgSubject = findViewById(R.id.img_subject_of_subject_action_manager);
        m_TxtSubjectName = findViewById(R.id.txt_subject_name_of_subject_action_manager);
        m_TxtSubjectDescription = findViewById(R.id.txt_subj_description_of_subject_action_manager);

        m_BtnBack = findViewById(R.id.btn_back_of_subject_action_manager);
        m_BtnRestore = findViewById(R.id.btn_restore_of_subject_action_manager);
        m_BtnAddImage = findViewById(R.id.btn_add_a_photo_of_subject_action_manager);
        m_BtnAskAdvice = findViewById(R.id.btn_ask_for_advice);

        m_BtnBack.setOnClickListener(this);
        m_BtnRestore.setOnClickListener(this);
        m_BtnAddImage.setOnClickListener(this);
        m_BtnAskAdvice.setOnClickListener(this);

    }

    private void loadSubjectDetails(String i_SubjectName)
    {
        m_Database.getReference("Subjects").child(i_SubjectName)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot) {
                        m_CurrentSubject = i_DataSnapshot.getValue(Subject.class);
                        Picasso.get().load(m_CurrentSubject.getImgLink()).into(m_ImgSubject);
                        m_TxtSubjectName.setText(m_CurrentSubject.getSubjectName());
                        m_TxtSubjectDescription.setText(m_CurrentSubject.getSubjectDescription());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void startHomeScreenActivity()
    {
        Intent IntentHomeScreen = new Intent
                (ActivitySubjectActionManager.this, ActivityHomeScreen.class);
        startActivity(IntentHomeScreen);
    }

    private void startCreateChatRoomActivity()
    {
        Intent IntentCreateChatRoom = new Intent
                (ActivitySubjectActionManager.this, ActivityCreateChatRoom.class);
        IntentCreateChatRoom.putExtra(m_SubjectName , SUBJECT_NAME);
        startActivity(IntentCreateChatRoom);
    }

}
