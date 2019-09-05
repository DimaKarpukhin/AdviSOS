package com.studymobile.advisos.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.studymobile.advisos.R;
import com.studymobile.advisos.Services.CollectExpertsForChatRoom;

public class ActivityCreateChatRoom extends AppCompatActivity {

    private TextView mTextViewCreateChatRoomInfo;
    private Button mButtonCreateChatRoom;
    private EditText mEditTextRoomName;
    private  String mSubjectUid;
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
        mSubjectUid = getIntent().getStringExtra("subject_id"); //TODO deliver to me the subject Uid that the user creating the chat with from the activity
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
    private void showConfirmationDialodForRoomName(String i_roomName)
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.hello) + mUserFirstName +",");
        builder.setMessage(getResources().getString(R.string.confirm_room_name) +i_roomName + "?/n");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                createChatRoomActivity();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mEditTextRoomName.clearComposingText();

            }
        });

    }
    private void createChatRoomActivity()
    {
       Runnable collectChatUser = new CollectExpertsForChatRoom(mSubjectUid);
       new Thread(collectChatUser).start();

       


    }






}
