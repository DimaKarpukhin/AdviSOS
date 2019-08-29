package com.studymobile.advisos.Models;

import com.studymobile.advisos.Enums.eWeekDay;

public class Day
{
    private eWeekDay mDay;
    private String mStartTime;
    private String mEndTime;
    private boolean mIsAvailable;

    public Day(){}
    public Day(eWeekDay i_Day, String i_StartTime, String i_EndTime, boolean i_IsAvailable)
    {
        this.setDay(i_Day);
        this.setTimeFrom(i_StartTime);
        this.setTimeTo(i_EndTime);
        this.setIsAvailable(i_IsAvailable);
    }

    public eWeekDay getDay()
    {
        return mDay;
    }

    public String getTimeFrom()
    {
        return mStartTime;
    }

    public String getTimeTo()
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

    public void setTimeFrom(String i_StartTime)
    {
        this.mStartTime = i_StartTime;
    }

    public void setTimeTo(String i_EndTime)
    {
        this.mEndTime = i_EndTime;
    }

    public void setIsAvailable(boolean i_IsAvailable)
    {
        this.mIsAvailable = i_IsAvailable;
    }
}
