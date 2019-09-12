package com.studymobile.advisos.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.studymobile.advisos.Models.SubjectUser;
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderChatRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentChatRequests extends Fragment {

    private View mChatRequestsView;
    private RecyclerView mChatRequestsList;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mChatRequestsRef;
    private FirebaseAuth mAuth;
    private String mSubjectID;

    private FirebaseRecyclerOptions<SubjectUser> mOptions;
    private FirebaseRecyclerAdapter<SubjectUser, ViewHolderChatRequest> mAdapter;

    public FragmentChatRequests() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater i_Inflater, ViewGroup i_Container,
                             Bundle i_SavedInstanceState)
    {
        // Inflate the layout for this fragment
        mChatRequestsView = i_Inflater.inflate(R.layout.fragment_chat_requests, i_Container, false);
        mChatRequestsList = mChatRequestsView.findViewById(R.id.recycler_chat_requests);
        mChatRequestsList.setLayoutManager(new LinearLayoutManager(getContext()));

        mDatabase = FirebaseDatabase.getInstance();
        mChatRequestsRef = mDatabase.getReference().child("SubjectUsers/MUSIC");

        return mChatRequestsView;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        buildAdviceMeOptions();
        populateAdviceMeView();
    }

    private void buildAdviceMeOptions()
    {
        mOptions = new FirebaseRecyclerOptions.Builder<SubjectUser>()
                .setQuery(mChatRequestsRef, SubjectUser.class)
                .build();
    }

    private void populateAdviceMeView()
    {
        mAdapter = new FirebaseRecyclerAdapter<SubjectUser, ViewHolderChatRequest>(mOptions) {
            @NonNull
            @Override
            public ViewHolderChatRequest onCreateViewHolder(@NonNull ViewGroup i_ViewGroup, int i_Position) {
                //Create a new instance of the ViewHolder and use R.layout.item_dish for each item
                View view = LayoutInflater
                        .from(i_ViewGroup.getContext())
                        .inflate(R.layout.item_chat_request, i_ViewGroup, false);

                return new ViewHolderChatRequest(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderChatRequest i_ViewHolder, int i_Position,
                                            @NonNull final SubjectUser i_ChatRequest) {
                Picasso.get().load(i_ChatRequest.getUserImgLink()).into(i_ViewHolder.getUserProfileImg());
                i_ViewHolder.setUserName(i_ChatRequest.getUserName());
                i_ViewHolder.setSubjectName(i_ChatRequest.getSubjectName());
            }
        };

        mAdapter.startListening();
        mChatRequestsList.setAdapter(mAdapter);
    }

}
