package com.studymobile.advisos.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.studymobile.advisos.Adapters.AdapterTabsAccessor;
import com.studymobile.advisos.Models.UserLocation;
import com.studymobile.advisos.R;

public class ActivityHomeScreen extends AppCompatActivity implements
        TextWatcher, MaterialSearchBar.OnSearchActionListener,
        NavigationView.OnNavigationItemSelectedListener
{
    private Toolbar mToolBar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private AdapterTabsAccessor mAdapterTabsAccessor;

    private NavigationView m_NavigationView;
    private DrawerLayout m_DrawerLayout;
    private MaterialSearchBar m_SearchBar;


    private FirebaseAuth m_Auth;
    private FirebaseUser m_CurrentUser;
    private FirebaseDatabase m_Database;
    DatabaseReference m_UsersRef;
    private FirebaseStorage m_Storage;
    private boolean m_IsOnline;
    private Menu m_NavigationMenu;



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

        setAvailability(false);

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

        if (id == R.id.nav_availability)
        {
            setAvailability(true);
        }
        else if (id == R.id.nav_my_profile)
        {
            startUserDetailsActivity();
            m_DrawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (id == R.id.nav_settings)
        {
            startExpertSettingsActivity();
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
        else if(id == R.id.nav_closed_chats)
        {
            myClosedChats();
            m_DrawerLayout.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    private void myClosedChats() {

    }

    private void setAvailability(final boolean i_IsClicked)
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
}
