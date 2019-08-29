package com.studymobile.advisos.Models;

public class Rating
{
    private long mVotersNum;
    private float mAvgRating; /* between 1 to 5 */

    public Rating(){}
    public Rating(long i_VotersNum, float i_AvgRating)
    {
        this.setVotersNum(i_VotersNum);
        this.setAvgRating(i_AvgRating);
    }

    public long getVotersNum()
    {
        return mVotersNum;
    }

    public float getAvgRating()
    {
        return mAvgRating;
    }

    public void setVotersNum(long i_VotersNum)
    {
        this.mVotersNum = i_VotersNum;
    }

    public void setAvgRating(float i_AvgRating)
    {
        this.mAvgRating = i_AvgRating;
    }

    public void Rate(int i_Score)
    {
        if(i_Score < 1 || i_Score > 5)
        {
            throw new IllegalArgumentException (String.format("A user cannot be rated with a score of %d", i_Score));
        }

        mVotersNum++;
        mAvgRating += 1 / mVotersNum * i_Score;
    }
}
