package com.studymobile.advisos.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import com.studymobile.advisos.Models.Rating;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.Models.SubjectUser;
import com.studymobile.advisos.Models.UserSubject;
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderSubject;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class ActivityUserRatingBySubjects extends AppCompatActivity {

    private RecyclerView m_RecyclerView;
    private RecyclerView.LayoutManager m_LayoutManager;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserSubjectsRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private FirebaseRecyclerOptions<String> mOptions;
    private FirebaseRecyclerAdapter<String, ViewHolderSubject> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rating_by_subjects);

        Toolbar toolbar = findViewById(R.id.toolbar_of_user_rating_by_subjects);
        setSupportActionBar(toolbar);
        toolbar.getContentInsetLeft();

        m_RecyclerView = findViewById(R.id.recycler_of_activity_user_rating_by_subjects);
        m_LayoutManager = new LinearLayoutManager(this);
        m_RecyclerView.setLayoutManager(m_LayoutManager);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mUserSubjectsRef = mDatabase.getReference("UserSubjects").child(mCurrentUser.getUid());

        buildSubjectsOptions();
        populateSubjectsView();
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
                .setQuery(mUserSubjectsRef, String.class)
                .build();
    }

    private void populateSubjectsView()
    {
        mAdapter = new FirebaseRecyclerAdapter<String, ViewHolderSubject>(mOptions) {
            @NonNull
            @Override
            public ViewHolderSubject onCreateViewHolder(@NonNull ViewGroup i_ViewGroup, int i_Position) {
                //Create a new instance of the ViewHolder and use R.layout.item_dish for each item
                View view = LayoutInflater
                        .from(i_ViewGroup.getContext())
                        .inflate(R.layout.item_subject, i_ViewGroup, false);

                return new ViewHolderSubject(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderSubject i_ViewHolder, int i_Position,
                                            @NonNull final String i_SubjectName)
            {
                mDatabase.getReference("Subjects").child(i_SubjectName)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                            {
                                if(i_DataSnapshot.exists())
                                {
                                    Subject subject = i_DataSnapshot.getValue(Subject.class);

                                    i_ViewHolder.getCheckBox().setVisibility(View.INVISIBLE);
                                    i_ViewHolder.getArrowRightIcon().setVisibility(View.INVISIBLE);
                                    i_ViewHolder.getStarIcon().setVisibility(View.VISIBLE);
                                    i_ViewHolder.getAvgRating().setVisibility(View.VISIBLE);

                                    Picasso.get().load(subject.getImgLink()).into(i_ViewHolder.getSubjectImage());
                                    i_ViewHolder.setSubjectName(subject.getSubjectName());

                                    mDatabase.getReference("SubjectUsers")
                                            .child(i_SubjectName).child(mCurrentUser.getUid())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                                        {
                                            if(i_DataSnapshot.exists())
                                            {
                                                Rating rating = i_DataSnapshot.child("rating").getValue(Rating.class);
                                                if (rating == null) {
                                                    i_ViewHolder.getAvgRating().setText("--");
                                                } else {
                                                    String avgRating = String.valueOf(rating.getAvgRating());
                                                    i_ViewHolder.getAvgRating()
                                                            .setText(avgRating.substring(0, avgRating.indexOf(".") + 2));
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError i_DataSnapshot) {

                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError i_DataSnapshot) {

                            }
                        });
            }
        };

        mAdapter.startListening();
        m_RecyclerView.setAdapter(mAdapter);
    }
}
