package com.studymobile.advisos.Models;

import com.studymobile.advisos.Enums.eWeekDay;

public class Day
{
    private eWeekDay mDay;
    private String mStartTime = "00:00";
    private String mEndTime = "23:59";
    private boolean mIsAvailable = false;

    public Day(){}
    public Day(eWeekDay i_Day, String i_StartTime, String i_EndTime, boolean i_IsAvailable)
    {
        this.setDay(i_Day);
        this.setStartTime(i_StartTime);
        this.setEndTime(i_EndTime);
        this.setIsAvailable(i_IsAvailable);
    }

    public eWeekDay getDay()
    {
        return mDay;
    }

    public String getStartTime()
    {
        return mStartTime;
    }

    public String getEndTime()
    {
        return mEndTime;
    }

    public boolean getIsAvailable()
    {
        return mIsAvailable;
    }

    public void setDay(eWeekDay i_Day)
    {
        this.mDay = i_Day;
    }

    public void setStartTime(String i_StartTime)
    {
        this.mStartTime = i_StartTime;
    }

    public void setEndTime(String i_EndTime)
    {
        this.mEndTime = i_EndTime;
    }

    public void setIsAvailable(boolean i_IsAvailable)
    {
        this.mIsAvailable = i_IsAvailable;
    }
}
