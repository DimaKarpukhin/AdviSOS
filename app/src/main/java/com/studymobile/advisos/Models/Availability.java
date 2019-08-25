package com.studymobile.advisos.Models;

import com.studymobile.advisos.Ecxeptions.AlwaysAvailTryingDoNotDisturbException;
import com.studymobile.advisos.Ecxeptions.DoNotDisturbTryingToAlwaysAvailableException;
import com.studymobile.advisos.Ecxeptions.TimeNotValidColisionsException;

public class Availability
{
    private boolean mIsAlwaysAvailable;
    private boolean mIsNotDisturb;
    private Week mWeek;
    private int mMaxConcurrentChats;
    private boolean mIsNoNumChatsLimit;

    public Availability(){}

    public Availability(boolean i_IsAlwaysAvailable, boolean i_IsNotDisturb, Week i_Week,
                        int i_MaxConcurrentChats, boolean i_IsNoNumChatsLimit)
    {
        this.SetIsAlwaysAvailable(i_IsAlwaysAvailable);
        this.SetIsNotDisturb(i_IsNotDisturb);
        this.SetWeek(i_Week);
        this.SetMaxConcurrentChats(i_MaxConcurrentChats);
        this.SetIsNoNumChatsLimit(i_IsNoNumChatsLimit);
    }

    public boolean GetIsAlwaysAvailable()
    {
        return mIsAlwaysAvailable;
    }

    public boolean GetIsNotDisturb()
    {
        return mIsNotDisturb;
    }

    public Week GetWeek()
    {
        return mWeek;
    }

    public int GetMaxConcurrentChats()
    {
        return mMaxConcurrentChats;
    }

    public boolean GetIsNoNumChatsLimit()
    {
        return mIsNoNumChatsLimit;
    }

    public void SetIsAlwaysAvailable(boolean i_IsAlwaysAvailable)
    {
        this.mIsAlwaysAvailable = i_IsAlwaysAvailable;
    }

    public void SetIsNotDisturb(boolean i_IsNotDisturb)
    {
        this.mIsNotDisturb = i_IsNotDisturb;
    }

    public void SetWeek(Week i_Week) {
        this.mWeek = i_Week;
    }

    public void SetMaxConcurrentChats(int i_MaxConcurrentChats)
    {
        this.mMaxConcurrentChats = i_MaxConcurrentChats;
    }

    public void SetIsNoNumChatsLimit(boolean i_IsNoNumChatsLimit)
    {
        this.mIsNoNumChatsLimit = i_IsNoNumChatsLimit;
    }

    //    public UserAvailability() {}
//    public UserAvailability(int from, int to, int i_maxConvers)throws TimeNotValidColisionsException {
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
