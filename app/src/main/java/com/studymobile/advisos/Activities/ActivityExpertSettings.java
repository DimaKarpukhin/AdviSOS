package com.studymobile.advisos.Activities;

import android.Manifest;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.studymobile.advisos.Enums.eWeekDay;
import com.studymobile.advisos.Interfaces.ItemClickListener;
import com.studymobile.advisos.Models.Day;
import com.studymobile.advisos.Models.Rating;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.Models.SubjectUser;
import com.studymobile.advisos.Models.User;
import com.studymobile.advisos.Models.UserAvailability;
import com.studymobile.advisos.Models.Week;
import com.studymobile.advisos.R;
import com.studymobile.advisos.Services.InputValidation;
import com.studymobile.advisos.ViewHolders.ViewHolderSubject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class ActivityExpertSettings extends AppCompatActivity implements
        View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        TextWatcher, MaterialSearchBar.OnSearchActionListener
{
    private static final String RES = "android.resource://";
    private static final String DRAWABLE_DEFAULT = "/drawable/img_advisos";
    private static final String DEFAULT = "Default";
    private static final int IMG_REQ = 1;
    private static final int REQ_CODE = 2;

    private FirebaseAuth m_Auth;
    private FirebaseUser m_CurrentUser;
    private FirebaseDatabase m_Database;
    private FirebaseStorage m_Storage;
    private FirebaseRecyclerOptions<Subject> m_Options;
    private FirebaseRecyclerAdapter<Subject, ViewHolderSubject> m_Adapter;

    private Dialog m_DialogCreateSubj;
    private Uri m_DialogImgURI;
    private CircleImageView m_DialogImgView;
    private EditText m_FieldSubjName;
    private EditText m_FieldSubjDescription;
    private boolean m_IsImgPicked = false;
    private boolean m_IsImgStored = false;

    private Dialog m_DialogSubjList;
    private RecyclerView m_DialogRecyclerView;
    private ImageButton m_FabCreateSubj;
    private List<String> m_SuggestionsList;
    private HashMap<String, Boolean> m_SubjStateMap = new HashMap<>();
    private Set<String> m_SubjNamesSet = new HashSet<String>();
    private SparseBooleanArray m_SubjStateArray = new SparseBooleanArray();
    private MaterialSearchBar m_SearchBar;

    private TextView m_BtnSubjects;
    private FloatingActionButton m_FabNext;
    private Switch m_SwitchAlwaysAvailable,m_SwitchNotDisturb;

    private Button m_SwitchSunday, m_SwitchMonday, m_SwitchTuesday,
            m_SwitchWednesday, m_SwitchThursday, m_SwitchFriday, m_SwitchSaturday;

    private Button m_BtnStartTimeSunday, m_BtnStartTimeMonday, m_BtnStartTimeTuesday,
            m_BtnStartTimeWednesday,  m_BtnStartTimeThursday, m_BtnStartTimeFriday, m_BtnStartTimeSaturday;

    private Button m_BtnEndTimeSunday, m_BtnEndTimeMonday, m_BtnEndTimeTuesday,
            m_BtnEndTimeWednesday, m_BtnEndTimeThursday, m_BtnEndTimeFriday, m_BtnEndTimeSaturday;

    private TextView m_TxtStartTimeSunday, m_TxtStartTimeMonday, m_TxtStartTimeTuesday,
            m_TxtStartTimeWednesday, m_TxtStartTimeThursday, m_TxtStartTimeFriday, m_TxtStartTimeSaturday;

    private TextView m_TxtEndTimeSunday, m_TxtEndTimeMonday, m_TxtEndTimeTuesday,
            m_TxtEndTimeWednesday, m_TxtEndTimeThursday, m_TxtEndTimeFriday, m_TxtEndTimeSaturday;

    private User m_DatabaseUser = null;
    private UserAvailability m_Availability = null;
    private short m_AvailableDays = 0;
    private boolean m_IsDisableAllDays = true;
    private boolean m_IsNoNumChatsLimit;
    private boolean m_IsNeverAvailable;
    private boolean m_IsAlwaysAvailable;
    private boolean m_IsSundayAvailable;
    private boolean m_IsMondayAvailable;
    private boolean m_IsTuesdayAvailable;
    private boolean m_IsWednesdayAvailable;
    private boolean m_IsThursdayAvailable;
    private boolean m_IsFridayAvailable;
    private boolean m_IsSaturdayAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_settings);
        setActivityContent();
        setFirebaseData();
        getUserSubjectsFromDB();
        getUserAvailabilityFromDB();
        setDialogSubjectsList();
        setDialogCreateSubject();
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

        if(id == m_BtnSubjects.getId()){
            showDialogSubjectsList();
        }else if(id == m_FabNext.getId()) {
            populateDatabaseWithExpertSettings();
            startHomeScreenActivity();
        }else if(id == m_SwitchSunday.getId()) {
            setSundayClick();
        }else if(id == m_SwitchMonday.getId()) {
            setMondayClick();
        }else if(id == m_SwitchTuesday.getId()) {
            setTuesdayClick();
        }else if(id == m_SwitchWednesday.getId()) {
            setWednesdayClick();
        }else if(id == m_SwitchThursday.getId()) {
            setThursdayClick();
        }else if(id == m_SwitchFriday.getId()) {
            setFridayClick();
        }else if(id == m_SwitchSaturday.getId()) {
            setSaturdayClick();
        }

        menageStartTimeClicks(id);
        menageEndTimeClicks(id);
    }

    private void getUserSubjectsFromDB()
    {
        Query subjUsersRef = m_Database.getReference("SubjectUsers")
                .orderByChild(m_CurrentUser.getUid());
        subjUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    SubjectUser user = (snapshot.child(m_CurrentUser.getUid())).getValue(SubjectUser.class);
                    if(user != null) {
                        m_SubjNamesSet.add(user.getSubjectName());
                        m_SubjStateMap.put(user.getSubjectName(), user.getIsValid());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void getUserAvailabilityFromDB()
    {
        DatabaseReference userId = m_Database.getReference("Users");
        userId.child(m_CurrentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot) {
                        if (i_DataSnapshot.exists() )
                        {
                            m_DatabaseUser = i_DataSnapshot.getValue(User.class);
                            if (i_DataSnapshot.child("userAvailability").exists())
                            {
                                m_Availability = i_DataSnapshot.child("userAvailability").getValue(UserAvailability.class);
                                String path = "userAvailability/weekAvailability/";
                                if (i_DataSnapshot.child(path).exists())
                                {
                                    Week week = i_DataSnapshot.getValue(Week.class);
                                    if (i_DataSnapshot.child(path + "sunday").exists()) {
                                        week.setSunday(i_DataSnapshot.child(path + "sunday").getValue(Day.class));
                                    }
                                    if (i_DataSnapshot.child(path + "monday").exists()) {
                                        week.setMonday(i_DataSnapshot.child(path + "monday").getValue(Day.class));
                                    }
                                    if (i_DataSnapshot.child(path + "tuesday").exists()) {
                                        week.setTuesday(i_DataSnapshot.child(path + "tuesday").getValue(Day.class));
                                    }
                                    if (i_DataSnapshot.child(path + "wednesday").exists()) {
                                        week.setWednesday(i_DataSnapshot.child(path + "wednesday").getValue(Day.class));
                                    }
                                    if (i_DataSnapshot.child(path + "thursday").exists()) {
                                        week.setThursday(i_DataSnapshot.child(path + "thursday").getValue(Day.class));
                                    }
                                    if (i_DataSnapshot.child(path + "friday").exists()) {
                                        week.setFriday(i_DataSnapshot.child(path + "friday").getValue(Day.class));
                                    }
                                    if (i_DataSnapshot.child(path + "saturday").exists()) {
                                        week.setSaturday(i_DataSnapshot.child(path + "saturday").getValue(Day.class));
                                    }

                                    m_Availability.setWeekAvailability(week);
                                }

                                updateUI();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError i_DataSnapshot) {}
                });
    }

    private void updateUI()
    {
        m_IsAlwaysAvailable = m_Availability.getIsAlwaysAvailable();
        m_IsNeverAvailable = m_Availability.getIsNeverAvailable();
        m_IsNoNumChatsLimit = m_Availability.getIsNoNumChatsLimit();

        Week weekAvailability = m_Availability.getWeekAvailability();
        if(weekAvailability != null)
        {
            Day sunday = weekAvailability.getSunday();
            Day monday = weekAvailability.getMonday();
            Day tuesday = weekAvailability.getTuesday();
            Day wednesday = weekAvailability.getWednesday();
            Day thursday = weekAvailability.getThursday();
            Day friday = weekAvailability.getFriday();
            Day saturday = weekAvailability.getSaturday();

            if (m_IsNeverAvailable) {
                m_IsDisableAllDays = true;
                switchOffAllDays();
            }
            else {
                if (m_IsAlwaysAvailable) {
                    m_IsDisableAllDays = false;
                    switchOnAllDays();
                }
                if (sunday != null && sunday.getIsAvailable()) {
                    m_IsSundayAvailable = true;
                    m_AvailableDays++;
                    updateSundayUI(sunday);
                }
                if (monday != null && monday.getIsAvailable()) {
                    m_IsMondayAvailable = true;
                    m_AvailableDays++;
                    updateMondayUI(monday);
                }
                if (tuesday != null && tuesday.getIsAvailable()) {
                    m_IsTuesdayAvailable = true;
                    m_AvailableDays++;
                    updateTuesdayUI(tuesday);
                }
                if (wednesday != null && wednesday.getIsAvailable()) {
                    m_IsWednesdayAvailable = true;
                    m_AvailableDays++;
                    updateWednesdayUI(wednesday);
                }
                if (thursday != null && thursday.getIsAvailable()) {
                    m_IsThursdayAvailable = true;
                    m_AvailableDays++;
                    updateThursdayUI(thursday);
                }
                if (friday != null && friday.getIsAvailable()) {
                    m_IsFridayAvailable = true;
                    m_AvailableDays++;
                    updateFridayUI(friday);
                }
                if (saturday != null && saturday.getIsAvailable()) {
                    m_IsSaturdayAvailable = true;
                    m_AvailableDays++;
                    updateSaturdayUI(saturday);
                }
            }
        }
    }

    private void updateSundayUI(Day i_Sunday)
    {
        int hours, minutes;

        switchOn(m_SwitchSunday, m_BtnStartTimeSunday, m_BtnEndTimeSunday);
        hours = Integer.parseInt(i_Sunday.getStartTime()
                .substring(0, i_Sunday.getStartTime().indexOf(":")));
        minutes = Integer.parseInt(i_Sunday.getStartTime()
                .substring(i_Sunday.getStartTime().indexOf(":") + 1));
        setSundayTime(hours, minutes, true);

        hours = Integer.parseInt(i_Sunday.getEndTime()
                .substring(0, i_Sunday.getEndTime().indexOf(":")));
        minutes = Integer.parseInt(i_Sunday.getEndTime()
                .substring(i_Sunday.getEndTime().indexOf(":") + 1));
        setSundayTime(hours, minutes, false);
    }

    private void updateMondayUI(Day i_Monday)
    {
        int hours, minutes;

        switchOn(m_SwitchMonday, m_BtnStartTimeMonday, m_BtnEndTimeMonday);
        hours = Integer.parseInt(i_Monday.getStartTime()
                .substring(0, i_Monday.getStartTime().indexOf(":")));
        minutes = Integer.parseInt(i_Monday.getStartTime()
                .substring(i_Monday.getStartTime().indexOf(":") + 1));
        setMondayTime(hours, minutes, true);

        hours = Integer.parseInt(i_Monday.getEndTime()
                .substring(0, i_Monday.getEndTime().indexOf(":")));
        minutes = Integer.parseInt(i_Monday.getEndTime()
                .substring(i_Monday.getEndTime().indexOf(":") + 1));
        setMondayTime(hours, minutes, false);
    }

    private void updateTuesdayUI(Day i_Tuesday)
    {
        int hours, minutes;

        switchOn(m_SwitchTuesday, m_BtnStartTimeTuesday, m_BtnEndTimeTuesday);
        hours = Integer.parseInt(i_Tuesday.getStartTime()
                .substring(0, i_Tuesday.getStartTime().indexOf(":")));
        minutes = Integer.parseInt(i_Tuesday.getStartTime()
                .substring(i_Tuesday.getStartTime().indexOf(":") + 1));
        setTuesdayTime(hours, minutes, true);

        hours = Integer.parseInt(i_Tuesday.getEndTime()
                .substring(0, i_Tuesday.getEndTime().indexOf(":")));
        minutes = Integer.parseInt(i_Tuesday.getEndTime()
                .substring(i_Tuesday.getEndTime().indexOf(":") + 1));
        setTuesdayTime(hours, minutes, false);
    }

    private void updateWednesdayUI(Day i_Wednesday)
    {
        int hours, minutes;

        switchOn(m_SwitchWednesday, m_BtnStartTimeWednesday, m_BtnEndTimeWednesday);
        hours = Integer.parseInt(i_Wednesday.getStartTime()
                .substring(0, i_Wednesday.getStartTime().indexOf(":")));
        minutes = Integer.parseInt(i_Wednesday.getStartTime()
                .substring(i_Wednesday.getStartTime().indexOf(":") + 1));
        setWednesdayTime(hours, minutes, true);

        hours = Integer.parseInt(i_Wednesday.getEndTime()
                .substring(0, i_Wednesday.getEndTime().indexOf(":")));
        minutes = Integer.parseInt(i_Wednesday.getEndTime()
                .substring(i_Wednesday.getEndTime().indexOf(":") + 1));
        setWednesdayTime(hours, minutes, false);

    }

    private void updateThursdayUI(Day i_Thursday)
    {
        int hours, minutes;

        switchOn(m_SwitchThursday, m_BtnStartTimeThursday, m_BtnEndTimeThursday);
        hours = Integer.parseInt(i_Thursday.getStartTime()
                .substring(0, i_Thursday.getStartTime().indexOf(":")));
        minutes = Integer.parseInt(i_Thursday.getStartTime()
                .substring(i_Thursday.getStartTime().indexOf(":") + 1));
        setThursdayTime(hours, minutes, true);

        hours = Integer.parseInt(i_Thursday.getEndTime()
                .substring(0, i_Thursday.getEndTime().indexOf(":")));
        minutes = Integer.parseInt(i_Thursday.getEndTime()
                .substring(i_Thursday.getEndTime().indexOf(":") + 1));
        setThursdayTime(hours, minutes, false);
    }

    private void updateFridayUI(Day i_Friday)
    {
        int hours, minutes;

        switchOn(m_SwitchFriday, m_BtnStartTimeFriday, m_BtnEndTimeFriday);
        hours = Integer.parseInt(i_Friday.getStartTime()
                .substring(0, i_Friday.getStartTime().indexOf(":")));
        minutes = Integer.parseInt(i_Friday.getStartTime()
                .substring(i_Friday.getStartTime().indexOf(":") + 1));
        setFridayTime(hours, minutes, true);

        hours = Integer.parseInt(i_Friday.getEndTime()
                .substring(0, i_Friday.getEndTime().indexOf(":")));
        minutes = Integer.parseInt(i_Friday.getEndTime()
                .substring(i_Friday.getEndTime().indexOf(":") + 1));
        setFridayTime(hours, minutes, false);
    }

    private void updateSaturdayUI(Day i_Saturday)
    {
        int hours, minutes;

        switchOn(m_SwitchSaturday, m_BtnStartTimeSaturday, m_BtnEndTimeSaturday);
        hours = Integer.parseInt(i_Saturday.getStartTime()
                .substring(0, i_Saturday.getStartTime().indexOf(":")));
        minutes = Integer.parseInt(i_Saturday.getStartTime()
                .substring(i_Saturday.getStartTime().indexOf(":") + 1));
        setSaturdayTime(hours, minutes, true);

        hours = Integer.parseInt(i_Saturday.getEndTime()
                .substring(0, i_Saturday.getEndTime().indexOf(":")));
        minutes = Integer.parseInt(i_Saturday.getEndTime()
                .substring(i_Saturday.getEndTime().indexOf(":") + 1));
        setSaturdayTime(hours, minutes, false);
    }

    private void showDialogSubjectsList()
    {
        buildSubjectsListOptions();
        populateSubjectsListView();

        m_DialogSubjList.findViewById(R.id.btn_new_of_dialog_subjects_list)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View i_View) {
                        showDialogCreateSubject();
                    }
                });

        m_DialogSubjList.findViewById(R.id.btn_done_of_dialog_subjects_list)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View i_View) {
                        showConfirmDialog();
                    }
                });

        m_DialogSubjList.show();
    }

    private void showConfirmDialog()
    {
        final Dialog confirmDialog = new Dialog(ActivityExpertSettings.this);
        confirmDialog.setContentView(R.layout.dialog_confirm);

        ImageButton closeBtn = confirmDialog.findViewById(R.id.btn_close_of_dialog_confirm);
        TextView fieldTitle = confirmDialog.findViewById(R.id.txt_title_of_dialog_confirm);
        TextView rightBtn = confirmDialog.findViewById(R.id.btn_right_of_dialog_confirm);
        TextView leftBtn = confirmDialog.findViewById(R.id.btn_left_of_dialog_confirm);

        closeBtn.setVisibility(View.VISIBLE);
        fieldTitle.setText("You'll be marked as an expert in the selected sections");
        rightBtn.setText("Confirm");
        leftBtn.setText("Cancel");

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabaseSubjectUsers();
                confirmDialog.dismiss();
                m_DialogSubjList.dismiss();
            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });

        confirmDialog.show();
    }

    private void buildSubjectsListOptions()
    {
        DatabaseReference subjectRef = m_Database.getReference().child("Subjects");
        m_Options = new FirebaseRecyclerOptions.Builder<Subject>()
                .setQuery(subjectRef, Subject.class)
                .build();
    }

    private void populateSubjectsListView()
    {
        m_Adapter = new FirebaseRecyclerAdapter<Subject, ViewHolderSubject>(m_Options) {
            @NonNull
            @Override
            public ViewHolderSubject onCreateViewHolder(@NonNull ViewGroup i_ViewGroup, int i_Position) {
                //Create a new instance of the ViewHolder and use R.layout.item_dish for each item
                View view = LayoutInflater
                        .from(i_ViewGroup.getContext())
                        .inflate(R.layout.item_subject, i_ViewGroup, false);

                return new ViewHolderSubject(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderSubject i_ViewHolder, int i_Position,
                                            @NonNull final Subject i_Subject)
            {
                i_ViewHolder.getCheckBox().setVisibility(View.VISIBLE);
                i_ViewHolder.getArrowRightIcon().setVisibility(View.INVISIBLE);
                Picasso.get().load(i_Subject.getImgLink()).into(i_ViewHolder.getSubjectImage());
                i_ViewHolder.setSubjectName(i_Subject.getSubjectName());

                i_ViewHolder.getCheckBox()
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        m_SubjStateMap.put(i_Subject.getSubjectName(), isChecked);
                        m_SubjNamesSet.add(i_Subject.getSubjectName());
                    }
                });

                if( m_SubjStateMap.get(i_Subject.getSubjectName()) != null)
                {
                    i_ViewHolder.getCheckBox()
                            .setChecked(m_SubjStateMap.get(i_Subject.getSubjectName()));
                }
                else {
                    i_ViewHolder.getCheckBox().setChecked(false);
                }

                i_ViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View i_View, int i_Position, boolean i_IsLongClick) {
                        if ( m_SubjStateMap.get(i_Subject.getSubjectName()) != null)
                        {
                            boolean state = !m_SubjStateMap.get(i_Subject.getSubjectName());
                            i_ViewHolder.getCheckBox().setChecked(state);
                            m_SubjStateMap.put(i_Subject.getSubjectName(), state);
                            m_SubjNamesSet.add(i_Subject.getSubjectName());
                        }
                        else  {
                            i_ViewHolder.getCheckBox().setChecked(true);
                            m_SubjStateMap.put(i_Subject.getSubjectName(), true);
                            m_SubjNamesSet.add(i_Subject.getSubjectName());
                        }
                    }
                });
            }
        };

        m_Adapter.startListening();
        m_DialogRecyclerView.setAdapter(m_Adapter);
    }

    private void showDialogCreateSubject()
    {
        m_DialogImgView.setImageURI(m_DialogImgURI);

        m_DialogCreateSubj.findViewById(R.id.btn_add_a_photo_of_dialog_create_a_subj)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addProfileImage();
                    }
                });
        m_DialogCreateSubj.findViewById(R.id.btn_create_of_dialog_create_a_subj)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(m_FieldSubjName.getText() == null
                                || m_FieldSubjName.getText().toString().isEmpty())
                        {
                            m_FieldSubjName.setError("The field is required");
                        }
                        else {
                            if(m_IsImgPicked){
                                uploadImageToStorage( m_FieldSubjName.getText().toString());
                            }else{
                                uploadImageToStorage(DEFAULT);
                            }
                        }
                    }
                });
        m_DialogCreateSubj.findViewById(R.id.btn_cancel_of_dialog_create_a_subj)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDefaultDialogCreateSubject();
                        m_DialogCreateSubj.dismiss();
                    }
                });

        m_DialogCreateSubj.show();
    }

    private void setDefaultDialogCreateSubject()
    {
        m_DialogImgURI = Uri.parse(RES + getApplicationContext()
                .getPackageName() + DRAWABLE_DEFAULT);
        m_FieldSubjDescription.getText().clear();
        m_FieldSubjName.getText().clear();
    }

    private void addProfileImage()
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
        if(ContextCompat.checkSelfPermission(ActivityExpertSettings.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    ActivityExpertSettings.this, Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(ActivityExpertSettings.this,
                        "Please accept for required permission ", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(ActivityExpertSettings.this,
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
        Intent IntentGallery = new Intent(Intent.ACTION_GET_CONTENT);
        IntentGallery.setType("image/*");
        startActivityForResult(IntentGallery,IMG_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == IMG_REQ && data != null)
        {
            m_DialogImgURI = data.getData();
            m_DialogImgView.setImageURI(m_DialogImgURI);
            m_IsImgPicked = true;
        }
    }

    private void uploadImageToStorage(String i_Image)
    {
        StorageReference imagePath = m_Storage.getReference().child("Images/Subjects");
        if(m_DialogImgURI != null && !m_IsImgStored)
        {
            final StorageReference imageRef = imagePath.child(i_Image);
            imageRef.putFile(m_DialogImgURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    m_IsImgStored = true;
                                    m_DialogImgURI = uri;
                                    pushNewSubjectToDatabase();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ActivityExpertSettings.this,
                            "ERROR:\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            pushNewSubjectToDatabase();
        }
    }


    private void pushNewSubjectToDatabase()
    {
        final Subject subject = new Subject();
        final DatabaseReference subjectsRef = m_Database.getReference("Subjects");
        final String subjName = m_FieldSubjName.getText().toString().toUpperCase();

        if(m_FieldSubjDescription.getText().toString().isEmpty())
        {
            m_FieldSubjDescription.setText("Subject description");
        }
        subjectsRef.child(subjName).addListenerForSingleValueEvent(new ValueEventListener()
        {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                    {
                        if (i_DataSnapshot.exists()) {
                            m_FieldSubjName.setError("Subject already exists.");
                        }
                        else {
                            subject.setSubjectName(subjName);
                            subject.setSubjectDescription(m_FieldSubjDescription.getText().toString());
                            subject.setImgLink(m_DialogImgURI.toString());
                            subjectsRef.child(subjName).setValue(subject)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> i_Task)
                                        {
                                            if (i_Task.isSuccessful()) {
                                                Toast.makeText(ActivityExpertSettings.this,
                                                        "The subject is created.",
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(ActivityExpertSettings.this,
                                                        "Failure! Something was going wrong.",
                                                        Toast.LENGTH_SHORT).show();
                                            }

                                            setDefaultDialogCreateSubject();
                                        }
                                    });
                            m_DialogCreateSubj.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError i_DataSnapshot) {}
                });
    }

    private void setFirebaseData()
    {
        m_Auth = FirebaseAuth.getInstance();
        m_CurrentUser = m_Auth.getCurrentUser();
        m_Database = FirebaseDatabase.getInstance();
        m_Storage = FirebaseStorage.getInstance();
    }

    private void setDialogSubjectsList()
    {
        m_DialogSubjList = new Dialog(ActivityExpertSettings.this);
        m_DialogSubjList.setContentView(R.layout.dialog_subjects_list);
        m_DialogSubjList.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        m_SuggestionsList = new ArrayList<>();
//        m_SearchBar = findViewById(R.id.search_bar_of_dialog_subjects_list);
//
//        m_SearchBar.setCardViewElevation(10);
//        m_SearchBar.setMaxSuggestionCount(5);
//        m_SearchBar.addTextChangeListener(this);
//        m_SearchBar.setOnSearchActionListener(this);

        m_DialogRecyclerView = m_DialogSubjList.findViewById(R.id.recycler_view_of_dialog_subjects_list);
        m_DialogRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        m_DialogRecyclerView.setLayoutManager(layoutManager);
    }

    private void setDialogCreateSubject()
    {
        m_DialogCreateSubj = new Dialog(ActivityExpertSettings.this);
        m_DialogCreateSubj.setContentView(R.layout.dialog_create_a_subject);
        m_DialogCreateSubj.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        m_DialogImgView = m_DialogCreateSubj.findViewById(R.id.img_of_dialog_create_a_subj);
        m_DialogImgURI = Uri.parse(RES + getApplicationContext()
                .getPackageName() + DRAWABLE_DEFAULT);
        m_FieldSubjName = m_DialogCreateSubj.findViewById(R.id.field_subject_name_of_dialog_create_a_subj);
        m_FieldSubjDescription = m_DialogCreateSubj.findViewById(R.id.field_subject_description_of_dialog_create_a_subj);

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }

    private void populateDatabaseWithExpertSettings()
    {
        Day sunday, monday, tuesday, wednesday, thursday, friday, saturday;
        sunday = monday = tuesday = thursday
                = wednesday = friday = saturday = null;

        if(m_IsSundayAvailable) {
            sunday = new Day();
            setDayAvailability(sunday, eWeekDay.Sunday,
                    m_TxtStartTimeSunday.getText().toString(),
                    m_TxtEndTimeSunday.getText().toString());
        }
        if(m_IsMondayAvailable) {
            monday = new Day();
            setDayAvailability(monday, eWeekDay.Monday,
                    m_TxtStartTimeMonday.getText().toString(),
                    m_TxtEndTimeMonday.getText().toString());
        }
        if(m_IsTuesdayAvailable) {
            tuesday = new Day();
            setDayAvailability(tuesday, eWeekDay.Tuesday,
                    m_TxtStartTimeTuesday.getText().toString(),
                    m_TxtEndTimeTuesday.getText().toString());
        }
        if(m_IsWednesdayAvailable) {
            wednesday = new Day();
            setDayAvailability(wednesday, eWeekDay.Wednesday,
                    m_TxtStartTimeWednesday.getText().toString(),
                    m_TxtEndTimeWednesday.getText().toString());
        }
        if(m_IsThursdayAvailable) {
            thursday = new Day();
            setDayAvailability(thursday, eWeekDay.Thursday,
                    m_TxtStartTimeThursday.getText().toString(),
                    m_TxtEndTimeThursday.getText().toString());
        }
        if(m_IsFridayAvailable) {
            friday = new Day();
            setDayAvailability(friday, eWeekDay.Friday,
                    m_TxtStartTimeFriday.getText().toString(),
                    m_TxtEndTimeFriday.getText().toString());
        }
        if(m_IsSaturdayAvailable) {
            saturday = new Day();
            setDayAvailability(saturday, eWeekDay.Saturday,
                    m_TxtStartTimeSaturday.getText().toString(),
                    m_TxtEndTimeSaturday.getText().toString());
        }

        Week week = new Week();
        if(isWeekAvailable(week, sunday, monday, tuesday, wednesday, thursday, friday, saturday))
        {
            m_Availability = new UserAvailability();
            m_Availability.setWeekAvailability(week);
            m_Availability.setIsAlwaysAvailable(m_IsAlwaysAvailable);
            m_Availability.setIsNeverAvailable(m_IsNeverAvailable);
            m_Availability.setIsNoNumChatsLimit(m_IsNoNumChatsLimit);
        }
        DatabaseReference databaseRef = m_Database.getReference("Users");
        databaseRef.child(m_CurrentUser.getUid()).child("userAvailability").setValue(m_Availability)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> i_Task) {
                if(i_Task.isSuccessful()){ }
                else{
                    Toast.makeText(ActivityExpertSettings.this,
                            "Failure! Something was going wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateDatabaseSubjectUsers()
    {
        DatabaseReference subjectRef =  m_Database.getReference("SubjectUsers");
        String userID = m_CurrentUser.getUid();
        for (String subjectName : m_SubjNamesSet)
        {
            if(m_SubjStateMap.get(subjectName) != null)
            {
                if(m_SubjStateMap.get(subjectName))
                {
                    subjectRef.child(subjectName).child(userID).child("isValid").setValue(true);
                    subjectRef.child(subjectName).child(userID).child("subjectName").setValue(subjectName);
                    subjectRef.child(subjectName).child(userID).child("userName")
                            .setValue(m_DatabaseUser.getFirstName() + " " + m_DatabaseUser.getFamilyName());
                    subjectRef.child(subjectName).child(userID).child("userImgLink").setValue(m_DatabaseUser.getImgLink());
                    subjectRef.child(subjectName).child(userID).child("userId").setValue(userID);

                }
                else{
                    subjectRef.child(subjectName).child(userID).child("isValid").setValue(false);
                    subjectRef.child(subjectName).child(userID).child("subjectName").setValue(subjectName);
                    subjectRef.child(subjectName).child(userID).child("userName")
                            .setValue(m_DatabaseUser.getFirstName() + " " + m_DatabaseUser.getFamilyName());
                    subjectRef.child(subjectName).child(userID).child("userImgLink").setValue(m_DatabaseUser.getImgLink());
                    subjectRef.child(subjectName).child(userID).child("userId").setValue(userID);
                }
            }
        }
    }

    private void setDayAvailability(Day i_Day, eWeekDay i_WeekDay,
                                    String i_StartTime,  String i_EndTime)
    {
        i_Day.setDay(i_WeekDay);
        i_Day.setIsAvailable(true);
        i_Day.setStartTime(i_StartTime);
        i_Day.setEndTime(i_EndTime);
    }


    private boolean isWeekAvailable(Week i_Week, Day i_Sun, Day i_Mon, Day i_Tue,
                                     Day i_Wed, Day i_Thu, Day i_Fri, Day i_Sat)
    {
        boolean isAvailable = false;
        if(i_Sun != null) {
            i_Week.setSunday(i_Sun);
            isAvailable = true;
        }
        if(i_Mon != null) {
            i_Week.setMonday(i_Mon);
            isAvailable = true;
        }
        if(i_Tue != null) {
            i_Week.setTuesday(i_Tue);
            isAvailable = true;
        }
        if(i_Wed != null) {
            i_Week.setWednesday(i_Wed);
            isAvailable = true;
        }
        if(i_Thu != null) {
            i_Week.setThursday(i_Thu);
            isAvailable = true;
        }
        if(i_Fri != null) {
            i_Week.setFriday(i_Fri);
            isAvailable = true;
        }
        if(i_Sat != null) {
            i_Week.setSaturday(i_Sat);
            isAvailable = true;
        }

        return isAvailable;
    }




    @Override
    public void onCheckedChanged(CompoundButton i_ButtonView, boolean i_IsChecked)
    {
        int id = i_ButtonView.getId();

        if(m_SwitchAlwaysAvailable.getId() == id)
        {
            if(i_IsChecked) {
                m_IsAlwaysAvailable = true;
                m_IsNeverAvailable = false;
                m_SwitchNotDisturb.setChecked(false);
                switchOnAllDays();
            }else{
                if(m_IsDisableAllDays) {
                    switchOffAllDays();
                    m_Availability = null;
                }
                m_IsAlwaysAvailable = false;
            }
        }
        else if(m_SwitchNotDisturb.getId() == id)
        {
            if (i_IsChecked) {
                m_IsDisableAllDays = true;
                m_IsNeverAvailable = true;
                m_IsAlwaysAvailable = false;
                m_SwitchAlwaysAvailable.setChecked(false);
                switchOffAllDays();
                m_Availability = null;
            }else {
                //switchOnAllDays();
            }
        }
    }


    private void switchOnAllDays()
    {
        m_AvailableDays = 7;
        switchOn(m_SwitchSunday, m_BtnStartTimeSunday, m_BtnEndTimeSunday);
        m_IsSundayAvailable = true;
        switchOn(m_SwitchMonday, m_BtnStartTimeMonday, m_BtnEndTimeMonday);
        m_IsMondayAvailable = true;
        switchOn(m_SwitchTuesday, m_BtnStartTimeTuesday, m_BtnEndTimeTuesday);
        m_IsTuesdayAvailable = true;
        switchOn(m_SwitchWednesday, m_BtnStartTimeWednesday, m_BtnEndTimeWednesday);
        m_IsWednesdayAvailable = true;
        switchOn(m_SwitchThursday, m_BtnStartTimeThursday, m_BtnEndTimeThursday);
        m_IsThursdayAvailable = true;
        switchOn(m_SwitchFriday, m_BtnStartTimeFriday, m_BtnEndTimeFriday);
        m_IsFridayAvailable = true;
        switchOn(m_SwitchSaturday, m_BtnStartTimeSaturday, m_BtnEndTimeSaturday);
        m_IsSaturdayAvailable = true;
    }

    private void switchOffAllDays()
    {
        m_AvailableDays = 0;
        switchOff(m_SwitchSunday, m_BtnStartTimeSunday, m_BtnEndTimeSunday,
                m_TxtStartTimeSunday, m_TxtEndTimeSunday);
        m_IsSundayAvailable = false;
        switchOff(m_SwitchMonday, m_BtnStartTimeMonday, m_BtnEndTimeMonday,
                m_TxtStartTimeMonday, m_TxtEndTimeMonday);
        m_IsMondayAvailable = false;
        switchOff(m_SwitchTuesday, m_BtnStartTimeTuesday, m_BtnEndTimeTuesday,
                m_TxtStartTimeTuesday, m_TxtEndTimeTuesday);
        m_IsTuesdayAvailable = false;
        switchOff(m_SwitchWednesday, m_BtnStartTimeWednesday, m_BtnEndTimeWednesday,
                m_TxtStartTimeWednesday, m_TxtEndTimeWednesday);
        m_IsWednesdayAvailable = false;
        switchOff(m_SwitchThursday, m_BtnStartTimeThursday, m_BtnEndTimeThursday,
                m_TxtStartTimeThursday, m_TxtEndTimeThursday);
        m_IsThursdayAvailable = false;
        switchOff(m_SwitchFriday, m_BtnStartTimeFriday, m_BtnEndTimeFriday,
                m_TxtStartTimeFriday, m_TxtEndTimeFriday);
        m_IsFridayAvailable = false;
        switchOff(m_SwitchSaturday, m_BtnStartTimeSaturday, m_BtnEndTimeSaturday,
                m_TxtStartTimeSaturday, m_TxtEndTimeSaturday);
        m_IsSaturdayAvailable = false;
    }

    private void switchOn(Button i_WeekDay, Button i_TimeFrom, Button i_TimeTo)
    {
        if(m_SwitchNotDisturb.isChecked())
        {
            m_IsNeverAvailable = false;
            m_SwitchNotDisturb.setChecked(false);
        }
        if(m_AvailableDays == 7)
        {
            m_IsDisableAllDays = true;
            m_IsAlwaysAvailable = true;
            m_SwitchAlwaysAvailable.setChecked(true);
        }

        i_WeekDay.setBackground(getDrawable(R.drawable.btn_circl_blue_pressed_blue_transparent));
        i_WeekDay.setTextColor(getColor(R.color.white));

        i_TimeFrom.setBackground(getDrawable(R.drawable.btn_circl_green_light_pressed_white));
        i_TimeFrom.setText("+");
        i_TimeFrom.setEnabled(true);

        i_TimeTo.setBackground(getDrawable(R.drawable.btn_circl_green_light_pressed_white));
        i_TimeTo.setText("+");
        i_TimeTo.setEnabled(true);
    }

    private void switchOff(Button i_WeekDay, Button i_BtnFrom, Button i_BtnTo, TextView i_TxtFrom, TextView i_TxtTo)
    {
        if(m_SwitchAlwaysAvailable.isChecked())
        {
            m_IsDisableAllDays = false;
            m_IsAlwaysAvailable = false;
            m_SwitchAlwaysAvailable.setChecked(false);
        }
        i_TxtFrom.setVisibility(View.INVISIBLE);
        i_TxtFrom.setText("00:00");
        i_TxtTo.setVisibility(View.INVISIBLE);
        i_TxtTo.setText("23:59");

        i_WeekDay.setBackground(getDrawable(R.drawable.btn_circl_white_pressed_blue));
        i_WeekDay.setTextColor(getColor(R.color.black));

        i_BtnFrom.setBackground(getDrawable(R.drawable.btn_circl_white_with_black_frame));
        i_BtnFrom.setText("-");
        i_BtnFrom.setVisibility(View.VISIBLE);
        i_BtnFrom.setEnabled(false);

        i_BtnTo.setBackground(getDrawable(R.drawable.btn_circl_white_with_black_frame));
        i_BtnTo.setText("-");
        i_BtnTo.setVisibility(View.VISIBLE);
        i_BtnTo.setEnabled(false);
    }

    private void setSundayClick()
    {
        if(m_IsSundayAvailable)
        {
            m_IsSundayAvailable = false;
            m_AvailableDays--;
            switchOff(m_SwitchSunday, m_BtnStartTimeSunday, m_BtnEndTimeSunday,
                    m_TxtStartTimeSunday, m_TxtEndTimeSunday);
        }else{
            m_IsSundayAvailable = true;
            m_AvailableDays++;
            switchOn(m_SwitchSunday, m_BtnStartTimeSunday, m_BtnEndTimeSunday);
        }
    }

    private void setMondayClick()
    {
        if(m_IsMondayAvailable)
        {
            m_IsMondayAvailable = false;
            m_AvailableDays--;
            switchOff(m_SwitchMonday, m_BtnStartTimeMonday, m_BtnEndTimeMonday,
                    m_TxtStartTimeMonday, m_TxtEndTimeMonday);
        }
        else {
            m_IsMondayAvailable = true;
            m_AvailableDays++;
            switchOn(m_SwitchMonday, m_BtnStartTimeMonday, m_BtnEndTimeMonday);
        }
    }

    private void setTuesdayClick()
    {
        if(m_IsTuesdayAvailable)
        {
            m_IsTuesdayAvailable = false;
            m_AvailableDays--;
            switchOff(m_SwitchTuesday, m_BtnStartTimeTuesday, m_BtnEndTimeTuesday,
                    m_TxtStartTimeTuesday, m_TxtEndTimeTuesday);
        }
        else{
            m_IsTuesdayAvailable = true;
            m_AvailableDays++;
            switchOn(m_SwitchTuesday, m_BtnStartTimeTuesday, m_BtnEndTimeTuesday);
        }
    }

    private void setWednesdayClick()
    {
        if(m_IsWednesdayAvailable)
        {
            m_IsWednesdayAvailable = false;
            m_AvailableDays--;
            switchOff(m_SwitchWednesday, m_BtnStartTimeWednesday, m_BtnEndTimeWednesday,
                    m_TxtStartTimeWednesday, m_TxtEndTimeWednesday);
        }
        else{
            m_IsWednesdayAvailable = true;
            m_AvailableDays++;
            switchOn(m_SwitchWednesday, m_BtnStartTimeWednesday, m_BtnEndTimeWednesday);
        }
    }

    private void setThursdayClick()
    {
        if(m_IsThursdayAvailable)
        {
            m_IsThursdayAvailable = false;
            m_AvailableDays--;
            switchOff(m_SwitchThursday, m_BtnStartTimeThursday, m_BtnEndTimeThursday,
                    m_TxtStartTimeThursday, m_TxtEndTimeThursday);
        }
        else{
            m_IsThursdayAvailable = true;
            m_AvailableDays++;
            switchOn(m_SwitchThursday, m_BtnStartTimeThursday, m_BtnEndTimeThursday);
        }
    }

    private void setFridayClick()
    {
        if(m_IsFridayAvailable)
        {
            m_IsFridayAvailable = false;
            m_AvailableDays--;
            switchOff(m_SwitchFriday, m_BtnStartTimeFriday, m_BtnEndTimeFriday,
                    m_TxtStartTimeFriday, m_TxtEndTimeFriday);
        }
        else{
            m_IsFridayAvailable = true;
            m_AvailableDays++;
            switchOn(m_SwitchFriday, m_BtnStartTimeFriday, m_BtnEndTimeFriday);
        }
    }

    private void setSaturdayClick()
    {
        if(m_IsSaturdayAvailable)
        {
            m_IsSaturdayAvailable = false;
            m_AvailableDays--;
            switchOff(m_SwitchSaturday, m_BtnStartTimeSaturday, m_BtnEndTimeSaturday,
                    m_TxtStartTimeSaturday, m_TxtEndTimeSaturday);
        }
        else{
            m_IsSaturdayAvailable = true;
            m_AvailableDays++;
            switchOn(m_SwitchSaturday, m_BtnStartTimeSaturday, m_BtnEndTimeSaturday);
        }
    }

    private void menageStartTimeClicks(int i_ViewId) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePicker;

        if (i_ViewId == m_BtnStartTimeSunday.getId() || i_ViewId == m_TxtStartTimeSunday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setSundayTime(i_SelectedHour,i_SelectedMinute, true);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnStartTimeMonday.getId() || i_ViewId == m_TxtStartTimeMonday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setMondayTime(i_SelectedHour,i_SelectedMinute, true);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnStartTimeTuesday.getId() || i_ViewId == m_TxtStartTimeTuesday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setTuesdayTime(i_SelectedHour,i_SelectedMinute, true);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnStartTimeWednesday.getId() || i_ViewId == m_TxtStartTimeWednesday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setWednesdayTime(i_SelectedHour,i_SelectedMinute, true);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnStartTimeThursday.getId() || i_ViewId == m_TxtStartTimeThursday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setThursdayTime(i_SelectedHour,i_SelectedMinute, true);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnStartTimeFriday.getId() || i_ViewId == m_TxtStartTimeFriday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setFridayTime(i_SelectedHour,i_SelectedMinute, true);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnStartTimeSaturday.getId() || i_ViewId == m_TxtStartTimeSaturday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setSaturdayTime(i_SelectedHour,i_SelectedMinute, true);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        }
    }

    private void menageEndTimeClicks(int i_ViewId) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePicker;

        if (i_ViewId == m_BtnEndTimeSunday.getId() || i_ViewId == m_TxtEndTimeSunday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setSundayTime(i_SelectedHour,i_SelectedMinute, false);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnEndTimeMonday.getId() || i_ViewId == m_TxtEndTimeMonday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setMondayTime(i_SelectedHour,i_SelectedMinute, false);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnEndTimeTuesday.getId() || i_ViewId == m_TxtEndTimeTuesday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setTuesdayTime(i_SelectedHour,i_SelectedMinute, false);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnEndTimeWednesday.getId() || i_ViewId == m_TxtEndTimeWednesday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setWednesdayTime(i_SelectedHour,i_SelectedMinute, false);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnEndTimeThursday.getId() || i_ViewId == m_TxtEndTimeThursday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setThursdayTime(i_SelectedHour,i_SelectedMinute, false);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnEndTimeFriday.getId() || i_ViewId == m_TxtEndTimeFriday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setFridayTime(i_SelectedHour,i_SelectedMinute, false);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnEndTimeSaturday.getId() || i_ViewId == m_TxtEndTimeSaturday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setSaturdayTime(i_SelectedHour,i_SelectedMinute, false);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        }
    }

    private String convertToStringFormat(int i_Hour, int i_Minute)
    {
        String hourStr = (i_Hour < 10) ? "0" +  i_Hour : String.valueOf(i_Hour);
        String minutesStr = (i_Minute < 10) ? "0" + i_Minute : String.valueOf(i_Minute);

        return hourStr + ":" + minutesStr;
    }

    private void setSundayTime(int i_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String time = convertToStringFormat(i_Hour, i_Minute);
        
        if(i_IsStartTime)
        {
            m_BtnEndTimeSunday.setVisibility(View.INVISIBLE);
            m_TxtEndTimeSunday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndTimeSunday.getText().toString())) {
                m_BtnStartTimeSunday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeSunday.setText(time);
                m_TxtStartTimeSunday.setVisibility(View.VISIBLE);
            } else {
                //Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }
        else {
            m_BtnStartTimeSunday.setVisibility(View.INVISIBLE);
            m_TxtStartTimeSunday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeSunday.getText().toString(), time)) {
                m_BtnEndTimeSunday.setVisibility(View.INVISIBLE);
                m_TxtEndTimeSunday.setText(time);
                m_TxtEndTimeSunday.setVisibility(View.VISIBLE);
            } else {
                //Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    public void setMondayTime(int i_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String time = convertToStringFormat(i_Hour, i_Minute);

        if(i_IsStartTime)
        {
            m_BtnEndTimeMonday.setVisibility(View.INVISIBLE);
            m_TxtEndTimeMonday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndTimeMonday.getText().toString())) {
                m_BtnStartTimeMonday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeMonday.setText(time);
                m_TxtStartTimeMonday.setVisibility(View.VISIBLE);
            } else {
                //Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }
        else {
            m_BtnStartTimeMonday.setVisibility(View.INVISIBLE);
            m_TxtStartTimeMonday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeMonday.getText().toString(), time)) {
                m_BtnEndTimeMonday.setVisibility(View.INVISIBLE);
                m_TxtEndTimeMonday.setText(time);
                m_TxtEndTimeMonday.setVisibility(View.VISIBLE);
            } else {
                //Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    public void setTuesdayTime(int i_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String time = convertToStringFormat(i_Hour, i_Minute);

        if(i_IsStartTime)
        {
            m_BtnEndTimeTuesday.setVisibility(View.INVISIBLE);
            m_TxtEndTimeTuesday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndTimeTuesday.getText().toString())) {
                m_BtnStartTimeTuesday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeTuesday.setText(time);
                m_TxtStartTimeTuesday.setVisibility(View.VISIBLE);
            } else {
                m_TxtStartTimeTuesday.setError(errorMessage);
                //Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }
        else {
            m_BtnStartTimeTuesday.setVisibility(View.INVISIBLE);
            m_TxtStartTimeTuesday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeTuesday.getText().toString(), time)) {
                m_BtnEndTimeTuesday.setVisibility(View.INVISIBLE);
                m_TxtEndTimeTuesday.setText(time);
                m_TxtEndTimeTuesday.setVisibility(View.VISIBLE);
            } else {

                //Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void setWednesdayTime(int i_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String time = convertToStringFormat(i_Hour, i_Minute);

        if(i_IsStartTime)
        {
            m_BtnEndTimeWednesday.setVisibility(View.INVISIBLE);
            m_TxtEndTimeWednesday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndTimeWednesday.getText().toString())) {
                m_BtnStartTimeWednesday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeWednesday.setText(time);
                m_TxtStartTimeWednesday.setVisibility(View.VISIBLE);
            } else {
                //Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }
        else {
            m_BtnStartTimeWednesday.setVisibility(View.INVISIBLE);
            m_TxtStartTimeWednesday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(
                    m_TxtStartTimeWednesday.getText().toString(), time)) {
                m_BtnEndTimeWednesday.setVisibility(View.INVISIBLE);
                m_TxtEndTimeWednesday.setText(time);
                m_TxtEndTimeWednesday.setVisibility(View.VISIBLE);
            } else {
                //Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    public void setThursdayTime(int i_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String time = convertToStringFormat(i_Hour, i_Minute);

        if(i_IsStartTime)
        {
            m_BtnEndTimeThursday.setVisibility(View.INVISIBLE);
            m_TxtEndTimeThursday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndTimeThursday.getText().toString())) {
                m_BtnStartTimeThursday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeThursday.setText(time);
                m_TxtStartTimeThursday.setVisibility(View.VISIBLE);
            } else {
                //Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }
        else {
            m_BtnStartTimeThursday.setVisibility(View.INVISIBLE);
            m_TxtStartTimeThursday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeThursday.getText().toString(), time)) {
                m_BtnEndTimeThursday.setVisibility(View.INVISIBLE);
                m_TxtEndTimeThursday.setText(time);
                m_TxtEndTimeThursday.setVisibility(View.VISIBLE);
            } else {
                //Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    public void setFridayTime(int i_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String time = convertToStringFormat(i_Hour, i_Minute);

        if(i_IsStartTime)
        {
            m_BtnEndTimeFriday.setVisibility(View.INVISIBLE);
            m_TxtEndTimeFriday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndTimeFriday.getText().toString())) {
                m_BtnStartTimeFriday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeFriday.setText(time);
                m_TxtStartTimeFriday.setVisibility(View.VISIBLE);
            } else {
                //Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }
        else {
            m_BtnStartTimeFriday.setVisibility(View.INVISIBLE);
            m_TxtStartTimeFriday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeFriday.getText().toString(), time)) {
                m_BtnEndTimeFriday.setVisibility(View.INVISIBLE);
                m_TxtEndTimeFriday.setText(time);
                m_TxtEndTimeFriday.setVisibility(View.VISIBLE);
            } else {
                //Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    public void setSaturdayTime(int i_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String time = convertToStringFormat(i_Hour, i_Minute);

        if(i_IsStartTime)
        {
            m_BtnEndTimeSaturday.setVisibility(View.INVISIBLE);
            m_TxtEndTimeSaturday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndTimeSaturday.getText().toString())) {
                m_BtnStartTimeSaturday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeSaturday.setText(time);
                m_TxtStartTimeSaturday.setVisibility(View.VISIBLE);
            } else {
                //Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }
        else {
            m_BtnStartTimeSaturday.setVisibility(View.INVISIBLE);
            m_TxtStartTimeSaturday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeSaturday.getText().toString(), time)) {
                m_BtnEndTimeSaturday.setVisibility(View.INVISIBLE);
                m_TxtEndTimeSaturday.setText(time);
                m_TxtEndTimeSaturday.setVisibility(View.VISIBLE);
            } else {
                //Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void setActivityContent()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        m_BtnSubjects = findViewById(R.id.txt_subjects_expert_settings);
        m_BtnSubjects.setOnClickListener(this);
        m_FabNext = findViewById(R.id.fab_next_of_expert_settings);
        m_FabNext.setOnClickListener(this);
        m_SwitchAlwaysAvailable = findViewById(R.id.switch_always_available);
        m_SwitchAlwaysAvailable.setOnCheckedChangeListener(this);
        m_SwitchNotDisturb = findViewById(R.id.switch_never_available);
        m_SwitchNotDisturb.setOnCheckedChangeListener(this);

        setSwitchesOfWeekDays();
        setTimePickButtons();
        setTimePickTexts();
    }

    private void setSwitchesOfWeekDays()
    {
        m_SwitchSunday = findViewById(R.id.switch_sunday);
        m_SwitchSunday.setOnClickListener(this);
        m_SwitchMonday = findViewById(R.id.switch_monday);
        m_SwitchMonday.setOnClickListener(this);
        m_SwitchTuesday = findViewById(R.id.switch_tuesday);
        m_SwitchTuesday.setOnClickListener(this);
        m_SwitchWednesday = findViewById(R.id.switch_wednesday);
        m_SwitchWednesday.setOnClickListener(this);
        m_SwitchThursday = findViewById(R.id.switch_thursday);
        m_SwitchThursday.setOnClickListener(this);
        m_SwitchFriday = findViewById(R.id.switch_friday);
        m_SwitchFriday.setOnClickListener(this);
        m_SwitchSaturday = findViewById(R.id.switch_saturday);
        m_SwitchSaturday.setOnClickListener(this);
    }

    private void setTimePickButtons()
    {
        m_BtnStartTimeSunday = findViewById(R.id.btn_start_time_sunday);
        m_BtnStartTimeSunday.setOnClickListener(this);
        m_BtnStartTimeMonday = findViewById(R.id.btn_start_time_monday);
        m_BtnStartTimeMonday.setOnClickListener(this);
        m_BtnStartTimeTuesday = findViewById(R.id.btn_start_time_tuesday);
        m_BtnStartTimeTuesday.setOnClickListener(this);
        m_BtnStartTimeWednesday = findViewById(R.id.btn_start_time_wednesday);
        m_BtnStartTimeWednesday.setOnClickListener(this);
        m_BtnStartTimeThursday = findViewById(R.id.btn_start_time_thursday);
        m_BtnStartTimeThursday.setOnClickListener(this);
        m_BtnStartTimeFriday = findViewById(R.id.btn_start_time_friday);
        m_BtnStartTimeFriday.setOnClickListener(this);
        m_BtnStartTimeSaturday = findViewById(R.id.btn_start_time_saturday);
        m_BtnStartTimeSaturday.setOnClickListener(this);

        m_BtnEndTimeSunday = findViewById(R.id.btn_end_time_sunday);
        m_BtnEndTimeSunday.setOnClickListener(this);
        m_BtnEndTimeMonday = findViewById(R.id.btn_end_time_monday);
        m_BtnEndTimeMonday.setOnClickListener(this);
        m_BtnEndTimeTuesday = findViewById(R.id.btn_end_time_tuesday);
        m_BtnEndTimeTuesday.setOnClickListener(this);
        m_BtnEndTimeWednesday = findViewById(R.id.btn_end_time_wednesday);
        m_BtnEndTimeWednesday.setOnClickListener(this);
        m_BtnEndTimeThursday = findViewById(R.id.btn_end_time_thursday);
        m_BtnEndTimeThursday.setOnClickListener(this);
        m_BtnEndTimeFriday = findViewById(R.id.btn_end_time_friday);
        m_BtnEndTimeFriday.setOnClickListener(this);
        m_BtnEndTimeSaturday = findViewById(R.id.btn_end_time_saturday);
        m_BtnEndTimeSaturday.setOnClickListener(this);
    }

    private void setTimePickTexts()
    {
        m_TxtStartTimeSunday = findViewById(R.id.txt_start_time_sunday);
        m_TxtStartTimeSunday.setOnClickListener(this);
        m_TxtStartTimeMonday = findViewById(R.id.txt_start_time_monday);
        m_TxtStartTimeMonday.setOnClickListener(this);
        m_TxtStartTimeTuesday = findViewById(R.id.txt_start_time_tuesday);
        m_TxtStartTimeTuesday.setOnClickListener(this);
        m_TxtStartTimeWednesday = findViewById(R.id.txt_start_time_wednesday);
        m_TxtStartTimeWednesday.setOnClickListener(this);
        m_TxtStartTimeThursday = findViewById(R.id.txt_start_time_thursday);
        m_TxtStartTimeThursday.setOnClickListener(this);
        m_TxtStartTimeFriday = findViewById(R.id.txt_start_time_friday);
        m_TxtStartTimeFriday.setOnClickListener(this);
        m_TxtStartTimeSaturday = findViewById(R.id.txt_start_time_saturday);
        m_TxtStartTimeSaturday.setOnClickListener(this);

        m_TxtEndTimeSunday = findViewById(R.id.txt_end_time_sunday);
        m_TxtEndTimeSunday.setOnClickListener(this);
        m_TxtEndTimeMonday = findViewById(R.id.txt_end_time_monday);
        m_TxtEndTimeMonday.setOnClickListener(this);
        m_TxtEndTimeTuesday = findViewById(R.id.txt_end_time_tuesday);
        m_TxtEndTimeTuesday.setOnClickListener(this);
        m_TxtEndTimeWednesday = findViewById(R.id.txt_end_time_wednesday);
        m_TxtEndTimeWednesday.setOnClickListener(this);
        m_TxtEndTimeThursday = findViewById(R.id.txt_end_time_thursday);
        m_TxtEndTimeThursday.setOnClickListener(this);
        m_TxtEndTimeFriday = findViewById(R.id.txt_end_time_friday);
        m_TxtEndTimeFriday.setOnClickListener(this);
        m_TxtEndTimeSaturday = findViewById(R.id.txt_end_time_saturday);
        m_TxtEndTimeSaturday.setOnClickListener(this);
    }

    private void startHomeScreenActivity()
    {
        Intent IntentHomeScreen = new Intent
                (ActivityExpertSettings.this, ActivityHomeScreen.class);
        startActivity(IntentHomeScreen);
    }

}
