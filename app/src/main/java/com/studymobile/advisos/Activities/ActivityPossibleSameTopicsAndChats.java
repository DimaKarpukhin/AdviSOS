package com.studymobile.advisos.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.studymobile.advisos.Adapters.AdapterPossibleChatsTabsAccessor;
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

public class ActivityPossibleSameTopicsAndChats extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private AdapterPossibleChatsTabsAccessor mAdapterTabsAccessor;
    private DrawerLayout m_DrawerLayout;
    private TextView mTextViewCreateChatRoom;
    private static final String SUBJECT_NAME = "subject_name";
    private FirebaseAuth m_Auth;
    private FirebaseUser mCurrentUser;
    private FirebaseDatabase mDatabase;
    private FirebaseStorage m_Storage;
    private String mSubjectName;
    private Subject mCurrentSubject;
    private String mRoomName;
    private UserLocation mUserLocation;
    private String mImgLinkForChatIntent;
    String mChatRoomUId;
    List<String> mExpertsList;
    DatabaseReference mChatRoomsRef;
    private DatabaseServices mDatabaseServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_possible_same_topics_and_chats);
        init();
        setFirebaseData();
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
    private void loadSubjectDetails(String i_SubjectName)
    {
        mDatabase.getReference("Subjects").child(i_SubjectName)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot) {
                        mCurrentSubject = i_DataSnapshot.getValue(Subject.class);
//                        Picasso.get().load(mCurrentSubject.getImgLink()).into(mImgSubject);
//                        mTxtSubjectName.setText(mCurrentSubject.getSubjectName());
//                        mTxtSubjectDescription.setText(mCurrentSubject.getSubjectDescription());
                        mImgLinkForChatIntent= mCurrentSubject.getImgLink();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void init() {
        mSubjectName = getIntent().getStringExtra(SUBJECT_NAME);
        mRoomName = getIntent().getStringExtra("topic");
        mViewPager = findViewById(R.id.pager_of_possible_chats_and_topics);
        mAdapterTabsAccessor = new AdapterPossibleChatsTabsAccessor(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapterTabsAccessor);
        mTabLayout = findViewById(R.id.tabs_of_possible_chats_and_topics);
        mTabLayout.setupWithViewPager(mViewPager);
        m_DrawerLayout = findViewById(R.id.drawer_possible_chats_and_topics);
        mTextViewCreateChatRoom = findViewById(R.id.btn_create_new_chat);
        mTextViewCreateChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createChatRoomActivity(mRoomName);
            }
        });
    }
    private void setFirebaseData()
    {
        m_Auth = FirebaseAuth.getInstance();
        mCurrentUser = m_Auth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        m_Storage = FirebaseStorage.getInstance();
        DatabaseReference userRef = mDatabase.getReference("Users");
        userRef.child(mCurrentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot i_DataSnapshot) {
                mUserLocation = i_DataSnapshot.getValue(UserLocation.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createChatRoomActivity(String i_roomName) {
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
            Toast.makeText(ActivityPossibleSameTopicsAndChats.this,
                    "Nobody is available to chat now", Toast.LENGTH_SHORT).show();
        }
        else
        {
            pushNotify(mExpertsList,  "Need your advice about: " +
                    mSubjectName, "Tap to view the details");
            addRequestedUsersToDB();
            Intent intent = new Intent(this, ActivityChatRoom.class);
            intent.putExtra("chat_room_id", mChatRoomUId);
            intent.putExtra("room_name",i_roomName);
            intent.putExtra("subject_name", mSubjectName);
            intent.putExtra("subject_image",mImgLinkForChatIntent);
            this.startActivity(intent);
        }

    }
    private void addRequestedUsersToDB()
    {
        DatabaseReference requestedUsers = mDatabase
                .getReference("RequestedChatRoomUsers").child(mChatRoomUId);
        for(String expertID: mExpertsList)
        {
            requestedUsers.child(expertID).setValue(expertID);
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
                                chatRequest.setTopic(mRoomName);
                                chatRequest.setSubjectImgLink(mCurrentSubject.getImgLink());


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


    void pushNotify(List<String> i_ExpertsIDs, final String i_Title, final String i_Body)
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

    @Override
    public void onBackPressed() {
        if (m_DrawerLayout.isDrawerOpen(GravityCompat.START)) {
            m_DrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
