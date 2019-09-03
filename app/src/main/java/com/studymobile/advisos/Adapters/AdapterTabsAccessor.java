package com.studymobile.advisos.Adapters;

import com.studymobile.advisos.Fragments.FragmentAdviceMe;
import com.studymobile.advisos.Fragments.FragmentAdviceOthers;
import com.studymobile.advisos.Fragments.FragmentSubjects;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AdapterTabsAccessor extends FragmentPagerAdapter
{

    public AdapterTabsAccessor(FragmentManager i_Fragment)
    {
        super(i_Fragment);
    }

    @Override
    public Fragment getItem(int i_Position)
    {
        switch (i_Position){
            case 0:
                return new FragmentSubjects();
            case 1:
                return new FragmentAdviceMe();
            case 2:
                return new FragmentAdviceOthers();
            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int i_Position)
    {
        switch (i_Position){
            case 0:
                return "Subjects";
            case 1:
                return "Advice me";
            case 2:
                return "Advice Others";
            default:
                return null;
        }
    }
}
