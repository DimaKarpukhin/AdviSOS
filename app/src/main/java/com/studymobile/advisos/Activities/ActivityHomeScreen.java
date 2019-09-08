package com.studymobile.advisos.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.studymobile.advisos.Adapters.AdapterTabsAccessor;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_home_screen);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onButtonClicked(int i_Button) {
        switch (i_Button)
        {
            case MaterialSearchBar.BUTTON_NAVIGATION:
                m_DrawerLayout.openDrawer(Gravity.LEFT);
                //  Toast.makeText(this, "NAV", Toast.LENGTH_SHORT).show();
                break;
            case MaterialSearchBar.BUTTON_BACK:
                m_SearchBar.hideSuggestionsList();
                m_DrawerLayout.closeDrawer(GravityCompat.START);
                //   Toast.makeText(this, "BACK", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
