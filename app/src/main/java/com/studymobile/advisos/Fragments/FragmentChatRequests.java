package com.studymobile.advisos.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderSubject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentChatRequests extends Fragment {

    private RecyclerView mSubjectsList;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mSubjectRef;
    private FirebaseAuth mAuth;
    private String mSubjectID;

    private FirebaseRecyclerOptions<Subject> mOptions;
    private FirebaseRecyclerAdapter<Subject, ViewHolderSubject> mAdapter;


    public FragmentChatRequests() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_requests, container, false);
    }

}
