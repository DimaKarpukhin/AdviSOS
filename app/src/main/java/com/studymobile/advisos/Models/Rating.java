package com.studymobile.advisos.Models;

public class Rating
{
    private long mVotersNum;
    private float mAvgRating;

    public Rating(){}
    public Rating(long i_VotersNum, float i_AvgRating)
    {
        this.SetVotersNum(i_VotersNum);
        this.SetAvgRating(i_AvgRating);
    }

    public long GetVotersNum()
    {
        return mVotersNum;
    }

    public float GetAvgRating()
    {
        return mAvgRating;
    }

    public void SetVotersNum(long i_VotersNum)
    {
        this.mVotersNum = i_VotersNum;
    }

    public void SetAvgRating(float i_AvgRating)
    {
        this.mAvgRating = i_AvgRating;
    }
}
