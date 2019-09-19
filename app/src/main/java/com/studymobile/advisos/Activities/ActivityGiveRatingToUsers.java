package com.studymobile.advisos.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.studymobile.advisos.Models.ChatRoom;
import com.studymobile.advisos.Models.Rating;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.Models.SubjectUser;
import com.studymobile.advisos.Models.User;
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderRatedUser;
import com.studymobile.advisos.ViewHolders.ViewHolderSubject;

import java.util.ArrayList;

public class ActivityGiveRatingToUsers extends AppCompatActivity {

    private static final String CHAT_ROOM_ID = "chat_room_id";
    private static final String SUBJECT_NAME = "subject_name";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mChatParticipantsRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private FirebaseRecyclerOptions<String> mOptions;
    private FirebaseRecyclerAdapter<String, ViewHolderRatedUser> mAdapter;

    private String mChatRoomId;
    private String mSubjectName;

    private Dialog m_ConfirmDialog;

    private FloatingActionButton m_FabNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_rating_to_users);

        Toolbar toolbar = findViewById(R.id.toolbar_of_activity_give_rating_to_users);
        setSupportActionBar(toolbar);
        toolbar.getContentInsetLeft();

        mRecyclerView = findViewById(R.id.recycler_of_activity_give_rating_to_users);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        if(intent != null)
        {
            mSubjectName = intent.getStringExtra(SUBJECT_NAME);
            mChatRoomId = intent.getStringExtra(CHAT_ROOM_ID);
            if (!mChatRoomId.isEmpty())
            {
                mChatParticipantsRef = mDatabase.getReference("Participants")
                        .child(mChatRoomId);

                buildSubjectsOptions();
                populateSubjectsView();
            }
        }

        m_FabNext = findViewById(R.id.fab_next_of_activity_give_rating_to_users);
        m_FabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startHomeScreenActivity();
            }
        });
    }

    private void startHomeScreenActivity()
    {
        Intent IntentHomeScreen = new Intent
                (ActivityGiveRatingToUsers.this, ActivityHomeScreen.class);
        startActivity(IntentHomeScreen);
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
    public void onStart()
    {
        super.onStart();
        if(mAdapter != null)
        {
            mAdapter.startListening();
        }

    }

    @Override
    public void onStop()
    {
        if(mAdapter != null)
        {
            mAdapter.stopListening();
        }

        super.onStop();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(mAdapter != null)
        {
            mAdapter.startListening();
        }
    }

    private void buildSubjectsOptions()
    {
        mOptions = new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(mChatParticipantsRef, String.class)
                .build();
    }

    private void populateSubjectsView()
    {
        mAdapter = new FirebaseRecyclerAdapter<String, ViewHolderRatedUser>(mOptions) {
            @NonNull
            @Override
            public ViewHolderRatedUser onCreateViewHolder(@NonNull ViewGroup i_ViewGroup, int i_Position) {
                //Create a new instance of the ViewHolder and use R.layout.item_dish for each item
                View view = LayoutInflater
                        .from(i_ViewGroup.getContext())
                        .inflate(R.layout.item_rated_user, i_ViewGroup, false);

                return new ViewHolderRatedUser(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderRatedUser i_ViewHolder, int i_Position,
                                            @NonNull final String i_ParticipantID)
            {
                mDatabase.getReference("UserSubjects").child(i_ParticipantID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                            {
                                if(i_DataSnapshot.exists())
                                {
                                    ArrayList<String > userSubjList = new ArrayList<>();
                                    for(DataSnapshot dataSnapshot:i_DataSnapshot.getChildren())
                                    {
                                        userSubjList.add(dataSnapshot.getValue(String.class));
                                    }

                                    if(userSubjList.contains(mSubjectName))
                                    {
                                        updateViewHolderWithExpertUser(i_ViewHolder, mSubjectName, i_ParticipantID);
                                    }
                                    else {
                                        updateViewHolderWithNonExpertUser(i_ViewHolder, i_ParticipantID);
                                    }

                                }
                                else{
                                    updateViewHolderWithNonExpertUser(i_ViewHolder, i_ParticipantID);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError i_DataSnapshot) {

                            }
                        });
            }
        };

        mAdapter.startListening();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void updateViewHolderWithExpertUser
            (final ViewHolderRatedUser i_ViewHolder, String i_SubjectName, String i_ParticipantID)
    {
        final String subjUserRef = "SubjectUsers/" + i_SubjectName + "/" +i_ParticipantID;
        mDatabase.getReference(subjUserRef)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                    {
                        if(i_DataSnapshot.exists())
                        {
                            final SubjectUser subjectUser = i_DataSnapshot.getValue(SubjectUser.class);
                            Rating rating;
                            if(i_DataSnapshot.child("rating").exists())
                            {
                                rating = i_DataSnapshot.child("rating").getValue(Rating.class);
                                subjectUser.setRating(rating);
                            }

                            Picasso.get().load(subjectUser.getUserImgLink()).into(i_ViewHolder.getUserImageView());
                            i_ViewHolder.setUserName(subjectUser.getUserName());
                            if(i_ViewHolder.getRatingBar().isEnabled())
                            {
                                i_ViewHolder.getIconChecked().setVisibility(View.INVISIBLE);
                                i_ViewHolder.getButtonOk().setVisibility(View.VISIBLE);
                            }
                            else{
                                i_ViewHolder.getIconChecked().setVisibility(View.VISIBLE);
                                i_ViewHolder.getButtonOk().setVisibility(View.INVISIBLE);
                            }

                            mDatabase.getReference("ChatRooms").child(mChatRoomId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                                        {
                                            if(i_DataSnapshot.exists())
                                            {

                                                if(subjectUser.getUserId().equals(i_DataSnapshot.child("creatorId"))
                                                        || subjectUser.getUserId().equals(mCurrentUser.getUid()))
                                                {
                                                    i_ViewHolder.getIconChecked().setVisibility(View.VISIBLE);
                                                    i_ViewHolder.getButtonOk().setVisibility(View.INVISIBLE);
                                                    i_ViewHolder.getRatingBar().setRating(5);
                                                    i_ViewHolder.getRatingBar().setEnabled(false);
                                                }
                                            }

                                            i_ViewHolder.getButtonOk().setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v)
                                                {
                                                    showConfirmDialog(i_ViewHolder, subjectUser, subjUserRef);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError i_DataSnapshot) {

                                        }
                                    });

                            i_ViewHolder.getRatingBar().setOnRatingBarChangeListener
                                    (new RatingBar.OnRatingBarChangeListener() {
                                @Override
                                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) { }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError i_DataSnapshot) { }
                });
    }

    private void updateViewHolderWithNonExpertUser
            (final ViewHolderRatedUser i_ViewHolder, String i_ParticipantID)
    {
        mDatabase.getReference("Users")
                .child(i_ParticipantID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                    {
                        if(i_DataSnapshot.exists())
                        {
                            User user = i_DataSnapshot.getValue(User.class);
                            Picasso.get().load(user.getImgLink()).into(i_ViewHolder.getUserImageView());
                            i_ViewHolder.setUserName(user.getFirstName() + " " + user.getFamilyName());

                            if(i_ViewHolder.getRatingBar().isEnabled())
                            {
                                i_ViewHolder.getIconChecked().setVisibility(View.INVISIBLE);
                                i_ViewHolder.getButtonOk().setVisibility(View.VISIBLE);
                            }
                            else{
                                i_ViewHolder.getIconChecked().setVisibility(View.VISIBLE);
                                i_ViewHolder.getButtonOk().setVisibility(View.INVISIBLE);
                            }




                            mDatabase.getReference("ChatRooms").child(mChatRoomId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                                        {
                                            if(i_DataSnapshot.exists())
                                            {

                                                if(user.getUserId().equals(i_DataSnapshot.child("creatorId"))
                                                        || user.getUserId().equals(mCurrentUser.getUid()))
                                                {
                                                    i_ViewHolder.getIconChecked().setVisibility(View.VISIBLE);
                                                    i_ViewHolder.getButtonOk().setVisibility(View.INVISIBLE);
                                                    i_ViewHolder.getRatingBar().setRating(5);
                                                    i_ViewHolder.getRatingBar().setEnabled(false);
                                                }
                                            }

                                            i_ViewHolder.getButtonOk().setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v)
                                                {
                                                    showConfirmDialog(i_ViewHolder, null, null);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError i_DataSnapshot) {

                                        }
                                    });
                            i_ViewHolder.getRatingBar().setOnRatingBarChangeListener
                                    (new RatingBar.OnRatingBarChangeListener() {
                                        @Override
                                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) { }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError i_DataSnapshot) {

                    }
                });
    }

    private void showConfirmDialog(final ViewHolderRatedUser i_ViewHolder,
                                   final SubjectUser i_SubjectUser, final String i_SubjUserRef)
    {
        m_ConfirmDialog = new Dialog(ActivityGiveRatingToUsers.this);
        m_ConfirmDialog.setContentView(R.layout.dialog_confirm);
        m_ConfirmDialog.setCanceledOnTouchOutside(false);

        final ImageButton closeBtn = m_ConfirmDialog.findViewById(R.id.btn_close_of_dialog_confirm);
        TextView fieldTitle = m_ConfirmDialog.findViewById(R.id.txt_title_of_dialog_confirm);
        TextView rightBtn = m_ConfirmDialog.findViewById(R.id.btn_right_of_dialog_confirm);
        final TextView leftBtn = m_ConfirmDialog.findViewById(R.id.btn_left_of_dialog_confirm);

        closeBtn.setVisibility(View.INVISIBLE);
        fieldTitle.setText("After pressing 'ACCEPT' you'll not able to change the decision");
        rightBtn.setText("Accept");
        leftBtn.setText("Cancel");

        i_ViewHolder.getButtonOk().setVisibility(View.INVISIBLE);
        i_ViewHolder.getIconChecked().setVisibility(View.VISIBLE);

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               if(i_SubjectUser != null)
               {
                   i_SubjectUser.Rate(i_ViewHolder.getRatingBar().getRating());
                   mDatabase.getReference(i_SubjUserRef)
                           .child("rating").setValue(i_SubjectUser.getRating())
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   i_ViewHolder.getRatingBar().setEnabled(false);
                                   m_ConfirmDialog.dismiss();
                               }
                           });
               }
            }
        });

        m_ConfirmDialog.setCanceledOnTouchOutside(false);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_ConfirmDialog.dismiss();
                i_ViewHolder.getButtonOk().setVisibility(View.VISIBLE);
                i_ViewHolder.getIconChecked().setVisibility(View.INVISIBLE);
            }
        });

        m_ConfirmDialog.show();
    }
}
