package com.studymobile.advisos.Models;

import com.studymobile.advisos.Ecxeptions.AlwaysAvailTryingDoNotDisturbException;
import com.studymobile.advisos.Ecxeptions.DoNotDisturbTryingToAlwaysAvailableException;
import com.studymobile.advisos.Ecxeptions.TimeNotValidColisionsException;

import java.util.ArrayList;

public class UserAvailability
{
    private boolean mIsAlwaysAvailable;
    private boolean mIsNotDisturb;
    private Week mWeekAvailability;
    private int mMaxConcurrentChats;
    private boolean mIsNoNumChatsLimit;

    public UserAvailability(){}

    public UserAvailability(boolean i_IsAlwaysAvailable, boolean i_IsNotDisturb, Week i_Week,
                            int i_MaxConcurrentChats, boolean i_IsNoNumChatsLimit)
    {
        this.setIsAlwaysAvailable(i_IsAlwaysAvailable);
        this.setIsNotDisturb(i_IsNotDisturb);
        this.setWeekAvailability(i_Week);
        this.setMaxConcurrentChats(i_MaxConcurrentChats);
        this.setIsNoNumChatsLimit(i_IsNoNumChatsLimit);
    }

    public boolean getIsAlwaysAvailable()
    {
        return mIsAlwaysAvailable;
    }

    public boolean getIsNotDisturb()
    {
        return mIsNotDisturb;
    }

    public Week getWeekAvailability()
    {
        return mWeekAvailability;
    }

    public int getMaxConcurrentChats()
    {
        return mMaxConcurrentChats;
    }

    public boolean getIsNoNumChatsLimit()
    {
        return mIsNoNumChatsLimit;
    }

    public void setIsAlwaysAvailable(boolean i_IsAlwaysAvailable)
    {
        this.mIsAlwaysAvailable = i_IsAlwaysAvailable;
    }

    public void setIsNotDisturb(boolean i_IsNotDisturb)
    {
        this.mIsNotDisturb = i_IsNotDisturb;
    }

    public void setWeekAvailability(Week i_Week) {
        this.mWeekAvailability = i_Week;
    }

    public void setMaxConcurrentChats(int i_MaxConcurrentChats)
    {
        this.mMaxConcurrentChats = i_MaxConcurrentChats;
    }

    public void setIsNoNumChatsLimit(boolean i_IsNoNumChatsLimit)
    {
        this.mIsNoNumChatsLimit = i_IsNoNumChatsLimit;
    }

    //    public UserServices() {}
//    public UserServices(int from, int to, int i_maxConvers)throws TimeNotValidColisionsException {
//        if(from >= to){
//            throw new TimeNotValidColisionsException(from,to);
//        }
//        mAvailableFrom = from;
//        mAvailableTO = to;
//        mMaxConversationsConcurrently = i_maxConvers;
//    }
//
//    public int getmAvailableFrom() {
//        return mAvailableFrom;
//    }
//
//    public int getmAvailableTO() {
//        return mAvailableTO;
//    }
//
//    public void setmAvailableFrom(int mAvailableFrom) {
//
//        this.mAvailableFrom = mAvailableFrom;
//    }
//
//    public void setmAvailableTO(int mAvailableTO) {
//        this.mAvailableTO = mAvailableTO;
//    }
//
//    public void setmDoNotDisturb(boolean mDoNotDisturb)throws AlwaysAvailTryingDoNotDisturbException {
//        if(mIsAlwaysAvailable)
//        {
//            throw new AlwaysAvailTryingDoNotDisturbException();
//        }
//        this.mDoNotDisturb = mDoNotDisturb;
//    }
//
//    public int getmMaxConversationsConcurrently() {
//        return mMaxConversationsConcurrently;
//    }
//
//    public void setmMaxConversationsConcurrently(int mMaxConversationsConcurrently) {
//        this.mMaxConversationsConcurrently = mMaxConversationsConcurrently;
//    }
//
//    public void setmIsAlwaysAvailable(boolean mIsAlwaysAvailable) throws DoNotDisturbTryingToAlwaysAvailableException{
//        if(mDoNotDisturb)
//            throw new DoNotDisturbTryingToAlwaysAvailableException();
//        this.mIsAlwaysAvailable = mIsAlwaysAvailable;
//    }

}
