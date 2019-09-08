package com.studymobile.advisos.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.studymobile.advisos.Models.ChatRoom;
import com.studymobile.advisos.Models.User;
import com.studymobile.advisos.R;
import com.studymobile.advisos.Services.CollectExpertsForChatRoom;
import com.studymobile.advisos.notification.FCMNotification;

import java.util.Date;
import java.util.List;

public class ActivityCreateChatRoom extends AppCompatActivity {

    private TextView mTextViewCreateChatRoomInfo;
    private Button mButtonCreateChatRoom;
    private EditText mEditTextRoomName;
    private String mSubjectName;
    private FirebaseUser m_CurrentUser;
    private FirebaseAuth m_Auth;
    private FirebaseDatabase m_Database;
    private String mUserFirstName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat_room);

        init();
        setFirebaseData();
        setTextViewInformation();


    }

    private void setTextViewInformation() {

        DatabaseReference reference = m_Database.getReference("Users");
        final String info = getResources().getString(R.string.create_room_information);
        reference.child(m_CurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("firstName").getValue(String.class);
                mUserFirstName = name;
                mTextViewCreateChatRoomInfo.setText(name + ",\n" + info);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void init() {
        mTextViewCreateChatRoomInfo = findViewById(R.id.textView_room_name_information);
        mEditTextRoomName = findViewById(R.id.edittext_enter_room_name);
        mButtonCreateChatRoom = findViewById(R.id.button_create_chat_room);
        mSubjectName = getIntent().getStringExtra("subject_name"); //TODO deliver to me the subject Uid that the user creating the chat with from the activity
        mButtonCreateChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickOperation();
            }
        });
    }

    private void setFirebaseData() {
        m_Auth = FirebaseAuth.getInstance();
        m_CurrentUser = m_Auth.getCurrentUser();
        m_Database = FirebaseDatabase.getInstance();
    }
    private void onClickOperation(){
        String roomName = mEditTextRoomName.getText().toString();
        if(roomName.isEmpty())
        {
            Context context = getApplicationContext();
            String text = getResources().getString(R.string.empty_room_name);
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
            toast.show();
            mEditTextRoomName.clearComposingText();

        }
        else {
            showConfirmationDialodForRoomName(roomName);
        }
    }
    private void showConfirmationDialodForRoomName(final String i_roomName)
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.hello) + mUserFirstName +",");
        builder.setMessage(getResources().getString(R.string.confirm_room_name) +i_roomName + "?/n");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                createChatRoomActivity(i_roomName);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mEditTextRoomName.clearComposingText();

            }
        });

    }
    private void createChatRoomActivity(String i_roomName)
    {
       Runnable collectChatUser = new CollectExpertsForChatRoom(mSubjectName);
       new Thread(collectChatUser).start();
       DatabaseReference chatRoomsRef = m_Database.getReference("ChatRooms");
       String chatRoomUId = chatRoomsRef.push().getKey();//push new chat room id and get UId
       Pair<String, String> date = getCreationDateAndTime();
       String userID = m_CurrentUser.getUid();
       ChatRoom chatRoom = new ChatRoom(chatRoomUId,i_roomName,date.first,date.second,mSubjectName
       ,userID);
       chatRoomsRef.child(chatRoomUId).setValue(chatRoom);// add chat room information under room id
       m_Database.getReference("ActiveChats").child(userID).setValue(chatRoomUId);//add the room id to users active chats
       m_Database.getReference("Participants").setValue(chatRoomUId); // add  the room id to chat participants node
       m_Database.getReference("Participants").child(chatRoomUId).setValue(userID); // add the user as a participant as he created it
        try
        {
            this.wait();
            if(collectChatUser instanceof CollectExpertsForChatRoom)
            {
                CollectExpertsForChatRoom collector = (CollectExpertsForChatRoom)collectChatUser;
                pushNotify(collector.getmExpertUserOfSubjectSelectedId());
            }
        }
        catch (InterruptedException e){

        }
        Intent intent = new Intent(this, ActivityChatRoom.class);
        intent.putExtra("chat_room_id", chatRoomUId);
        Toast.makeText(getApplicationContext(),R.string.chat_room_created_success,Toast.LENGTH_SHORT);
        this.startActivity(intent);
    }

    private Pair<String,String> getCreationDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        Date currentLocalDateTime = calendar.getTime();
        DateFormat time = new SimpleDateFormat("HH:mm");
        String localTime = time.format(currentLocalDateTime);
        DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = date.format(currentLocalDateTime);
        return new Pair<>(currentDate,localTime);
    }


    void pushNotify(List<String> i_expertUserOfSubjectSelectedId)
    {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");

        for (String userID : i_expertUserOfSubjectSelectedId)
        {
            dbRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    try
                    {
                        FCMNotification.pushFCMNotification(user.getDeviceToken(), "title" , "body");
                    }
                    catch (Exception e)
                    {
                        Log.d("Error", "push notification error");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}
