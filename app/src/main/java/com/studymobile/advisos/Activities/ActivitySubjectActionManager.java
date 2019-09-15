package com.studymobile.advisos.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.studymobile.advisos.Models.ActiveChatRoom;
import com.studymobile.advisos.Models.ChatRequest;
import com.studymobile.advisos.Models.ChatRoom;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.Models.User;
import com.studymobile.advisos.Models.UserLocation;
import com.studymobile.advisos.R;
import com.studymobile.advisos.Services.DatabaseServices;
import com.studymobile.advisos.notification.FCMNotification;

import java.util.Date;
import java.util.List;

public class ActivitySubjectActionManager extends AppCompatActivity implements View.OnClickListener
{

    private static final String SUBJECT_NAME = "subject_name";

    private EditText mFieldTopic;
    private TextView mBtnAskAdvice;
    private ImageButton mBtnAddImage;
    private ImageButton mBtnRestore;
    private ImageButton mBtnBack;
    private ImageView mImgSubject;
    private TextView mTxtSubjectName;
    private TextView mTxtSubjectDescription;
    private Subject mCurrentSubject;
    private String mSubjectName;
    private String mImgLinkForChatIntent;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseServices mDatabaseServices;
    private UserLocation mUserLocation;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mSubjectsRef;

    DatabaseReference mChatRoomsRef;
    String mChatRoomUId;
    List<String> mExpertsList;

    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_subject_action_manager);
        setFirebaseData();
        setActivityContent();
        getUserLocation();

        if(getIntent() != null)
        {
            mSubjectName = getIntent().getStringExtra(SUBJECT_NAME);
        }
        if(!mSubjectName.isEmpty())
        {
            mDatabaseServices = new DatabaseServices(mSubjectName, mUserLocation);
            mDatabaseServices.CollectExperts();
            loadSubjectDetails(mSubjectName);
        }else {
            //Toast.makeText(this, "ERROR: There is no instance in database" ,Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(android.R.id.content),
                    "ERROR: There is no instance in database", Snackbar.LENGTH_SHORT).show();
        }

    }

    private void getUserLocation()
    {
        mDatabase.getReference("Users").child(mCurrentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot) {
                        mUserLocation = i_DataSnapshot.getValue(UserLocation.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
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

        if (id == mBtnAddImage.getId())
        {

        }
        else if (id == mBtnRestore.getId())
        {

        }
        else if (id == mBtnBack.getId())
        {
            startHomeScreenActivity();
        }
        else if (id == mBtnAskAdvice.getId())
        {
            if(mFieldTopic.getText().toString().isEmpty())
            {
                mFieldTopic.setError("The field is required");
            }
            else {
                checkIfTopicExists();
            }

        }
    }

    private void checkIfTopicExists()
    {
        final Dialog confirmDialog = new Dialog(ActivitySubjectActionManager.this);
        confirmDialog.setContentView(R.layout.dialog_confirm);

        String title = "Similar topics were discussed earlier";
        String startNewBtnTxt = "Start new";
        String viewSimilarBtnTxt = "View similar";

        ImageButton closeBtn = confirmDialog.findViewById(R.id.btn_close_of_dialog_confirm);
        TextView fieldTitle = confirmDialog.findViewById(R.id.txt_title_of_dialog_confirm);
        TextView startNewBtn = confirmDialog.findViewById(R.id.btn_right_of_dialog_confirm);
        TextView viewSimilarBtn = confirmDialog.findViewById(R.id.btn_left_of_dialog_confirm);

        closeBtn.setVisibility(View.VISIBLE);
        fieldTitle.setText(title);
        startNewBtn.setText(startNewBtnTxt);
        viewSimilarBtn.setText(viewSimilarBtnTxt);

        startNewBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //startCreateChatRoomActivity();
                        createChatRoomActivity(mFieldTopic.getText().toString());
                    }
                });
        viewSimilarBtn.setOnClickListener(new View.OnClickListener() {
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
        mDatabase = FirebaseDatabase.getInstance();
        mSubjectsRef = mDatabase.getReference("Subjects");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
    }

    private void setActivityContent()
    {
        mFieldTopic = findViewById(R.id.field_topic_action_manager);
        mImgSubject = findViewById(R.id.img_subject_of_subject_action_manager);
        mTxtSubjectName = findViewById(R.id.txt_subject_name_of_subject_action_manager);
        mTxtSubjectDescription = findViewById(R.id.txt_subj_description_of_subject_action_manager);

        mBtnBack = findViewById(R.id.btn_back_of_subject_action_manager);
        mBtnRestore = findViewById(R.id.btn_restore_of_subject_action_manager);
        mBtnAddImage = findViewById(R.id.btn_add_a_photo_of_subject_action_manager);
        mBtnAskAdvice = findViewById(R.id.btn_ask_for_advice);

        mBtnBack.setOnClickListener(this);
        mBtnRestore.setOnClickListener(this);
        mBtnAddImage.setOnClickListener(this);
        mBtnAskAdvice.setOnClickListener(this);

    }

    private void loadSubjectDetails(String i_SubjectName)
    {
        mDatabase.getReference("Subjects").child(i_SubjectName)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot) {
                        mCurrentSubject = i_DataSnapshot.getValue(Subject.class);
                        Picasso.get().load(mCurrentSubject.getImgLink()).into(mImgSubject);
                        mTxtSubjectName.setText(mCurrentSubject.getSubjectName());
                        mTxtSubjectDescription.setText(mCurrentSubject.getSubjectDescription());
                        mImgLinkForChatIntent= mCurrentSubject.getImgLink();
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
        IntentCreateChatRoom.putExtra(SUBJECT_NAME, mSubjectName);
        startActivity(IntentCreateChatRoom);
    }

    //===================================================
    private void createChatRoomActivity(String i_roomName)
    {
        mChatRoomsRef = mDatabase.getReference("ChatRooms");
        mChatRoomUId = mChatRoomsRef.push().getKey();//push new chat room id and get UId
        Pair<String, String> date = getCreationDateAndTime();
        String creatorID = mCurrentUser.getUid();

        ChatRoom chatRoom = new ChatRoom
                (mChatRoomUId, i_roomName,
                date.first, date.second,
                mSubjectName,creatorID,
                mCurrentSubject.getImgLink());

        mChatRoomsRef.child(mChatRoomUId).setValue(chatRoom);//add chat room information under room id

        ActiveChatRoom activeChatRoom = new ActiveChatRoom
                (mChatRoomUId, creatorID,true);
        mDatabase.getReference("ActiveChats").child(creatorID)
                .child(mChatRoomUId).setValue(activeChatRoom);

        mDatabase.getReference("Participants").child(mChatRoomUId)
                .child(creatorID).setValue(creatorID);//add the room id to chat participants node
        mDatabase.getReference("Messages").child(mChatRoomUId).setValue(mChatRoomUId);

        mExpertsList =  mDatabaseServices.GetExpertsIDs();
        pushRequestsToDB();


        if( mExpertsList.isEmpty() )
        {
            Toast.makeText(ActivitySubjectActionManager.this,
                    "Nobody is available to chat now", Toast.LENGTH_SHORT).show();
        }
        else
        {
            pushNotify(mExpertsList,  "Need your advice about: " +
                    mSubjectName, "Tap to view the details");
            Intent intent = new Intent(this, ActivityChatRoom.class);
            intent.putExtra("chat_room_id", mChatRoomUId);
            intent.putExtra("room_name",i_roomName);
            intent.putExtra("subject_name", mSubjectName);
            intent.putExtra("subject_image",mImgLinkForChatIntent);
            this.startActivity(intent);
        }


    }

    private void pushRequestsToDB()
    {
        mDatabase.getReference("Users").child(mCurrentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                    {
                       if(i_DataSnapshot.exists())
                       {
                           String creatorId = mCurrentUser.getUid();
                           String creatorName = i_DataSnapshot.child("firstName").getValue(String.class)
                                   + " " + i_DataSnapshot.child("familyName").getValue(String.class);
                           String creatorImgLink  = i_DataSnapshot.child("imgLink").getValue(String.class);

                           for(String expertId : mExpertsList)
                           {
                               ChatRequest chatRequest = new ChatRequest();
                               chatRequest.setRequestedUserId(expertId);
                               chatRequest.setChatCreatorId(creatorId);
                               chatRequest.setChatCreatorName(creatorName);
                               chatRequest.setChatCreatorImgLink(creatorImgLink);
                               chatRequest.setChatRoomId(mChatRoomUId);
                               chatRequest.setChatRoomName(mSubjectName);
                               chatRequest.setTopic(mFieldTopic.getText().toString());

                               String requestID = mDatabase.getReference
                                       ("ChatRequests").child(expertId).push().getKey();
                               chatRequest.setRequestId(requestID);

                               mDatabase.getReference("ChatRequests")
                                       .child(expertId).child(requestID).setValue(chatRequest);
                           }
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError i_DataSnapshot) {

                    }
                });
    }

    private Pair<String,String> getCreationDateAndTime()
    {
        android.icu.util.Calendar calendar = Calendar.getInstance();
        Date currentLocalDateTime = calendar.getTime();
        android.icu.text.DateFormat time = new android.icu.text.SimpleDateFormat("HH:mm");
        String localTime = time.format(currentLocalDateTime);
        DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = date.format(currentLocalDateTime);
        return new Pair<>(currentDate,localTime);
    }


    void pushNotify(List<String> i_ExpertsIDs,  final String i_Title, final String i_Body)
    {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");

        for (String expertId : i_ExpertsIDs)
        {
            dbRef.child(expertId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    try
                    {
                        Log.e("pushing to " + user.getFirstName() + " " + user.getFamilyName(), user.getDeviceToken());
                        FCMNotification.pushFCMNotification(user.getDeviceToken(), i_Title , i_Body);
                    }
                    catch (Exception e)
                    {
                        Log.d("Error", "push notification error");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
