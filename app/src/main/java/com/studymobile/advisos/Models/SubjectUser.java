package com.studymobile.advisos.Models;

import android.util.Log;

public class SubjectUser
{
    private String mSubjectName;
    private String mUserName;
    private String mUserImgLink;
    private String mUserId;
    private Rating mRating;
    private boolean mIsValid; //?

    public SubjectUser(){}

    public SubjectUser(String i_UserId, Rating i_Rating, boolean i_Valid,
                       String i_UserName, String i_UserImgLink, String i_SubjectName)
    {
        this.setUserId(i_UserId);
        this.setRating(i_Rating);
        this.setIsValid(i_Valid);
        this.setSubjectName(i_SubjectName);
        this.setUserName(i_UserName);
        this.setUserImgLink(i_UserImgLink);
    }

    public void Rate(float i_Score)
    {
        if(mRating == null)
        {
            mRating = new Rating(0, 0);
        }

        if (i_Score < 0 || i_Score > 5) {
            throw new IllegalArgumentException
                    (String.format("A user cannot be rated with a score of %d", i_Score));
        }
        float avgRating = mRating.getAvgRating();
        long votersNum = mRating.getVotersNum();
        votersNum++;
        avgRating = (avgRating * votersNum + i_Score)/(votersNum);
        mRating.setAvgRating(avgRating);
        mRating.setVotersNum(votersNum);

        Log.e("avgRating", String.valueOf(mRating.getAvgRating()));
        Log.e("votersNum", String.valueOf(mRating.getVotersNum()));
    }

    public String getUserName()
    {
        return mUserName;
    }

    public String getUserImgLink()
    {
        return mUserImgLink;
    }

    public boolean getIsValid() {
        return mIsValid;
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

    public void setUserName(String i_UserName)
    {
        this.mUserName = i_UserName;
    }

    public void setUserImgLink(String i_UserImgLink)
    {
        this.mUserImgLink = i_UserImgLink;
    }

    public void setIsValid(boolean i_Valid) {
        mIsValid = i_Valid;
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
