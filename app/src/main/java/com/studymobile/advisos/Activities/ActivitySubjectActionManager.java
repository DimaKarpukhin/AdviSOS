package com.studymobile.advisos.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.studymobile.advisos.Models.ActiveChatRoom;
import com.studymobile.advisos.Models.ChatRequest;
import com.studymobile.advisos.Models.ChatRoom;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.Models.User;
import com.studymobile.advisos.Models.UserLocation;
import com.studymobile.advisos.R;
import com.studymobile.advisos.Services.DatabaseServices;
import com.studymobile.advisos.Services.FileUtil;
import com.studymobile.advisos.notification.FCMNotification;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ActivitySubjectActionManager extends AppCompatActivity implements View.OnClickListener
{
    private static final String RES = "android.resource://";
    private static final String DRAWABLE_DEFAULT = "/drawable/img_advisos";
    private static final String DEFAULT = "Default";
    private static final String SUBJECT_NAME = "subject_name";
    private static final int IMG_REQ = 1;
    private static final int REQ_CODE = 2;

    private EditText mFieldTopic;
    private TextView mBtnAskAdvice;
    private ImageButton mBtnEdit;
    private ImageButton mBtnRestore;
    private ImageButton mBtnBack;
    private ImageView mImgSubject;
    private TextView mTxtSubjectName;
    private TextView mTxtSubjectDescription;
    private Subject mCurrentSubject;
    private String mSubjectName;
    private String mImgLinkForChatIntent;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseServices mDatabaseServices;
    private UserLocation mUserLocation;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mSubjectsRef;
    private  FirebaseStorage mStorage;

    DatabaseReference mChatRoomsRef;
    String mChatRoomUId;
    List<String> mExpertsList;

    private Dialog m_PopupDialog;
    private Uri m_DialogImgURI;
    private CircleImageView m_DialogImgView;

    private File m_PickedImage;
    private  File m_CompressedImage;
    private boolean m_IsImgPicked = false;

    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_subject_action_manager);
        setFirebaseData();
        setActivityContent();
        getUserLocation();
        setPopupDialog();

        if(getIntent() != null)
        {
            mSubjectName = getIntent().getStringExtra(SUBJECT_NAME);
        }
        if(!mSubjectName.isEmpty())
        {
            mDatabaseServices = new DatabaseServices(mSubjectName, mUserLocation);
            mDatabaseServices.CollectExperts();
            loadSubjectDetails(mSubjectName);
        }else {
            //Toast.makeText(this, "ERROR: There is no instance in database" ,Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(android.R.id.content),
                    "ERROR: There is no instance in database", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void getUserLocation()
    {
        mDatabase.getReference("Users").child(mCurrentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot) {
                        mUserLocation = i_DataSnapshot.getValue(UserLocation.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View i_View)
    {
        int id = i_View.getId();

        if (id == mBtnEdit.getId())
        {
            showPopupDialog();
        }
        else if (id == mBtnBack.getId())
        {
            startHomeScreenActivity();
        }
        else if (id == mBtnAskAdvice.getId())
        {
            if(mFieldTopic.getText().toString().isEmpty())
            {
                mFieldTopic.setError("The field is required");
            }
            else {
                createChatRoomActivity(mFieldTopic.getText().toString());
                //checkIfTopicExists();
            }

        }
    }

    private void setPopupDialog()
    {
        m_PopupDialog = new Dialog(ActivitySubjectActionManager.this);
        m_PopupDialog.setContentView(R.layout.dialog_image);
        m_PopupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        m_DialogImgView = m_PopupDialog.findViewById(R.id.img_of_dialog_profile_picture);
    }

    private void showPopupDialog()
    {
        m_PopupDialog.setCanceledOnTouchOutside(false);
        m_PopupDialog.findViewById(R.id.txt_title_of_dialog_image)
                .setVisibility(View.INVISIBLE);
        m_PopupDialog.findViewById(R.id.layout_optional_of_dialog_image)
                .setVisibility(View.VISIBLE);
        m_PopupDialog.findViewById(R.id.btn_ok_of_dialog_profile_picture)
                .setVisibility(View.VISIBLE);
        m_PopupDialog.findViewById(R.id.btn_remove_of_dialog_profile_picture)
                .setVisibility(View.VISIBLE);
        m_PopupDialog.findViewById(R.id.btn_add_a_photo_of_dialog_profile_picture)
                .setVisibility(View.VISIBLE);
        Picasso.get().load(mCurrentSubject.getImgLink()).into(m_DialogImgView);

        m_PopupDialog.findViewById(R.id.btn_add_a_photo_of_dialog_profile_picture)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addImage();
                    }
                });
        m_PopupDialog.findViewById(R.id.btn_ok_of_dialog_profile_picture)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        m_PopupDialog.dismiss();
                        if(m_IsImgPicked)
                        {
                            mImgSubject.setImageURI(m_DialogImgURI);
                            mCurrentSubject.setImgLink(m_DialogImgURI.toString());
                            uploadImgToStorage(mCurrentSubject.getSubjectName());
                        }
                    }
                });
        m_PopupDialog.findViewById(R.id.btn_remove_of_dialog_profile_picture)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        confirmRemoving();
                    }
                });
        m_PopupDialog.findViewById(R.id.btn_close_of_dialog_profile_picture)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        m_PopupDialog.dismiss();
                    }
                });

        m_PopupDialog.show();
    }

    private void uploadImgToStorage(String i_Image)
    {
        StorageReference imagePath = mStorage.getReference().child("Images/Subjects");

        final StorageReference imageRef = imagePath.child(i_Image);

        imageRef.putFile(m_DialogImgURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                m_DialogImgURI = uri;
                                mCurrentSubject.setImgLink(String.valueOf(uri));
                                updateDatabase();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ActivitySubjectActionManager.this,
                            "ERROR$:\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void updateDatabase()
    {
         mDatabase.getReference("Subjects")
                .child(mCurrentSubject.getSubjectName())
                .child("imgLink").setValue(mCurrentSubject.getImgLink());
    }

    private void confirmRemoving()
    {
        final Dialog confirmDialog = new Dialog(ActivitySubjectActionManager.this);
        confirmDialog.setContentView(R.layout.dialog_confirm);
        String title = "Remove photo?";
        String rightBtnTxt = "Remove";
        String leftBtnTxt = "Cancel";
        ImageButton closeBtn = confirmDialog.findViewById(R.id.btn_close_of_dialog_confirm);
        TextView fieldTitle = confirmDialog.findViewById(R.id.txt_title_of_dialog_confirm);
        TextView rightBtn = confirmDialog.findViewById(R.id.btn_right_of_dialog_confirm);
        TextView leftBtn = confirmDialog.findViewById(R.id.btn_left_of_dialog_confirm);

        closeBtn.setVisibility(View.INVISIBLE);
        fieldTitle.setText(title);
        rightBtn.setText(rightBtnTxt);
        leftBtn.setText(leftBtnTxt);

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                m_DialogImgURI = Uri.parse(RES + getApplicationContext()
                        .getPackageName() + DRAWABLE_DEFAULT);
                mImgSubject.setImageURI(m_DialogImgURI);
                mCurrentSubject.setImgLink(m_DialogImgURI.toString());
                m_DialogImgView.setImageURI(m_DialogImgURI);
                uploadImgToStorage(DEFAULT);
                m_IsImgPicked = false;
            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });

        confirmDialog.show();
    }

    private void addImage()
    {
        if(Build.VERSION.SDK_INT >= 24)
        {
            requestPermission();
        }
        else
        {
            openGallery();
        }
    }

    private void requestPermission()
    {
        if(ContextCompat.checkSelfPermission(ActivitySubjectActionManager.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    ActivitySubjectActionManager.this, Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(ActivitySubjectActionManager.this,
                        "Please accept for required permission ", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(ActivitySubjectActionManager.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_CODE);
            }
        }
        else
        {
            openGallery();
        }
    }

    private void openGallery()
    {
        Intent IntentGallery =new Intent(Intent.ACTION_GET_CONTENT);
        IntentGallery.setType("image/*");
        startActivityForResult(IntentGallery,IMG_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == IMG_REQ && data != null)
        {
            try {
                m_PickedImage = FileUtil.from(this, data.getData());
                m_CompressedImage = new Compressor(this).compressToFile(m_PickedImage);
            }
            catch (IOException e){
                e.printStackTrace();
            }
            m_DialogImgURI = Uri.fromFile(m_CompressedImage);
            m_DialogImgView.setImageURI(m_DialogImgURI);
            m_IsImgPicked = true;
        }
    }


//    private void checkIfTopicExists()
//    {
//        final Dialog confirmDialog = new Dialog(ActivitySubjectActionManager.this);
//        confirmDialog.setContentView(R.layout.dialog_confirm);
//
//        String title = "Similar topics were discussed earlier";
//        String startNewBtnTxt = "Start new";
//        String viewSimilarBtnTxt = "View similar";
//
//        ImageButton closeBtn = confirmDialog.findViewById(R.id.btn_close_of_dialog_confirm);
//        TextView fieldTitle = confirmDialog.findViewById(R.id.txt_title_of_dialog_confirm);
//        TextView startNewBtn = confirmDialog.findViewById(R.id.btn_right_of_dialog_confirm);
//        TextView viewSimilarBtn = confirmDialog.findViewById(R.id.btn_left_of_dialog_confirm);
//
//        closeBtn.setVisibility(View.VISIBLE);
//        fieldTitle.setText(title);
//        startNewBtn.setText(startNewBtnTxt);
//        viewSimilarBtn.setText(viewSimilarBtnTxt);
//
//        startNewBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //startCreateChatRoomActivity();
//                        createChatRoomActivity(mFieldTopic.getText().toString());
//                    }
//                });
//        viewSimilarBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
//        closeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                confirmDialog.dismiss();
//            }
//        });
//
//        confirmDialog.show();
//    }

    private void setFirebaseData()
    {
        mDatabase = FirebaseDatabase.getInstance();
        mSubjectsRef = mDatabase.getReference("Subjects");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance();
    }

    private void setActivityContent()
    {
        mFieldTopic = findViewById(R.id.field_topic_action_manager);
        mImgSubject = findViewById(R.id.img_subject_of_subject_action_manager);
        mTxtSubjectName = findViewById(R.id.txt_subject_name_of_subject_action_manager);
        mTxtSubjectDescription = findViewById(R.id.txt_subj_description_of_subject_action_manager);

        mBtnBack = findViewById(R.id.btn_back_of_subject_action_manager);
        mBtnEdit = findViewById(R.id.btn_edit_of_subject_action_manager);
        mBtnAskAdvice = findViewById(R.id.btn_ask_for_advice);

        mBtnBack.setOnClickListener(this);
        mBtnEdit.setOnClickListener(this);
        mBtnAskAdvice.setOnClickListener(this);

    }

    private void loadSubjectDetails(String i_SubjectName)
    {
        mDatabase.getReference("Subjects").child(i_SubjectName)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot) {
                        mCurrentSubject = i_DataSnapshot.getValue(Subject.class);
                        Picasso.get().load(mCurrentSubject.getImgLink()).into(mImgSubject);
                        mTxtSubjectName.setText(mCurrentSubject.getSubjectName());
                        mTxtSubjectDescription.setText(mCurrentSubject.getSubjectDescription());
                        mImgLinkForChatIntent= mCurrentSubject.getImgLink();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void startHomeScreenActivity()
    {
        Intent IntentHomeScreen = new Intent
                (ActivitySubjectActionManager.this, ActivityHomeScreen.class);
        startActivity(IntentHomeScreen);
    }

    //===================================================
    private void createChatRoomActivity(String i_roomName)
    {
        int subjectPopularity = mCurrentSubject.getPopularity();
        subjectPopularity--;
        mDatabase.getReference("Subjects")
                .child(mCurrentSubject.getSubjectName())
                .child("popularity").setValue(subjectPopularity);

        mChatRoomsRef = mDatabase.getReference("ChatRooms");
        mChatRoomUId = mChatRoomsRef.push().getKey();//push new chat room id and get UId
        Pair<String, String> date = getCreationDateAndTime();
        String creatorID = mCurrentUser.getUid();

        ChatRoom chatRoom = new ChatRoom
                (mChatRoomUId, i_roomName,
                date.first, date.second,
                mSubjectName,creatorID,
                mCurrentSubject.getImgLink(), 1);

        mChatRoomsRef.child(mChatRoomUId).setValue(chatRoom);//add chat room information under room id

        ActiveChatRoom activeChatRoom = new ActiveChatRoom
                (mChatRoomUId, creatorID,true);
        mDatabase.getReference("ActiveChats").child(creatorID)
                .child(mChatRoomUId).setValue(activeChatRoom);

        mDatabase.getReference("Participants").child(mChatRoomUId)
                .child(creatorID).setValue(creatorID);//add the room id to chat participants node
        mDatabase.getReference("Messages").child(mChatRoomUId).setValue(mChatRoomUId);

        mExpertsList =  mDatabaseServices.GetExpertsIDs();
        pushRequestsToDB();


        if( mExpertsList.isEmpty() )
        {
            Toast.makeText(ActivitySubjectActionManager.this,
                    "Nobody is available to chat now", Toast.LENGTH_SHORT).show();
        }
        else
        {
            pushNotify(mExpertsList,  "Need your advice about: " +
                    mSubjectName, "Tap to view the details");
            addRequestedUsersToDB();
            Intent intent = new Intent(this, ActivityChatRoom.class);
            intent.putExtra("chat_room_id", mChatRoomUId);
            intent.putExtra("room_name",i_roomName);
            intent.putExtra("subject_name", mSubjectName);
            intent.putExtra("subject_image",mImgLinkForChatIntent);
            this.startActivity(intent);
        }


    }

    private void addRequestedUsersToDB()
    {
        DatabaseReference requestedUsers = mDatabase
                .getReference("RequestedChatRoomUsers").child(mChatRoomUId);
        for(String expertID: mExpertsList)
        {
            requestedUsers.child(expertID).setValue(expertID);
        }
    }

    private void pushRequestsToDB()
    {
        mDatabase.getReference("Users").child(mCurrentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                    {
                       if(i_DataSnapshot.exists())
                       {
                           String creatorId = mCurrentUser.getUid();
                           String creatorName = i_DataSnapshot.child("firstName").getValue(String.class)
                                   + " " + i_DataSnapshot.child("familyName").getValue(String.class);
                           String creatorImgLink  = i_DataSnapshot.child("imgLink").getValue(String.class);

                           for(String expertId : mExpertsList)
                           {
                               ChatRequest chatRequest = new ChatRequest();
                               chatRequest.setRequestedUserId(expertId);
                               chatRequest.setChatCreatorId(creatorId);
                               chatRequest.setChatCreatorName(creatorName);
                               chatRequest.setChatCreatorImgLink(creatorImgLink);
                               chatRequest.setChatRoomId(mChatRoomUId);
                               chatRequest.setChatRoomName(mSubjectName);
                               chatRequest.setTopic(mFieldTopic.getText().toString());
                               chatRequest.setSubjectImgLink(mCurrentSubject.getImgLink());


                               String requestID = mDatabase.getReference
                                       ("ChatRequests").child(expertId).push().getKey();
                               chatRequest.setRequestId(requestID);

                               mDatabase.getReference("ChatRequests")
                                       .child(expertId).child(requestID).setValue(chatRequest);
                           }
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError i_DataSnapshot) {

                    }
                });
    }

    private Pair<String,String> getCreationDateAndTime()
    {
        android.icu.util.Calendar calendar = Calendar.getInstance();
        Date currentLocalDateTime = calendar.getTime();
        android.icu.text.DateFormat time = new android.icu.text.SimpleDateFormat("HH:mm");
        String localTime = time.format(currentLocalDateTime);
        DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = date.format(currentLocalDateTime);
        return new Pair<>(currentDate,localTime);
    }


    void pushNotify(List<String> i_ExpertsIDs,  final String i_Title, final String i_Body)
    {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");

        for (String expertId : i_ExpertsIDs)
        {
            dbRef.child(expertId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    try
                    {
                        Log.e("pushing to " + user.getFirstName() + " " + user.getFamilyName(), user.getDeviceToken());
                        FCMNotification.pushFCMNotification(user.getDeviceToken(), i_Title , i_Body);
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
