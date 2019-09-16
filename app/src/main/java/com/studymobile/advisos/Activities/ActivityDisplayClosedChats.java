package com.studymobile.advisos.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderAdviceGroup;

public class ActivityDisplayClosedChats extends AppCompatActivity {

    private RecyclerView mClosedChatsRecyclerView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mClosedChats;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private FirebaseRecyclerOptions<ActiveChatRoom> mOptions;
    private FirebaseRecyclerAdapter<ActiveChatRoom, ViewHolderAdviceGroup> mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_closed_chats);
        init();
        setFirebaseData();
    }

    private void init() {

        Toolbar toolbar = findViewById(R.id.toolbar_of_closed_chats);
        setSupportActionBar(toolbar);
        toolbar.getContentInsetLeft();

        mClosedChatsRecyclerView = findViewById(R.id.recycler_closed_chats);
        mClosedChatsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void setFirebaseData() {
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mClosedChats = mDatabase.getReference().child("ClosedChats");
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
                .setQuery(mClosedChats.child(mCurrentUser.getUid())
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
                                final ChatRoom chatRoom = i_DataSnapshot.getValue(ChatRoom.class);
                                Picasso.get().load(chatRoom.getImgLink()).into(i_ViewHolder.getGroupProfileImg());
                                i_ViewHolder.setParentSubjectName(chatRoom.getSubjectName());
                                i_ViewHolder.setGroupTopic(chatRoom.getRoomName());
                                i_ViewHolder.setLastMessageTime(chatRoom.getCreationTime());
                                i_ViewHolder.setLastMessageDate(chatRoom.getCreationDate());

                                i_ViewHolder.setItemClickListener(new ItemClickListener() {
                                    @Override
                                    public void onClick(View view, int position, boolean isLongClick)
                                    {
                                        if(isLongClick)
                                        {
                                            deleteClosedChatRoom(chatRoom);
                                        }
                                        else{
                                            Intent intent = new Intent(getApplicationContext(), ActivityChatRoom.class);
                                            intent.putExtra("chat_room_id", chatRoom.getRoomId());
                                            intent.putExtra("room_name",chatRoom.getRoomName());
                                            intent.putExtra("subject_image",chatRoom.getImgLink());
                                            startActivity(intent);
                                        }
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
        mClosedChatsRecyclerView.setAdapter(mAdapter);
    }

    private void deleteClosedChatRoom(ChatRoom roomId) {
        mClosedChats.child(roomId.getCreatorId()).child(roomId.getRoomId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),"Chat room deleted",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}

