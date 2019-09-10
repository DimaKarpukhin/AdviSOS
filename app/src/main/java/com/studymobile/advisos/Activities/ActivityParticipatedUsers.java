package com.studymobile.advisos.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.studymobile.advisos.Models.SubjectUser;
import com.studymobile.advisos.Models.User;
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderParticipatedUser;

import java.util.HashSet;

public class ActivityParticipatedUsers extends AppCompatActivity {

    private RecyclerView mUsersParticipatingRecyclerView;
    private  FirebaseDatabase mDataBase;
    private FirebaseStorage mStorage;
    private DatabaseReference mSubjectUsersRef;
    private String mUserUid;
    private String mChatRoomUid;
    private String mSubjectName;
    private HashSet<String> mParticipantsIds = new HashSet<>();
    private FirebaseRecyclerAdapter<SubjectUser, ViewHolderParticipatedUser> mParticipatedUsersAdapter;
    private FirebaseRecyclerOptions<SubjectUser> mOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participated_users);
        mUserUid = getIntent().getStringExtra("user_id");
        mChatRoomUid = getIntent().getStringExtra("chat_room_id");
        init();
        setDataBaseReference();
        setParticipentsSet();
        startPupolateRecyclerView();

    }

    private void setParticipentsSet() {
        DatabaseReference participantsRes = mDataBase.getReference("Participants");
        participantsRes.child(mChatRoomUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot userIds : dataSnapshot.getChildren())
                {
                    mParticipantsIds.add(userIds.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setDataBaseReference() {
        mDataBase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();
        String subjectName = getCurrentChatRoomSubjectName();
        mSubjectUsersRef = mDataBase.getReference("SubjectUsers/" + subjectName);


    }

    private void init() {
        mUsersParticipatingRecyclerView = findViewById(R.id.recycler_participating_users);
        mUsersParticipatingRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void startPupolateRecyclerView() {
        setOptions();
        fillRecyclerView();




    }

    private void fillRecyclerView() {
        mParticipatedUsersAdapter = new FirebaseRecyclerAdapter<SubjectUser, ViewHolderParticipatedUser>(mOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderParticipatedUser viewHolderParticipatedUser,
                                            int i, @NonNull SubjectUser i_subjectUser) {
                if(mParticipantsIds.contains(i_subjectUser.getUserId()))
                {
                    viewHolderParticipatedUser.setUserName(i_subjectUser.getUserName());
                    viewHolderParticipatedUser.setUserRate(i_subjectUser.getRating().getAvgRating());
                    Picasso.get().load(i_subjectUser.getUserImgLink()).into(viewHolderParticipatedUser.getUserImage());
                }

            }

            @NonNull
            @Override
            public ViewHolderParticipatedUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view =
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.item_participated_users, parent, false);
                return new ViewHolderParticipatedUser(view);
            }
        };
        mParticipatedUsersAdapter.startListening();
        mUsersParticipatingRecyclerView.setAdapter(mParticipatedUsersAdapter);
    }

    private String getCurrentChatRoomSubjectName(){
        DatabaseReference chatRoomRef = mDataBase.getReference("ChatRooms").child(mChatRoomUid);
        chatRoomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mSubjectName = dataSnapshot.child("subjectName").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return mSubjectName;


    }
    private void setOptions(){
        mOptions =  new FirebaseRecyclerOptions.Builder<SubjectUser>()
                .setQuery(mSubjectUsersRef, SubjectUser.class)
                .build();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mParticipatedUsersAdapter.stopListening();
    }
}
