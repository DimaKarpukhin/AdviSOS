package com.studymobile.advisos.Adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.studymobile.advisos.Fragments.FragmentAdviceMe;
import com.studymobile.advisos.Fragments.FragmentAdviceOthers;
import com.studymobile.advisos.Fragments.FragmentChatRequests;
import com.studymobile.advisos.Fragments.FragmentPossibleActiveChats;
import com.studymobile.advisos.Fragments.FragmentPossibleClosedChats;
import com.studymobile.advisos.Fragments.FragmentSubjects;

public class AdapterPossibleChatsTabsAccessor extends FragmentPagerAdapter {

    public AdapterPossibleChatsTabsAccessor(FragmentManager i_Fragment)
    {
        super(i_Fragment);
    }

    @Override
    public Fragment getItem(int i_Position)
    {
        switch (i_Position){
            case 0:
                return new FragmentPossibleClosedChats();
            case 1:
                return new FragmentPossibleActiveChats();
            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int i_Position)
    {
        switch (i_Position){
            case 0:
                return "Closed chats similar";
            case 1:
                return "Active chats similar";
            default:
                return null;
        }
    }
}
