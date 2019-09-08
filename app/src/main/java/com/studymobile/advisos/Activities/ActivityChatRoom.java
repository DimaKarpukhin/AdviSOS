package com.studymobile.advisos.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.studymobile.advisos.Models.ChatMessage;
import com.studymobile.advisos.Models.ChatRoom;
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderRecievedMessageHolder;
import com.studymobile.advisos.ViewHolders.ViewHolderSentMessageHolder;

import java.util.Date;


public class ActivityChatRoom extends AppCompatActivity {

    private String link;
    private static final int LAYOUT_SENDER = 1;
    private static final int LAYOUT_RECIEVER = 2;
    private RecyclerView mMessagesRecyclerView;
    private Button mSendMessageButton;
    private EditText mMessageBodyText;
    private String senderName;
    private Button mCloseChatButton;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mChatRoonIdRefMessages;
    private FirebaseAuth mAuth;
    private String mChatRoomId;
    private FirebaseUser mCurrentUser;
    private FirebaseStorage mStorage;
    private ViewHolderRecievedMessageHolder mImgLInk; //?
    private FirebaseRecyclerOptions<ChatMessage> mOptions;
    private FirebaseRecyclerAdapter<ChatMessage, RecyclerView.ViewHolder> mMessagesAdapterd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        init();
        setFirebaseReferences();
        showButtonCloseChatForChatRoomCreator();

    }

    private void showButtonCloseChatForChatRoomCreator() {
        DatabaseReference reference = mDatabase.getReference("ChatRooms").child(mChatRoomId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("mChatRoomCreatorUid").getValue(String.class).equals(mCurrentUser.getUid()))
                    {
                        mCloseChatButton.setCursorVisible(true);
                        mCloseChatButton.setClickable(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void init() {
        mChatRoomId = getIntent().getStringExtra("chat_room_id");
        mCloseChatButton = findViewById(R.id.button_close_chat);
        mSendMessageButton = findViewById(R.id.button_chatbox_send);
        mMessageBodyText = findViewById(R.id.edittext_chatbox);
        mMessagesRecyclerView = findViewById(R.id.reyclerview_chat_messages);
        mMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mCloseChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeChatButtonPressed();
            }
        });
        mSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void closeChatButtonPressed() {
        //TODO do the logic of closing the chat and rate the users,
        //TODO need to check how closinf the chat affects other user since the chat is not active
        //how do we notify other participents that the chat is closed to not load it again next time?
        //maybe need to set an childevent listener on chatRooms? or active chats?


    }

    private void sendMessage() {
        String messageText = mMessageBodyText.getText().toString();
        if(TextUtils.isEmpty(messageText))
        {
            Toast.makeText(this,R.string.message_edit_text_toast,Toast.LENGTH_SHORT);
        }
        else{
            Calendar cal = Calendar.getInstance();
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("HH:mm");
            String localTime = date.format(currentLocalTime);
            String senderID = mCurrentUser.getUid();
            DatabaseReference reference = mDatabase.getReference("Users");
            reference.child(senderID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("firstName").getValue(String.class);
                    senderName = name;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            mChatRoonIdRefMessages.push().setValue(new ChatMessage(messageText,senderName,localTime,senderID));

        }

    }

    private void setFirebaseReferences() {
        mDatabase = FirebaseDatabase.getInstance();
        mChatRoonIdRefMessages = mDatabase.getReference().child("Messages").child(mChatRoomId);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        buildMessagesList();
        populateMessagesOnREcyclerView();

    }

    private void populateMessagesOnREcyclerView() {
        mMessagesAdapterd = new FirebaseRecyclerAdapter<ChatMessage,RecyclerView.ViewHolder >(mOptions){
            @Override
            public int getItemViewType(int position) {
                ChatMessage message = getItem(position);
                if(message.getmMessageSenderID().equals(mCurrentUser.getUid()))
                {
                    return LAYOUT_SENDER;
                }
                else{
                    return LAYOUT_RECIEVER;
                }

            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view;
                if(viewType == LAYOUT_SENDER){
                     view = LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.item_message_sent, parent, false);
                    return new ViewHolderSentMessageHolder(view);
                }
                else{
                     view = LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.item_message_recived, parent, false);
                    return new ViewHolderSentMessageHolder(view);
                }
            }

            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i, @NonNull ChatMessage chatMessage) {
                if(viewHolder instanceof ViewHolderSentMessageHolder )
                {
                    ((ViewHolderSentMessageHolder) viewHolder).setMessageText(chatMessage.getMessageBody());
                    ((ViewHolderSentMessageHolder) viewHolder).setTimeText(chatMessage.getTimeSent());
                }
                if(viewHolder instanceof ViewHolderRecievedMessageHolder)
                {
                    ((ViewHolderRecievedMessageHolder)viewHolder).setMessageText(chatMessage.getMessageBody());
                    ((ViewHolderRecievedMessageHolder)viewHolder).setNameText(chatMessage.getSenderName());
                    ((ViewHolderRecievedMessageHolder)viewHolder).setTimeText(chatMessage.getTimeSent());
                    DatabaseReference reference = mDatabase.getReference("Users");
                    reference.child(chatMessage.getmMessageSenderID()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            link = dataSnapshot.child("imgLink").getValue(String.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Picasso.get().load(link).into(((ViewHolderRecievedMessageHolder) viewHolder).getProfileImage());
                }
            }
        };
        mMessagesAdapterd.startListening();
        mMessagesRecyclerView.setAdapter(mMessagesAdapterd);

    }

    private void buildMessagesList() {

        mOptions = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(mChatRoonIdRefMessages, ChatMessage.class)
                .build();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMessagesAdapterd.stopListening();
    }
}
