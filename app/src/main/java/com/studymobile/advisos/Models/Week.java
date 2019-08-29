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
        this.setSunday(i_Sunday);
        this.setMonday(i_Monday);
        this.setTuesday(i_Tuesday);
        this.setWednesday(i_Wednesday);
        this.setThursday(i_Thursday);
        this.setFriday(i_Friday);
        this.setSaturday(i_Saturday);
    }

    public Day getSunday()
    {
        return mSunday;
    }

    public Day getMonday()
    {
        return mMonday;
    }

    public Day getTuesday()
    {
        return mTuesday;
    }

    public Day getWednesday()
    {
        return mWednesday;
    }

    public Day getThursday()
    {
        return mThursday;
    }

    public Day getFriday()
    {
        return mFriday;
    }

    public Day getSaturday()
    {
        return mSaturday;
    }

    public void setSunday(Day i_Sunday)
    {
        if(i_Sunday.getDay() != eWeekDay.Sunday)
        {
            throw new IllegalArgumentException(
                    String.format("Sunday member cannot be assigned with \'%s\'",
                            i_Sunday.getDay()));
        }
        this.mSunday = i_Sunday;
    }

    public void setMonday(Day i_Monday)
    {
        if(i_Monday.getDay() != eWeekDay.Monday)
        {
            throw new IllegalArgumentException
                    (String.format("Monday member cannot be assigned with \'%s\'",
                            i_Monday.getDay()));
        }
        this.mMonday = i_Monday;
    }

    public void setTuesday(Day i_Tuesday)
    {
        if(i_Tuesday.getDay() != eWeekDay.Tuesday)
        {
            throw new IllegalArgumentException(
                    String.format("Tuesday member cannot be assigned with \'%s\'",
                            i_Tuesday.getDay()));
        }
        this.mTuesday = i_Tuesday;
    }

    public void setWednesday(Day i_Wednesday)
    {
        if(i_Wednesday.getDay() != eWeekDay.Wednesday)
        {
            throw new IllegalArgumentException(
                    String.format("Wednesday member cannot be assigned with \'%s\'",
                            i_Wednesday.getDay()));
        }
        this.mWednesday = i_Wednesday;
    }

    public void setThursday(Day i_Thursday)
    {
        if(i_Thursday.getDay() != eWeekDay.Thursday)
        {
            throw new IllegalArgumentException(
                    String.format("Thursday member cannot be assigned with \'%s\'",
                            i_Thursday.getDay()));
        }
        this.mThursday = i_Thursday;
    }

    public void setFriday(Day i_Friday)
    {
        if(i_Friday.getDay() != eWeekDay.Friday)
        {
            throw new IllegalArgumentException(
                    String.format("Friday member cannot be assigned with \'%s\'",
                            i_Friday.getDay()));
        }
        this.mFriday = i_Friday;
    }

    public void setSaturday(Day i_Saturday)
    {
        if(i_Saturday.getDay() != eWeekDay.Saturday)
        {
            throw new IllegalArgumentException(
                    String.format("Saturday member cannot be assigned with \'%s\'",
                            i_Saturday.getDay()));
        }
        this.mSaturday = i_Saturday;
    }
}
