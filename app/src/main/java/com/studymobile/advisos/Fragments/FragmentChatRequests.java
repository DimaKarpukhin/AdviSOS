package com.studymobile.advisos.Fragments;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.studymobile.advisos.Activities.ActivityUserDetails;
import com.studymobile.advisos.Models.ChatRequest;
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
    private DatabaseReference mUserRequestsRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private FirebaseRecyclerOptions<ChatRequest> mOptions;
    private FirebaseRecyclerAdapter<ChatRequest, ViewHolderChatRequest> mAdapter;

    private Dialog m_PopupDialog;
    private Uri m_DialogImgURI;
    private CircleImageView m_DialogImgView;

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

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mUserRequestsRef = mDatabase.getReference("ChatRequests")
                .child(mCurrentUser.getUid());

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
        mOptions = new FirebaseRecyclerOptions.Builder<ChatRequest>()
                .setQuery(mUserRequestsRef, ChatRequest.class)
                .build();
    }

    private void populateAdviceMeView()
    {
        mAdapter = new FirebaseRecyclerAdapter<ChatRequest, ViewHolderChatRequest>(mOptions) {
            @NonNull
            @Override
            public ViewHolderChatRequest onCreateViewHolder(
                    @NonNull ViewGroup i_ViewGroup, int i_Position)
            {
                View view = LayoutInflater
                        .from(i_ViewGroup.getContext())
                        .inflate(R.layout.item_chat_request, i_ViewGroup, false);

                return new ViewHolderChatRequest(view);
            }

            @Override
            protected void onBindViewHolder(
                    @NonNull ViewHolderChatRequest i_ViewHolder, int i_Position,
                    @NonNull final ChatRequest i_ChatRequest)
            {
                Picasso.get().load(i_ChatRequest.getChatCreatorImgLink()).into(i_ViewHolder.getChatCreatorImg());
                i_ViewHolder.setChatCreatorName(i_ChatRequest.getChatCreatorName());
                i_ViewHolder.setSubjectName(i_ChatRequest.getChatRoomName());
                i_ViewHolder.setTopic(i_ChatRequest.getTopic());

                i_ViewHolder.getChatCreatorImg().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View i_View) {
                        showPopupDialog(i_ChatRequest.getChatCreatorImgLink());
                    }
                });

                i_ViewHolder.getBtnAccept().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View i_View) {
                        addUserToActiveChats();
                        //removeRequestFromDB();
                    }
                });

                i_ViewHolder.getBtnReject().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View i_View) {
                        removeRequestFromDB();
                    }
                });
            }
        };

        mAdapter.startListening();
        mChatRequestsList.setAdapter(mAdapter);
    }

    private void addUserToActiveChats()
    {
        Toast.makeText(getContext(),"Accepted", Toast.LENGTH_SHORT).show();
    }
    private void removeRequestFromDB()
    {
        Toast.makeText(getContext(),"Rejected", Toast.LENGTH_SHORT).show();
    }

    private void showPopupDialog(String i_ImgLink)
    {
        m_PopupDialog = new Dialog(getContext());
        m_PopupDialog.setContentView(R.layout.dialog_image);
        m_PopupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        m_DialogImgView = m_PopupDialog.findViewById(R.id.img_of_dialog_profile_picture);
        Picasso.get().load(i_ImgLink).into(m_DialogImgView);

        m_PopupDialog.findViewById(R.id.btn_ok_of_dialog_profile_picture)
                .setVisibility(View.INVISIBLE);
        m_PopupDialog.findViewById(R.id.btn_remove_of_dialog_profile_picture)
                .setVisibility(View.INVISIBLE);
        m_PopupDialog.findViewById(R.id.btn_add_a_photo_of_dialog_profile_picture)
                .setVisibility(View.INVISIBLE);


        m_PopupDialog.findViewById(R.id.btn_close_of_dialog_profile_picture)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        m_PopupDialog.dismiss();
                    }
                });

        m_PopupDialog.show();
    }
}
