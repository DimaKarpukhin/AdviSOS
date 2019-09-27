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
import com.studymobile.advisos.Adapters.AdapterPossibleClosedChats;
import com.studymobile.advisos.Interfaces.ItemClickListener;
import com.studymobile.advisos.Models.ActiveChatRoom;
import com.studymobile.advisos.Models.ChatRoom;
import com.studymobile.advisos.R;
//import com.studymobile.advisos.Services.TopicAndContentMatcher;
import com.studymobile.advisos.ViewHolders.ViewHolderAdviceGroup;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPossibleClosedChats extends Fragment {

    private View mViewPossibleClosedChats;
    private RecyclerView mRecyclerPossibleClosedchats;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mClosedChats;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mSubjectName;
    private String mRoomName;
    private AdapterPossibleClosedChats mAdapter;
    private Set<String> mCurrentClientTopicWords;
    private List<ChatRoom> mOptions;
    private DataSnapshot mClosedChatRoomIds;
    //private FirebaseRecyclerAdapter<ChatRoom, ViewHolderAdviceGroup> mAdapter;
    private List<ChatRoom> mFilteredChatRooms;


    public FragmentPossibleClosedChats(){}

    @Override
    public View onCreateView(LayoutInflater i_Inflater, ViewGroup i_Container,
                             Bundle i_SavedInstanceState)
    {
        mSubjectName = getActivity().getIntent().getStringExtra("subject_name");
        mRoomName = getActivity().getIntent().getStringExtra("topic");
        mViewPossibleClosedChats = i_Inflater.inflate(R.layout.fragment_possible_closed_chats, i_Container, false);
        mRecyclerPossibleClosedchats = mViewPossibleClosedChats.findViewById(R.id.recycler_possible_closed_chats);
        mRecyclerPossibleClosedchats.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mFilteredChatRooms = new LinkedList<>();
        mOptions = new LinkedList<>();
        mClosedChats = mDatabase.getReference().child("SubjectClosedChats");
        assignSet();
        return mViewPossibleClosedChats;
    }

    @Override
    public void onStart() {
        super.onStart();
        buildPossibleClosedChatsOptions();
//        getChatRoomKeysDataSnapShot();
//        getCahtRoomToPopulate();
//        setAdapterForRecyclerView();


    }

    private void setAdapterForRecyclerView() {

        Log.e("in Fragment Closed Chats: ","Starting method setAdapterForRecyclerView() ");
        mAdapter = new AdapterPossibleClosedChats(mFilteredChatRooms, getContext());
        mRecyclerPossibleClosedchats.setAdapter(mAdapter);
    }

    private void getChatRoomKeysDataSnapShot() {
        DatabaseReference chatRoomKeysRef = mDatabase.getReference("ChatRoomKeys");
        chatRoomKeysRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mClosedChatRoomIds = dataSnapshot;
                Log.e("in Fragment Closed Chats: ","Data Snap Shot First Child: " +
                        mClosedChatRoomIds.getChildren().iterator().next().getKey());
                getCahtRoomToPopulate();
                setAdapterForRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void assignSet() {
        String[] currentWords = mRoomName.split("\\s+");
        mCurrentClientTopicWords = new HashSet<>();
        for(String str : currentWords){
            mCurrentClientTopicWords.add(str.trim().toLowerCase());
        }
    }

    private void buildPossibleClosedChatsOptions() {
//        mOptions = new FirebaseRecyclerOptions.Builder<ChatRoom>()
//                .setQuery(mClosedChats.child(mSubjectName), ChatRoom.class)
//                .build();
        mClosedChats.child(mSubjectName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot room : dataSnapshot.getChildren())
                {
                    ChatRoom chatRoom = room.getValue(ChatRoom.class);
                    Log.e("in Fragment Closed Chats:", "Inserting Room" + chatRoom.getRoomId());
                    mOptions.add(chatRoom);
                }
                getChatRoomKeysDataSnapShot();
//                getCahtRoomToPopulate();
//                setAdapterForRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

//    private void populatePossibleClosedChatsView() {
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
//            @Override
//            protected void onBindViewHolder
//                    (@NonNull final ViewHolderAdviceGroup i_ViewHolder,
//                     final int i_Position, @NonNull final ChatRoom i_ClosedChatRoom)
//            {
//                //Log.e("In Fragment Closed Chats", i_ClosedChatRoom.getImgLink());
//                DatabaseReference chatRoomKeysRef = mDatabase.getReference("ChatRoomKeys");
//                chatRoomKeysRef.child(i_ClosedChatRoom.getRoomId())
//                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot i_DataSnapshot) {
//                                Log.e("in Fragment Closed Chats:", i_ClosedChatRoom.getCreationDate());
//                                List<String> keys = new LinkedList<>();
//                                for (DataSnapshot entry : i_DataSnapshot.child("mKeyWords").getChildren()) {
//                                    keys.add(entry.getKey().trim().toLowerCase());
//                                }
//                                int keyMatch = countMatchesOfTopicAndKeys(keys);
//                                if (keyMatch > 0)
//                                {
//                                    Picasso.get().load(i_ClosedChatRoom.getImgLink()).into(i_ViewHolder.getGroupProfileImg());
//                                i_ViewHolder.setParentSubjectName(i_ClosedChatRoom.getSubjectName());
//                                i_ViewHolder.setGroupTopic(i_ClosedChatRoom.getRoomName());
//                                i_ViewHolder.setLastMessageTime(i_ClosedChatRoom.getCreationTime());
//                                i_ViewHolder.setLastMessageDate(i_ClosedChatRoom.getCreationDate());
//                            }
//
//                                 i_ViewHolder.setItemClickListener(new ItemClickListener() {
//                                    @Override
//                                    public void onClick(View view, int position, boolean isLongClick) {
//                                        Intent IntentChatRoom = new Intent(getContext(), ActivityChatRoom.class);
//                                        IntentChatRoom.putExtra("chat_room_id", i_ClosedChatRoom.getRoomId());
//                                        IntentChatRoom.putExtra("room_name", i_ClosedChatRoom.getRoomName());
//                                        IntentChatRoom.putExtra("subject_name", i_ClosedChatRoom.getSubjectName());
//                                        IntentChatRoom.putExtra("subject_image", i_ClosedChatRoom.getImgLink());
//                                        startActivity(IntentChatRoom);
//                                    }
//                                });
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError i_DataSnapshot) {
//
//                            }
//                        });
//
//            }
//        };
//
//        mAdapter.startListening();
//        mRecyclerPossibleClosedchats.setAdapter(mAdapter);
//    }

    private void getCahtRoomToPopulate(){
        Log.e("in Fragment Closed Chats: ","Starting method getCahtRoomToPopulate() ");
        for(ChatRoom c : mOptions) {
            Log.e("in Fragment Closed Chats:", "Getting in loop with:" + c.getRoomId());
            populateRecyclerView(c);
        }
//        mAdapter = new AdapterPossibleClosedChats(mFilteredChatRooms, getContext());
//        mRecyclerPossibleClosedchats.setAdapter(mAdapter);
    }
    private void populateRecyclerView(ChatRoom c){
             Log.e("in Fragment Closed Chats:", "Getting in loop with:" + c.getRoomId());

                             List<String> keys = new LinkedList<>();
                             for (DataSnapshot entry : mClosedChatRoomIds.child(c.getRoomId()).child("mKeyWords").getChildren()) {
                                 Log.e("One Of Keys:", entry.getKey());
                                 keys.add(entry.getKey().trim().toLowerCase());
                             }
                             //Log.e("One Of Keys:", keys.get(0));
                             int keyMatch = countMatchesOfTopicAndKeys(keys);
                             if(keyMatch > 0){
                                mFilteredChatRooms.add(c);
                                 Log.e("in Fragment Closed Chats:", c.getCreationDate());
                             }


    }

    private int countMatchesOfTopicAndKeys(List<String> keys) {
        int matches =0;
        for(String key : keys)
        {
            if(mCurrentClientTopicWords.contains(key))
            {
                matches++;
            }
        }
        Log.e("In Possible Closed Chats Matches: ", Integer.toString(matches));
        return matches;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFilteredChatRooms.clear();
        mOptions.clear();
        if(mAdapter != null)
           mAdapter.clearRecyclerView();

    }
}
