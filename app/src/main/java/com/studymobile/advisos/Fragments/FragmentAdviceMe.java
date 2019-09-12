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
    private DatabaseReference mAdviceMeRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mSubjectID;

    private FirebaseRecyclerOptions<ChatRoom> mOptions;
    private FirebaseRecyclerAdapter<ChatRoom, ViewHolderAdviceGroup> mAdapter;

    private List<String> mUserActiveChatRooms = new ArrayList<>();

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
        mAdviceMeRef = mDatabase.getReference().child("ChatRooms");

        return mAdviceMeView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        buildAdviceMeOptions();
        populateAdviceMeView();

    }

    private void updateUI(@NonNull final ViewHolderAdviceGroup i_ViewHolder, int i_Position,
                          @NonNull final ChatRoom i_ChatRoom)
    {

        DatabaseReference d = mAdapter.getRef(i_Position).getRef();

        d.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot) {
//                        for(DataSnapshot chatRoomID : i_DataSnapshot.getChildren())
//                        {
                            ChatRoom chatRoom = i_DataSnapshot.getValue(ChatRoom.class);

                            Toast.makeText(getContext(),">>>" + chatRoom.getRoomId()
                                    + "<<<\n" + mUserActiveChatRooms.size(), Toast.LENGTH_LONG).show();
                            if(mUserActiveChatRooms.contains(chatRoom.getRoomId()))
                            {
                                i_ViewHolder.getBtnUnreadMessages().setVisibility(View.VISIBLE);
                                i_ViewHolder.setParentSubjectName(chatRoom.getSubjectName());
                                i_ViewHolder.setGroupTopic(chatRoom.getRoomName());
                                i_ViewHolder.setLastMessageTime(chatRoom.getCreationTime());
                                Picasso.get().load(chatRoom.getImgLink()).into(i_ViewHolder.getGroupProfileImg());
                            }

                     //   }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void buildAdviceMeOptions()
    {
        mOptions = new FirebaseRecyclerOptions.Builder<ChatRoom>()
                .setQuery(mAdviceMeRef.orderByChild("creatorId").equalTo(mCurrentUser.getUid()), ChatRoom.class)
                .build();
    }

    private void populateAdviceMeView()
    {
        mAdapter = new FirebaseRecyclerAdapter<ChatRoom, ViewHolderAdviceGroup>(mOptions) {
            @NonNull
            @Override
            public ViewHolderAdviceGroup onCreateViewHolder(@NonNull ViewGroup i_ViewGroup, int i_Position) {
                //Create a new instance of the ViewHolder and use R.layout.item_dish for each item
                View view = LayoutInflater
                        .from(i_ViewGroup.getContext())
                        .inflate(R.layout.item_advice_group, i_ViewGroup, false);

                return new ViewHolderAdviceGroup(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderAdviceGroup i_ViewHolder, final int i_Position,
                                            @NonNull final ChatRoom i_ChatRoom) {

                final String currentUserID = mCurrentUser.getUid();
                mDatabase.getReference("ActiveChats").child(currentUserID)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                            {
                                if(i_DataSnapshot.exists())
                                {
                                    for (DataSnapshot chatRoomID : i_DataSnapshot.getChildren())
                                    {
                                        if(!mUserActiveChatRooms.contains(chatRoomID.getKey()))
                                        {
                                            mUserActiveChatRooms.add(chatRoomID.getKey());
                                        }
                                    }

//                                    Toast.makeText(getContext(), mUserActiveChatRooms.size()
//                                            + " " + currentUserID
//                                            +"\n" + mUserActiveChatRooms.get(0), Toast.LENGTH_LONG).show();
                                    updateUI(i_ViewHolder, i_Position, i_ChatRoom);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
//                if(mUserChatRooms.contains(i_ChatRoom.getRoomId()))
//                {
//                    i_ViewHolder.setVisibility(View.VISIBLE);
//                    Toast.makeText(getContext(),"GOOD", Toast.LENGTH_LONG).show();
//                    i_ViewHolder.getBtnUnreadMessages().setVisibility(View.INVISIBLE);
//                    i_ViewHolder.setParentSubjectName(i_ChatRoom.getSubjectName());
//                    i_ViewHolder.setGroupTopic(i_ChatRoom.getRoomName());
//                    i_ViewHolder.setLastMessageTime(i_ChatRoom.getCreationTime());
//                    Picasso.get().load(i_ChatRoom.getImgLink()).into(i_ViewHolder.getGroupProfileImg());
//                }
//                else{
//                    Toast.makeText(getContext(),"BAD", Toast.LENGTH_LONG).show();
//                    i_ViewHolder.setVisibility(View.GONE);
//                }
//
//                i_ViewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//
//                    }
//                });
            }
        };

        mAdapter.startListening();
        mAdviceMeList.setAdapter(mAdapter);
    }

}
