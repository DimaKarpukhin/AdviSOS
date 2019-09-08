package com.studymobile.advisos.Models;

public class SubjectUser
{
    private String mSubjectName;
    private String mUserId;
    private Rating mRating;
    private boolean mIsValid; //?

    public SubjectUser(){}

    public SubjectUser(String i_UserId, Rating i_Rating, boolean i_Valid, String i_SubjectName)
    {
        this.setUserId(i_UserId);
        this.setRating(i_Rating);
        this.setIsValid(i_Valid);
        this.setSubjectName(i_SubjectName);
    }

    public boolean getIsValid() {
        return mIsValid;
    }

    public void setIsValid(boolean i_Valid) {
        mIsValid = i_Valid;
    }


    public String getUserId()
    {
        return mUserId;
    }

    public String getSubjectName()
    {
        return mSubjectName;
    }

    public Rating getRating()
    {
        return mRating;
    }

    public void setUserId(String i_UserId)
    {
        this.mUserId = i_UserId;
    }

    public void setSubjectName(String i_SubjectName)
    {
        this.mSubjectName = i_SubjectName;
    }

    public void setRating(Rating i_Rating)
    {
        this.mRating = i_Rating;
    }
}
