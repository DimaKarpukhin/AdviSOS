package com.studymobile.advisos.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.studymobile.advisos.Activities.ActivityChatRoom;
import com.studymobile.advisos.Adapters.AdapterPossibleActiveChats;
import com.studymobile.advisos.Interfaces.ItemClickListener;
import com.studymobile.advisos.Models.ActiveChatRoom;
import com.studymobile.advisos.Models.ChatRoom;
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderAdviceGroup;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */

public class FragmentPossibleActiveChats extends Fragment {

    private View mViewPossibleActive;
    private RecyclerView mRecyclerPossibleActive;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mActiveChat;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mSubjectName;
    private String mRoomName;
    private Set<String> mCurrentClientTopicWords;
    private AdapterPossibleActiveChats mAdapter;
    private List<ChatRoom> mFilteredChatsList;
    private List<ChatRoom> mOptions;
    //private FirebaseRecyclerAdapter<ChatRoom, ViewHolderAdviceGroup> mAdapter;

    public FragmentPossibleActiveChats(){}




    @Override
    public View onCreateView(LayoutInflater i_Inflater, ViewGroup i_Container,
                             Bundle i_SavedInstanceState)
    {
        mSubjectName = getActivity().getIntent().getStringExtra("subject_name");
        mFilteredChatsList = new LinkedList<>();
        mOptions = new LinkedList<>();
        mRoomName = getActivity().getIntent().getStringExtra("topic");
        mViewPossibleActive = i_Inflater.inflate(R.layout.fragment_possible_active_chats, i_Container, false);
        mRecyclerPossibleActive = mViewPossibleActive.findViewById(R.id.recycler_possible_active_chats);
        mRecyclerPossibleActive.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mActiveChat = mDatabase.getReference("ChatRooms");
        assignSet();
        return mViewPossibleActive;
    }

    @Override
    public void onStart() {
        super.onStart();
        buildPossibleActiveChatsOptions();
       // pupolateRecyclerView();
//        mAdapter = new AdapterPossibleActiveChats(mFilteredChatsList,getContext());
//        mRecyclerPossibleActive.setAdapter(mAdapter);


    }

    private void assignSet() {
        String[] currentWords = mRoomName.split("\\s+");
        mCurrentClientTopicWords = new HashSet<>();
        for(String str : currentWords){
            mCurrentClientTopicWords.add(str.trim().toLowerCase());
        }
    }

//    private void populatePossibleActiveChatsView() {
//        mAdapter = new FirebaseRecyclerAdapter<ChatRoom, ViewHolderAdviceGroup>(mOptions) {
//            @NonNull
//            @Override
//            public ViewHolderAdviceGroup onCreateViewHolder
//                    (@NonNull ViewGroup i_ViewGroup, int i_Position)
//            {
//                View view = LayoutInflater
//                        .from(i_ViewGroup.getContext())
//                        .inflate(R.layout.item_advice_group, i_ViewGroup, false);
//
//                return new ViewHolderAdviceGroup(view);
//            }
//
//            @Override
//            protected void onBindViewHolder
//                    (@NonNull final ViewHolderAdviceGroup i_ViewHolder,
//                     final int i_Position, @NonNull final ChatRoom i_ActiveChatRoom)
//            {
//                Log.e("Fragment Active Chats", i_ActiveChatRoom.getSubjectName());
//                        if(mSubjectName.equals(i_ActiveChatRoom.getSubjectName())) {
//                            int matchesInTopis = countMatchesInTopics(i_ActiveChatRoom.getRoomName());
//                            if (matchesInTopis > 0) {
//                                Log.e("In Fragment Active Chats:", i_ActiveChatRoom.getCreationDate());
//                                Picasso.get().load(i_ActiveChatRoom.getImgLink()).into(i_ViewHolder.getGroupProfileImg());
//                                i_ViewHolder.setParentSubjectName(i_ActiveChatRoom.getSubjectName());
//                                i_ViewHolder.setGroupTopic(i_ActiveChatRoom.getRoomName());
//                                i_ViewHolder.setLastMessageTime(i_ActiveChatRoom.getCreationTime());
//                                i_ViewHolder.setLastMessageDate(i_ActiveChatRoom.getCreationDate());
//                            }
//                        }
//                                i_ViewHolder.setItemClickListener(new ItemClickListener() {
//                                    @Override
//                                    public void onClick(View view, int position, boolean isLongClick) {
//                                        Intent IntentChatRoom = new Intent(getContext(), ActivityChatRoom.class);
//                                        IntentChatRoom.putExtra("chat_room_id", i_ActiveChatRoom.getRoomId());
//                                        IntentChatRoom.putExtra("room_name", i_ActiveChatRoom.getRoomName());
//                                        IntentChatRoom.putExtra("subject_name", i_ActiveChatRoom.getSubjectName());
//                                        IntentChatRoom.putExtra("subject_image", i_ActiveChatRoom.getImgLink());
//                                        IntentChatRoom.putExtra("type", "observer");
//                                        startActivity(IntentChatRoom);
//                                    }
//                                });
//
//
//
//
//
//            }
//        };
//
//        mAdapter.startListening();
//        mRecyclerPossibleActive.setAdapter(mAdapter);
//    }

    private void pupolateRecyclerView(){
        Log.e("Fragment Active Chats in pupolateRecyclerView(): mOptions size: ", " " + mOptions.size());
        for(ChatRoom c : mOptions)
        {
            Log.e("Fragment Active Chats in pupolateRecyclerView(): ", c.getSubjectName());
            if(mSubjectName.equals(c.getSubjectName()))
            {
                Log.e("Fragment Active Chats in pupolateRecyclerView():: if(): ", c.getSubjectName());
                int matchesInTopis = countMatchesInTopics(c.getRoomName());
                if(matchesInTopis > 0)
                    mFilteredChatsList.add(c);
            }
        }
        mAdapter = new AdapterPossibleActiveChats(mFilteredChatsList,getContext());
        mRecyclerPossibleActive.setAdapter(mAdapter);
    }

    private int countMatchesInTopics(String i_RoomName) {
        int matches =0;
        String[] words = i_RoomName.split("\\s+");
        for(String str : words)
        {
            if(mCurrentClientTopicWords.contains(str.trim().toLowerCase()));
            {
                matches++;
            }
        }
        Log.e("In Possible Active Chats Matches: ", Integer.toString(matches));
        return matches;
    }


    private void buildPossibleActiveChatsOptions() {
        mActiveChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot room: dataSnapshot.getChildren()){
                        if(!room.child("mIsChatClosed").getValue(boolean.class))
                        {
                            mOptions.add(room.getValue(ChatRoom.class));
                        }
                    }
                    pupolateRecyclerView();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mFilteredChatsList.clear();
        mOptions.clear();
        if(mAdapter != null)
            mAdapter.clearRecyclerView();

    }
}
