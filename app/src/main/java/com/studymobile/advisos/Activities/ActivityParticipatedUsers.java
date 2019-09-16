package com.studymobile.advisos.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.internal.Logger;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.studymobile.advisos.Models.Rating;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.Models.SubjectUser;
import com.studymobile.advisos.Models.User;
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderParticipatedUser;

import java.util.HashSet;

public class ActivityParticipatedUsers extends AppCompatActivity {


    private static final String SUBJECT_NAME = "subject_name";

    private RecyclerView mUsersParticipatingRecyclerView;
    private FirebaseDatabase mDataBase;
    private FirebaseStorage mStorage;
    private DatabaseReference mParticipantsRef;
    private String mUserUid;
    private String mChatRoomUid;
    private String mSubjectName;
    private HashSet<String> mParticipantsIds = new HashSet<>();
    private FirebaseRecyclerAdapter<String, ViewHolderParticipatedUser> mParticipatedUsersAdapter;
    private FirebaseRecyclerOptions<String> mOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participated_users);
        mUserUid = getIntent().getStringExtra("user_id");
        mChatRoomUid = getIntent().getStringExtra("chat_room_id");
        mSubjectName = getIntent().getStringExtra(SUBJECT_NAME);

        Toolbar toolbar = findViewById(R.id.toolbar_of_participants_screen);
        setSupportActionBar(toolbar);
        toolbar.getContentInsetLeft();
        init();
        setDataBaseReference();
        setParticipentsSet();
