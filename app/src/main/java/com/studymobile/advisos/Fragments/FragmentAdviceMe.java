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
import com.studymobile.advisos.Interfaces.ItemClickListener;
import com.studymobile.advisos.Models.ChatRoom;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderAdviceGroup;
import com.studymobile.advisos.ViewHolders.ViewHolderSubject;

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
    private String mSubjectID;

    private FirebaseRecyclerOptions<ChatRoom> mOptions;
    private FirebaseRecyclerAdapter<ChatRoom, ViewHolderAdviceGroup> mAdapter;

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

    private void buildAdviceMeOptions()
    {
        mOptions = new FirebaseRecyclerOptions.Builder<ChatRoom>()
                .setQuery(mAdviceMeRef, ChatRoom.class)
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
            protected void onBindViewHolder(@NonNull ViewHolderAdviceGroup i_ViewHolder, int i_Position,
                                            @NonNull final ChatRoom i_ChatRoom) {
                i_ViewHolder.getBtnUnreadMessages().setVisibility(View.VISIBLE);
                i_ViewHolder.setParentSubjectName(i_ChatRoom.getSubjectName());
                i_ViewHolder.setGroupTopic(i_ChatRoom.getRoomName());
                i_ViewHolder.setLastMessageTime(i_ChatRoom.getCreationTime());
                Picasso.get().load(i_ChatRoom.getImgLink()).into(i_ViewHolder.getGroupProfileImg());

                i_ViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };

        mAdapter.startListening();
        mAdviceMeList.setAdapter(mAdapter);
    }

}
