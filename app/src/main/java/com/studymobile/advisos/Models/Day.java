package com.studymobile.advisos.Models;

import com.studymobile.advisos.Enums.eWeekDay;

public class Day
{
    private eWeekDay mDay;
    private String mStartTime;
    private String mEndTime;
    private boolean mIsAvailable;

    public Day(){}
    public Day(eWeekDay i_WeekDay, String i_StartTime, String i_EndTime, boolean i_IsAvailable)
    {
        this.mDay = i_WeekDay;
        this.mStartTime = i_StartTime;
        this.mEndTime = i_EndTime;
        this.mIsAvailable = i_IsAvailable;
    }

    public eWeekDay GetWeekDay()
    {
        return mDay;
    }

    public String GetStartTime()
    {
        return mStartTime;
    }

    public String GetEndTime()
    {
        return mEndTime;
    }

    public boolean GetIsAvailable()
    {
        return mIsAvailable;
    }

    public void SetWeekDay(eWeekDay i_Day)
    {
        this.mDay = i_Day;
    }

    public void SetStartTime(String i_StartTime)
    {
        this.mStartTime = i_StartTime;
    }

    public void SetEndTime(String i_EndTime)
    {
        this.mEndTime = i_EndTime;
    }

    public void SetIsAvailable(boolean i_IsAvailable)
    {
        this.mIsAvailable = i_IsAvailable;
    }
}
