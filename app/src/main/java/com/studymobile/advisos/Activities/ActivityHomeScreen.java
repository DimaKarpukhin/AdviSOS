package com.studymobile.advisos.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.studymobile.advisos.Adapters.AdapterTabsAccessor;
import com.studymobile.advisos.R;

public class ActivityHomeScreen extends AppCompatActivity
{
    private Toolbar mToolBar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private AdapterTabsAccessor mAdapterTabsAccessor;

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
    }
}
