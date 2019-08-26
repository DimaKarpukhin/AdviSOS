package com.studymobile.advisos.Models;

import com.studymobile.advisos.Enums.eWeekDay;

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
        this.SetSunday(i_Sunday);
        this.SetMonday(i_Monday);
        this.SetTuesday(i_Tuesday);
        this.SetWednesday(i_Wednesday);
        this.SetThursday(i_Thursday);
        this.SetFriday(i_Friday);
        this.SetSaturday(i_Saturday);
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
        if(i_Sunday.GetWeekDay() != eWeekDay.Sunday)
        {
            throw new IllegalArgumentException(String.format("Sunday member cannot be assigned with \'%s\'", i_Sunday.GetWeekDay()));
        }
        this.mSunday = i_Sunday;
    }

    public void SetMonday(Day i_Monday)
    {
        if(i_Monday.GetWeekDay() != eWeekDay.Monday)
        {
            throw new IllegalArgumentException(String.format("Monday member cannot be assigned with \'%s\'", i_Monday.GetWeekDay()));
        }
        this.mMonday = i_Monday;
    }

    public void SetTuesday(Day i_Tuesday)
    {
        if(i_Tuesday.GetWeekDay() != eWeekDay.Tuesday)
        {
            throw new IllegalArgumentException(String.format("Tuesday member cannot be assigned with \'%s\'", i_Tuesday.GetWeekDay()));
        }
        this.mTuesday = i_Tuesday;
    }

    public void SetWednesday(Day i_Wednesday)
    {
        if(i_Wednesday.GetWeekDay() != eWeekDay.Wednesday)
        {
            throw new IllegalArgumentException(String.format("Wednesday member cannot be assigned with \'%s\'", i_Wednesday.GetWeekDay()));
        }
        this.mWednesday = i_Wednesday;
    }

    public void SetThursday(Day i_Thursday)
    {
        if(i_Thursday.GetWeekDay() != eWeekDay.Thursday)
        {
            throw new IllegalArgumentException(String.format("Thursday member cannot be assigned with \'%s\'", i_Thursday.GetWeekDay()));
        }
        this.mThursday = i_Thursday;
    }

    public void SetFriday(Day i_Friday)
    {
        if(i_Friday.GetWeekDay() != eWeekDay.Friday)
        {
            throw new IllegalArgumentException(String.format("Friday member cannot be assigned with \'%s\'", i_Friday.GetWeekDay()));
        }
        this.mFriday = i_Friday;
    }

    public void SetSaturday(Day i_Saturday)
    {
        if(i_Saturday.GetWeekDay() != eWeekDay.Saturday)
        {
            throw new IllegalArgumentException(String.format("Saturday member cannot be assigned with \'%s\'", i_Saturday.GetWeekDay()));
        }
        this.mSaturday = i_Saturday;
    }
}
