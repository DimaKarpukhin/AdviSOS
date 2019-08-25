package com.studymobile.advisos.Models;

public class Week
{
    private Day mSunday;
    private Day mMonday;
    private Day mTuesday;
    private Day mWednesday;
    private Day mThursday;
    private Day mFriday;
    private Day mSaturday;

    public Week(){}

    public Week(Day i_Sunday, Day i_Monday, Day i_Tuesday, Day i_Wednesday, Day i_Thursday, Day i_Friday, Day i_Saturday)
    {
        this.mSunday = i_Sunday;
        this.mMonday = i_Monday;
        this.mTuesday = i_Tuesday;
        this.mWednesday = i_Wednesday;
        this.mThursday = i_Thursday;
        this.mFriday = i_Friday;
        this.mSaturday = i_Saturday;
    }

    public Day GetSunday()
    {
        return mSunday;
    }

    public Day GetMonday()
    {
        return mMonday;
    }

    public Day GetTuesday()
    {
        return mTuesday;
    }

    public Day GetWednesday()
    {
        return mWednesday;
    }

    public Day GetThursday()
    {
        return mThursday;
    }

    public Day GetFriday()
    {
        return mFriday;
    }

    public Day GetSaturday()
    {
        return mSaturday;
    }

    public void SetSunday(Day i_Sunday)
    {
        //TODO
        this.mSunday = i_Sunday;
    }

    public void SetMonday(Day i_Monday)
    {
        //TODO
        this.mMonday = i_Monday;
    }

    public void SetTuesday(Day i_Tuesday)
    {
        //TODO
        this.mTuesday = i_Tuesday;
    }

    public void SetWednesday(Day i_Wednesday)
    {
        //TODO
        this.mWednesday = i_Wednesday;
    }

    public void SetThursday(Day i_Thursday)
    {
        //TODO
        this.mThursday = i_Thursday;
    }

    public void SetFriday(Day i_Friday)
    {
        //TODO
        this.mFriday = i_Friday;
    }

    public void SetSaturday(Day i_Saturday)
    {
        //TODO
        this.mSaturday = i_Saturday;
    }
}
