package com.studymobile.advisos.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
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
import com.studymobile.advisos.Adapters.AdapterTabsAccessor;
import com.studymobile.advisos.Interfaces.ItemClickListener;
import com.studymobile.advisos.Models.Day;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.Models.SubjectUser;
import com.studymobile.advisos.Models.User;
import com.studymobile.advisos.Models.UserAvailability;
import com.studymobile.advisos.Models.UserLocation;
import com.studymobile.advisos.Models.Week;
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderSubject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActivityHomeScreen extends AppCompatActivity implements
        TextWatcher, MaterialSearchBar.OnSearchActionListener,
        NavigationView.OnNavigationItemSelectedListener
{
    private static final String RES = "android.resource://";
    private static final String DRAWABLE_DEFAULT = "/drawable/img_advisos";
    private static final String DEFAULT = "Default";
    private static final int IMG_REQ = 1;
    private static final int REQ_CODE = 2;

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

    private Dialog m_ConfirmDialog;

    private FirebaseRecyclerOptions<Subject> m_Options;
    private FirebaseRecyclerAdapter<Subject, ViewHolderSubject> m_Adapter;

    private Toolbar mToolBar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private AdapterTabsAccessor mAdapterTabsAccessor;

    private NavigationView m_NavigationView;
    private DrawerLayout m_DrawerLayout;
    private MaterialSearchBar m_SearchBar1;


    private FirebaseAuth m_Auth;
    private FirebaseUser m_CurrentUser;
    private FirebaseDatabase m_Database;
    private DatabaseReference m_UsersRef;
    private FirebaseStorage m_Storage;
    private boolean m_IsOnline;
    private Menu m_NavigationMenu;

    private ViewPager.OnPageChangeListener m_ViewPager;
    private FloatingActionButton m_FabCreate;


//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    protected Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final String TAG = "USER LOCATION";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;



    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getCurrentUserLocationOnGrantedAccess();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getCurrentUserLocationOnGrantedAccess();
            } else {
                // Permission denied.
//                Intent intent = new Intent(this, ActivityHomeScreen.class);
//                startActivity(intent);
            }
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions()
    {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        return permissionState == PackageManager.PERMISSION_GRANTED;
    }
    private void getCurrentUserLocationOnGrantedAccess()
    {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            final UserLocation location = new UserLocation
                                    (mLastLocation.getLatitude(),mLastLocation.getLongitude(), mLastLocation.getAltitude());
                            final DatabaseReference reference = m_Database.getReference("Users");
                            reference.child(m_CurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {
                                    reference.child(m_CurrentUser.getUid())
                                            .child("userLocation").setValue(location);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                            //getLastLocation();

                        }
                    }
                });
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(ActivityHomeScreen.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.textwarn, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<






    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        setFirebaseData();
        setActivityContent();
        setFabVisibilityForTabs();
        getUserSubjectsFromDB();
        setDialogSubjectsList();
        setDialogCreateSubject();
    }

    private void setFabVisibilityForTabs()
    {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        m_FabCreate.show();
                        break;
                    default:
                        m_FabCreate.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setFirebaseData()
    {
        m_Auth = FirebaseAuth.getInstance();
        m_CurrentUser = m_Auth.getCurrentUser();
        m_Database = FirebaseDatabase.getInstance();
        m_UsersRef = m_Database.getReference("Users");
        m_Storage = FirebaseStorage.getInstance();
    }

    private void setActivityContent()
    {
        //        mToolBar = findViewById(R.id.toolbar_of_homes_screen);
//        setSupportActionBar(mToolBar);
//        getSupportActionBar().setTitle("AdviSOS");

        mViewPager = findViewById(R.id.pager_of_home_screen);
        mAdapterTabsAccessor = new AdapterTabsAccessor(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapterTabsAccessor);

        mTabLayout = findViewById(R.id.tabs_of_home_screen);
        mTabLayout.setupWithViewPager(mViewPager);

        m_SearchBar = findViewById(R.id.search_bar_of_home_screen);
//        m_SearchBar.setCardViewElevation(10);
        m_SearchBar.setMaxSuggestionCount(5);
        m_SearchBar.addTextChangeListener(this);
        m_SearchBar.setOnSearchActionListener(this);

        m_DrawerLayout = findViewById(R.id.drawer_layout_home_screen);
        m_NavigationView = findViewById(R.id.nav_view_home_screen);
        m_NavigationView.setNavigationItemSelectedListener(this);
        m_NavigationMenu = m_NavigationView.getMenu();

        m_FabCreate = findViewById(R.id.fab_create_of_activity_home_screen);
        m_FabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCreateSubject();
            }
        });

        //>>
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //<<
    }

    @Override
    public void onBackPressed() {
        if (m_DrawerLayout.isDrawerOpen(GravityCompat.START)) {
            m_DrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu i_Menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_of_home_screen, i_Menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem i_Item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = i_Item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(i_Item);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem i_MenuItem)
    {
        int id = i_MenuItem.getItemId();

        if (id == R.id.nav_is_online)
        {
            setIsOnline(true);
        }
        else if (id == R.id.nav_my_profile)
        {
            startUserDetailsActivity();
            m_DrawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (id == R.id.nav_availability)
        {
            startExpertSettingsActivity();
            m_DrawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (id == R.id.nav_create_new_subject)
        {
            showDialogCreateSubject();
            m_DrawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (id == R.id.nav_my_subjects)
        {
            showDialogSubjectsList();
            m_DrawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (id == R.id.nav_my_ratings)
        {
           startMyRatingsActivity();
            m_DrawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (id == R.id.nav_share)
        {
            share();
            m_DrawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (id == R.id.nav_logout)
        {
            logOut();
            m_DrawerLayout.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    private void setIsOnline(final boolean i_IsClicked)
    {
        m_UsersRef.child(m_CurrentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot) {
                        if (i_DataSnapshot.exists())
                        {
                            m_IsOnline = i_DataSnapshot.child("isOnline").getValue(boolean.class);
                            updateNavUI(i_IsClicked);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError i_DataSnapshot) {}
                });
    }

    private void updateNavUI(final boolean i_IsClicked)
    {
        MenuItem availability = m_NavigationMenu.findItem(R.id.nav_availability);

        if(i_IsClicked)
        {
            if (m_IsOnline)
            {
                availability.setTitle("Offline");
                availability.setIcon(R.drawable.ic_disabled_red);
                m_UsersRef.child(m_CurrentUser.getUid())
                        .child("isOnline").setValue(false);
            }else {
                availability.setTitle("Online");
                availability.setIcon(R.drawable.ic_checked_green);
                m_UsersRef.child(m_CurrentUser.getUid())
                        .child("isOnline").setValue(true);
            }
        } else{
            if (m_IsOnline)
            {
                availability.setTitle("Online");
                availability.setIcon(R.drawable.ic_checked_green);
                m_UsersRef.child(m_CurrentUser.getUid())
                        .child("isOnline").setValue(true);
            }else {
                availability.setTitle("Offline");
                availability.setIcon(R.drawable.ic_disabled_red);
                m_UsersRef.child(m_CurrentUser.getUid())
                        .child("isOnline").setValue(false);

            }
        }

    }

    private void startExpertSettingsActivity()
    {
        Intent IntentExpertConfig = new Intent
                (ActivityHomeScreen.this, ActivityExpertSettings.class);
        startActivity(IntentExpertConfig);
    }

    private void startUserDetailsActivity()
    {
        Intent IntentUserDetails = new Intent
                (ActivityHomeScreen.this, ActivityUserDetails.class);
        startActivity(IntentUserDetails);
    }

    private void startMyRatingsActivity()
    {
        //TODO
        Snackbar.make(findViewById(android.R.id.content),
                "Will be implemented soon", Snackbar.LENGTH_LONG).show();
    }

    private void share()
    {
        //TODO
        Snackbar.make(findViewById(android.R.id.content),
                "Ronen will implement it soon", Snackbar.LENGTH_LONG).show();
    }

    private void logOut()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(this, gso).signOut();
        LoginManager.getInstance().logOut();
        Intent IntentRegistration = new Intent
                (ActivityHomeScreen.this, ActivityRegistration.class);
        startActivity(IntentRegistration);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int i_Button) {
        switch (i_Button)
        {
            case MaterialSearchBar.BUTTON_NAVIGATION:
                m_DrawerLayout.openDrawer(Gravity.LEFT);
                break;
            case MaterialSearchBar.BUTTON_BACK:
                m_SearchBar.hideSuggestionsList();
                m_SearchBar.disableSearch();
                break;
        }
    }

    private void setDialogCreateSubject()
    {
        m_DialogCreateSubj = new Dialog(ActivityHomeScreen.this);
        m_DialogCreateSubj.setContentView(R.layout.dialog_create_a_subject);
        m_DialogCreateSubj.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        m_DialogImgView = m_DialogCreateSubj.findViewById(R.id.img_of_dialog_create_a_subj);
        m_DialogImgURI = Uri.parse(RES + getApplicationContext()
                .getPackageName() + DRAWABLE_DEFAULT);
        m_FieldSubjName = m_DialogCreateSubj.findViewById(R.id.field_subject_name_of_dialog_create_a_subj);
        m_FieldSubjDescription = m_DialogCreateSubj.findViewById(R.id.field_subject_description_of_dialog_create_a_subj);

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
        if(ContextCompat.checkSelfPermission(ActivityHomeScreen.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    ActivityHomeScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(ActivityHomeScreen.this,
                        "Please accept for required permission ", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(ActivityHomeScreen.this,
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
                    Toast.makeText(ActivityHomeScreen.this,
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
                                        Toast.makeText(ActivityHomeScreen.this,
                                                "The subject is created.",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ActivityHomeScreen.this,
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

    private void setDialogSubjectsList()
    {
        m_DialogSubjList = new Dialog(ActivityHomeScreen.this);
        m_DialogSubjList.setContentView(R.layout.dialog_subjects_list);
        m_DialogSubjList.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        m_SuggestionsList = new ArrayList<>();
//        m_SearchBar = findViewById(R.id.search_bar_of_dialog_subjects_list);
//        m_SearchBar.setCardViewElevation(10);
//        m_SearchBar.setMaxSuggestionCount(5);
//        m_SearchBar.addTextChangeListener(this);
//        m_SearchBar.setOnSearchActionListener(this);
        m_DialogRecyclerView = m_DialogSubjList.findViewById(R.id.recycler_view_of_dialog_subjects_list);
        m_DialogRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        m_DialogRecyclerView.setLayoutManager(layoutManager);
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
        m_ConfirmDialog = new Dialog(ActivityHomeScreen.this);
        m_ConfirmDialog.setContentView(R.layout.dialog_confirm);

        ImageButton closeBtn = m_ConfirmDialog.findViewById(R.id.btn_close_of_dialog_confirm);
        TextView fieldTitle = m_ConfirmDialog.findViewById(R.id.txt_title_of_dialog_confirm);
        TextView rightBtn = m_ConfirmDialog.findViewById(R.id.btn_right_of_dialog_confirm);
        TextView leftBtn = m_ConfirmDialog.findViewById(R.id.btn_left_of_dialog_confirm);

        closeBtn.setVisibility(View.VISIBLE);
        fieldTitle.setText("You'll be marked as an expert in the selected sections");
        rightBtn.setText("Confirm");
        leftBtn.setText("Cancel");

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabaseSubjectUsers();
                m_ConfirmDialog.dismiss();
                m_DialogSubjList.dismiss();
            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_ConfirmDialog.dismiss();
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_ConfirmDialog.dismiss();
            }
        });

        m_ConfirmDialog.show();
    }

    private void updateDatabaseSubjectUsers()
    {
        final DatabaseReference subjectRef =  m_Database.getReference("SubjectUsers");
        final String userID = m_CurrentUser.getUid();
        for (final String subjectName : m_SubjNamesSet)
        {
            if(m_SubjStateMap.get(subjectName) != null)
            {
                DatabaseReference userId = m_Database.getReference("Users");
                userId.child(m_CurrentUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot i_DataSnapshot) {
                                if (i_DataSnapshot.exists() )
                                {
                                    User databaseUser = i_DataSnapshot.getValue(User.class);
                                    String userName = databaseUser.getFirstName() + " " + databaseUser.getFamilyName();
                                    String userImgLink = databaseUser.getImgLink();

                                    if(m_SubjStateMap.get(subjectName))
                                    {
                                        subjectRef.child(subjectName).child(userID).child("isValid").setValue(true);
                                        subjectRef.child(subjectName).child(userID).child("subjectName").setValue(subjectName);
                                        subjectRef.child(subjectName).child(userID).child("userName").setValue(userName);
                                        subjectRef.child(subjectName).child(userID).child("userImgLink").setValue(userImgLink);
                                        subjectRef.child(subjectName).child(userID).child("userId").setValue(userID);

                                    }
                                    else{
                                        subjectRef.child(subjectName).child(userID).child("isValid").setValue(false);
                                        subjectRef.child(subjectName).child(userID).child("subjectName").setValue(subjectName);
                                        subjectRef.child(subjectName).child(userID).child("userName").setValue(userName);
                                        subjectRef.child(subjectName).child(userID).child("userImgLink").setValue(userImgLink);
                                        subjectRef.child(subjectName).child(userID).child("userId").setValue(userID);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError i_DataSnapshot) {}
                        });
            }
        }
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
}
