package com.studymobile.advisos.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
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
import com.studymobile.advisos.Interfaces.ItemClickListener;
import com.studymobile.advisos.Models.ActiveChatRoom;
import com.studymobile.advisos.Models.ChatRoom;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderAdviceGroup;
import com.studymobile.advisos.ViewHolders.ViewHolderSubject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAdviceMe extends Fragment
{
    private View mAdviceMeView;
    private RecyclerView mAdviceMeList;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mActiveChat;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private FirebaseRecyclerOptions<ActiveChatRoom> mOptions;
    private FirebaseRecyclerAdapter<ActiveChatRoom, ViewHolderAdviceGroup> mAdapter;


    public FragmentAdviceMe() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater i_Inflater, ViewGroup i_Container,
                             Bundle i_SavedInstanceState)
    {
        // Inflate the layout for this fragment
        mAdviceMeView = i_Inflater.inflate(R.layout.fragment_advice_me, i_Container, false);
        mAdviceMeList = mAdviceMeView.findViewById(R.id.recycler_subjects_list_of_fragment_advice_me);
        mAdviceMeList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mActiveChat = mDatabase.getReference().child("ActiveChats");

        return mAdviceMeView;
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
        mOptions = new FirebaseRecyclerOptions.Builder<ActiveChatRoom>()
                .setQuery(mActiveChat.child(mCurrentUser.getUid())
                        .orderByChild("isCreator").equalTo(true), ActiveChatRoom.class)
                .build();
    }

    private void populateAdviceMeView()
    {
        mAdapter = new FirebaseRecyclerAdapter<ActiveChatRoom, ViewHolderAdviceGroup>(mOptions) {
            @NonNull
            @Override
            public ViewHolderAdviceGroup onCreateViewHolder
                    (@NonNull ViewGroup i_ViewGroup, int i_Position)
            {
                View view = LayoutInflater
                        .from(i_ViewGroup.getContext())
                        .inflate(R.layout.item_advice_group, i_ViewGroup, false);

                return new ViewHolderAdviceGroup(view);
            }

            @Override
            protected void onBindViewHolder
                    (@NonNull final ViewHolderAdviceGroup i_ViewHolder,
                     final int i_Position, @NonNull final ActiveChatRoom i_ActiveChatRoom)
            {
                DatabaseReference chatRoomRef = mDatabase.getReference("ChatRooms");
                chatRoomRef.child(i_ActiveChatRoom.getChatRoomId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                            {
                                ChatRoom chatRoom = i_DataSnapshot.getValue(ChatRoom.class);
                                Picasso.get().load(chatRoom.getImgLink()).into(i_ViewHolder.getGroupProfileImg());
                                i_ViewHolder.setParentSubjectName(chatRoom.getSubjectName());
                                i_ViewHolder.setGroupTopic(chatRoom.getRoomName());
                                i_ViewHolder.setLastMessageTime(chatRoom.getCreationTime());
                                i_ViewHolder.setLastMessageDate(chatRoom.getCreationDate());

                                i_ViewHolder.setItemClickListener(new ItemClickListener() {
                                    @Override
                                    public void onClick(View view, int position, boolean isLongClick)
                                    {
                                        Toast.makeText(getContext(),"Advice me", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError i_DataSnapshot) {

                            }
                        });

            }
        };

        mAdapter.startListening();
        mAdviceMeList.setAdapter(mAdapter);
    }
}