//        Toast.makeText(this, "" + mParticipantsIds.size(), Toast.LENGTH_LONG).show();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
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

                Toast.makeText(ActivityParticipatedUsers.this, "" + mParticipantsIds.size(), Toast.LENGTH_LONG).show();
                startPupolateRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setDataBaseReference() {
        mDataBase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();
        getCurrentChatRoomSubjectName();
        mParticipantsRef = mDataBase.getReference("Participants/" + mChatRoomUid);


    }

    private void init() {
        mUsersParticipatingRecyclerView = findViewById(R.id.recycler_participating_users);
        mUsersParticipatingRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void startPupolateRecyclerView() {
        setOptions();
        fillRecyclerView();
    }

    private void fillRecyclerView()
    {
        mParticipatedUsersAdapter = new FirebaseRecyclerAdapter
                <String, ViewHolderParticipatedUser>(mOptions) {
            @Override
            protected void onBindViewHolder
                    (@NonNull final ViewHolderParticipatedUser viewHolderParticipatedUser, int i, @NonNull String i_UserId)
            {
                    mDataBase.getReference("SubjectUsers").child(mSubjectName)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                                {
                                    SubjectUser currentSubjectUser;
                                    if (i_DataSnapshot.exists())
                                    {
                                        boolean found = false;
                                        for (DataSnapshot snapshotSubjUser : i_DataSnapshot.getChildren())
                                        {
                                            currentSubjectUser = snapshotSubjUser.getValue(SubjectUser.class);
                                            Rating userRating = snapshotSubjUser.child("rating").getValue(Rating.class);
                                            if (mParticipantsIds.contains(currentSubjectUser.getUserId()))
                                            {
                                                Picasso.get().load(currentSubjectUser.getUserImgLink())
                                                        .into(viewHolderParticipatedUser.getUserImage());
                                                viewHolderParticipatedUser.setUserName(currentSubjectUser.getUserName());
                                                if (userRating != null)
                                                {
                                                    viewHolderParticipatedUser.setUserRate
                                                            (currentSubjectUser.getRating().getAvgRating());
                                                } else
                                                    {
                                                    viewHolderParticipatedUser.setUserRate(-1);
                                                }

                                                mParticipantsIds.remove(currentSubjectUser.getUserId());
                                                found = true;
                                                break;

                                            }
                                        }
                                        if(!found)
                                        {
                                            mDataBase.getReference("Users")
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                                                        {
                                                            if (i_DataSnapshot.exists())
                                                            {
                                                                for (DataSnapshot snapshotUser : i_DataSnapshot.getChildren())
                                                                {
                                                                    User currentUser = snapshotUser.getValue(User.class);
                                                                    if (mParticipantsIds.contains(currentUser.getUserId()))
                                                                    {
                                                                        Picasso.get().load(currentUser.getImgLink())
                                                                                .into(viewHolderParticipatedUser.getUserImage());
                                                                        viewHolderParticipatedUser.setUserName(currentUser.getFirstName()
                                                                                + " " + currentUser.getFamilyName());
                                                                        viewHolderParticipatedUser.setUserRate(-1);
                                                                        mParticipantsIds.remove(currentUser.getUserId());
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                                                    });
                                        }


                                    }
                                    else {
                                        mDataBase.getReference("Users")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                                                    {
                                                        if (i_DataSnapshot.exists())
                                                        {
                                                            for (DataSnapshot snapshotUser : i_DataSnapshot.getChildren())
                                                            {
                                                                User currentUser = snapshotUser.getValue(User.class);
                                                                if (mParticipantsIds.contains(currentUser.getUserId()))
                                                                {
                                                                    Picasso.get().load(currentUser.getImgLink())
                                                                            .into(viewHolderParticipatedUser.getUserImage());
                                                                    viewHolderParticipatedUser.setUserName(currentUser.getFirstName()
                                                                            + " " + currentUser.getFamilyName());
                                                                    viewHolderParticipatedUser.setUserRate(-1);
                                                                    mParticipantsIds.remove(currentUser.getUserId());
                                                                    break;
                                                                }

                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
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

    //TODO potential bug here
    private void getCurrentChatRoomSubjectName(){
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

    }
    private void setOptions(){
        mOptions =  new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(mParticipantsRef, String.class)
                .build();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mParticipatedUsersAdapter.stopListening();
    }






//    private RecyclerView mUsersParticipatingRecyclerView;
//    private  FirebaseDatabase mDataBase;
//    private FirebaseStorage mStorage;
//    private DatabaseReference mSubjectUsersRef;
//    private String mUserUid;
//    private String mChatRoomUid;
//    private String mSubjectName;
//    private HashSet<String> mParticipantsIds = new HashSet<>();
//    private FirebaseRecyclerAdapter<SubjectUser, ViewHolderParticipatedUser> mParticipatedUsersAdapter;
//    private FirebaseRecyclerOptions<SubjectUser> mOptions;
//    TODO
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_participated_users);
//        mUserUid = getIntent().getStringExtra("user_id");
//        mChatRoomUid = getIntent().getStringExtra("chat_room_id");
//        init();
//        setDataBaseReference();
//        setParticipentsSet();
////        Toast.makeText(this, "" + mParticipantsIds.size(), Toast.LENGTH_LONG).show();
//
//
//    }
//
//    private void setParticipentsSet() {
//        DatabaseReference participantsRes = mDataBase.getReference("Participants");
//        participantsRes.child(mChatRoomUid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot userIds : dataSnapshot.getChildren())
//                {
//                    mParticipantsIds.add(userIds.getValue(String.class));
//                }
//
//                Toast.makeText(ActivityParticipatedUsers.this, "" + mParticipantsIds.size(), Toast.LENGTH_LONG).show();
//                startPupolateRecyclerView();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void setDataBaseReference() {
//        mDataBase = FirebaseDatabase.getInstance();
//        mStorage = FirebaseStorage.getInstance();
//        getCurrentChatRoomSubjectName();
//        mSubjectUsersRef = mDataBase.getReference("SubjectUsers/" + mSubjectName);
//
//
//    }
//
//    private void init() {
//        mUsersParticipatingRecyclerView = findViewById(R.id.recycler_participating_users);
//        mUsersParticipatingRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//    }
//
//    private void startPupolateRecyclerView() {
//        setOptions();
//        fillRecyclerView();
//
//
//
//
//    }
//
//    private void fillRecyclerView() {
//        mParticipatedUsersAdapter = new FirebaseRecyclerAdapter<SubjectUser, ViewHolderParticipatedUser>(mOptions) {
//            @Override
//            protected void onBindViewHolder(@NonNull ViewHolderParticipatedUser viewHolderParticipatedUser,
//                                            int i, @NonNull SubjectUser i_subjectUser) {
//                if(mParticipantsIds.contains(i_subjectUser.getUserId()))
//                {
//                    Toast.makeText(getApplicationContext(),"contains User", Toast.LENGTH_LONG);
//                    viewHolderParticipatedUser.setUserName(i_subjectUser.getUserName());
//                    viewHolderParticipatedUser.setUserRate(i_subjectUser.getRating().getAvgRating());
//                    Picasso.get().load(i_subjectUser.getUserImgLink()).into(viewHolderParticipatedUser.getUserImage());
//                }
//
//            }
//
//            @NonNull
//            @Override
//            public ViewHolderParticipatedUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view =
//                        LayoutInflater
//                                .from(parent.getContext())
//                                .inflate(R.layout.item_participated_users, parent, false);
//                return new ViewHolderParticipatedUser(view);
//            }
//        };
//        mParticipatedUsersAdapter.startListening();
//        mUsersParticipatingRecyclerView.setAdapter(mParticipatedUsersAdapter);
//    }
//     //TODO potential bug here
//    private void getCurrentChatRoomSubjectName(){
//        DatabaseReference chatRoomRef = mDataBase.getReference("ChatRooms").child(mChatRoomUid);
//        chatRoomRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mSubjectName = dataSnapshot.child("subjectName").getValue(String.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }
//    private void setOptions(){
//        mOptions =  new FirebaseRecyclerOptions.Builder<SubjectUser>()
//                .setQuery(mSubjectUsersRef, SubjectUser.class)
//                .build();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mParticipatedUsersAdapter.stopListening();
//    }
}
