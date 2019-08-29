package com.studymobile.advisos.Services;

import com.studymobile.advisos.Ecxeptions.AlwaysAvailTryingDoNotDisturbException;
import com.studymobile.advisos.Ecxeptions.DoNotDisturbTryingToAlwaysAvailableException;
import com.studymobile.advisos.Ecxeptions.TimeNotValidColisionsException;

public class UserServices {

    private int mAvailableFrom;
    private int mAvailableTO;
    private boolean mIsAlwaysAvaileble =false;
    private boolean mDoNotDisturb =false;
    private int mMaxConversationsConcurrently;
    private boolean mNoConversationLimit; //TODO
    public UserServices() {}
    public UserServices(int from, int to, int i_maxConvers)throws TimeNotValidColisionsException {
        if(from >= to){
            throw new TimeNotValidColisionsException(from,to);
        }
        mAvailableFrom = from;
        mAvailableTO = to;
        mMaxConversationsConcurrently = i_maxConvers;
    }

    public int getmAvailableFrom() {
        return mAvailableFrom;
    }

    public int getmAvailableTO() {
        return mAvailableTO;
    }

    public void setmAvailableFrom(int mAvailableFrom) {

        this.mAvailableFrom = mAvailableFrom;
    }

    public void setmAvailableTO(int mAvailableTO) {
        this.mAvailableTO = mAvailableTO;
    }

    public void setmDoNotDisturb(boolean mDoNotDisturb)throws AlwaysAvailTryingDoNotDisturbException {
        if(mIsAlwaysAvaileble)
        {
            throw new AlwaysAvailTryingDoNotDisturbException();
        }
        this.mDoNotDisturb = mDoNotDisturb;
    }

    public int getmMaxConversationsConcurrently() {
        return mMaxConversationsConcurrently;
    }

    public void setmMaxConversationsConcurrently(int mMaxConversationsConcurrently) {
        this.mMaxConversationsConcurrently = mMaxConversationsConcurrently;
    }

    public void setmIsAlwaysAvaileble(boolean mIsAlwaysAvaileble) throws DoNotDisturbTryingToAlwaysAvailableException{
        if(mDoNotDisturb)
            throw new DoNotDisturbTryingToAlwaysAvailableException();
        this.mIsAlwaysAvaileble = mIsAlwaysAvaileble;
    }

}
