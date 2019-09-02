package com.studymobile.advisos.Models;

public class SubjectUser
{
    
    private String mUserId;
    private Rating mRating;
    private boolean mIsChecked; //?

    public SubjectUser(){}

    public SubjectUser(String i_UserId, Rating i_Rating)
    {
        this.setUserId(i_UserId);
        this.setRating(i_Rating);
    }

    public boolean getChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean i_Checked) {
        mIsChecked = i_Checked;
    }


    public String getUserId()
    {
        return mUserId;
    }

    public Rating getRating()
    {
        return mRating;
    }

    public void setUserId(String i_UserId)
    {
        this.mUserId = mUserId;
    }

    public void setRating(Rating i_Rating)
    {
        this.mRating = i_Rating;
    }
}
