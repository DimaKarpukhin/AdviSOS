package com.studymobile.advisos.Fragments;


import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.studymobile.advisos.Interfaces.ItemClickListener;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderSubject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSubjects extends Fragment {
    private View mSubjectsView;
    private RecyclerView mSubjectsList;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mSujectRef;
    private FirebaseAuth mAuth;
    private String mSubjectID;

    private FirebaseRecyclerOptions<Subject> mOptions;
    private FirebaseRecyclerAdapter<Subject, ViewHolderSubject> mAdapter;

    public FragmentSubjects() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater i_Inflater, ViewGroup i_Container,
                             Bundle i_SavedInstanceState)
    {
        // Inflate the layout for this fragment
        mSubjectsView = i_Inflater.inflate(R.layout.fragment_subjects, i_Container, false);
        mSubjectsList = mSubjectsView.findViewById(R.id.recycler_subjects_list_of_fragment_subjects);
        mSubjectsList.setLayoutManager(new LinearLayoutManager(getContext()));

        mDatabase = FirebaseDatabase.getInstance();
        mSujectRef = mDatabase.getReference().child("Subjects");

        return mSubjectsView;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        mOptions = new FirebaseRecyclerOptions.Builder<Subject>()
                .setQuery(mSujectRef, Subject.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Subject, ViewHolderSubject>(mOptions) {
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
            protected void onBindViewHolder(@NonNull ViewHolderSubject i_ViewHolder, int i_Position,
                                            @NonNull final Subject i_Subject) {
//                i_ViewHolder.getCheckBox().setVisibility(View.INVISIBLE);
//                i_ViewHolder.getArowwRightIcon().setVisibility(View.VISIBLE);
                i_ViewHolder.setSubjectName(i_Subject.getSubjectName());
                Picasso.get().load(i_Subject.getImgLink()).into(i_ViewHolder.getSubjectImage());
                //i_ViewHolder.getSubjectImage().setImageURI(Uri.parse(i_Subject.getImgLink()));

                i_ViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        m_AnalyticsManager.TrackDishChoiceEvent(i_Model);
//                        Toast.makeText(ActivityDishesList.this,
//                                "" + i_Model.getName(), Toast.LENGTH_SHORT).show();
//                        startDisheDetailsActivity(i_Position);
                    }
                });
            }
        };

        mAdapter.startListening();
        mSubjectsList.setAdapter(mAdapter);
    }
}
