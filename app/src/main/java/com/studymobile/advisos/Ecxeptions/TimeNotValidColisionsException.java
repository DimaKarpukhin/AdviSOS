package com.studymobile.advisos.Ecxeptions;

public class TimeNotValidColisionsException extends Exception {

    private int mStartTime;
    private int mEndTime;
    private StringBuilder mMessage;

    public TimeNotValidColisionsException(int i_from, int i_to)
    {
        mStartTime = i_from;
        mEndTime = i_to;
    }

    @Override
    public String getMessage() {
        this.mMessage = new StringBuilder();
        this.mMessage.append("The times provided are not valid and collide, " +
                "availabilty set from" + mStartTime + " until " + mEndTime);
        return mMessage.toString();
    }


}
