package com.studymobile.advisos.Models;

import android.location.Location;

import com.studymobile.advisos.Services.UserAvailability;
import com.studymobile.advisos.Services.UserLocation;

public class User
{
    private String mFirstName, mLastName, mEmail, mPhone, mImgLink, mAuthContext;
    private UserLocation mLocation;
    private UserAvailability mAvailability;

    public User(){}

    public User(String i_FirstName, String i_LastName, String i_Email, String i_Phone)
    {
        this.mFirstName = i_FirstName;
        this.mLastName = i_LastName;
        this.mEmail = i_Email;
        this.mPhone = i_Phone;
    }

    public String GetFirstName()
    {
        return mFirstName;
    }

    public String GetLastName()
    {
        return mLastName;
    }

    public String GetEmail()
    {
        return mEmail;
    }

    public String GetPhone()
    {
        return mPhone;
    }

    public String Geti_ImgLink()
    {
        return mImgLink;
    }

    public String GetAuthContext()
    {
        return mAuthContext;
    }

    public UserLocation GetLocation()
    {
        return mLocation;
    }

    public UserAvailability GetAvailabilty()
    {
        return mAvailability;
    }

    public void SetFirstName(String i_FirstName)
    {
        this.mFirstName = i_FirstName;
    }

    public void SetLastName(String i_LastName)
    {
        this.mLastName = i_LastName;
    }

    public void SetEmail(String i_Email)
    {
        this.mEmail = i_Email;
    }

    public void SetPhone(String i_Phone)
    {
        this.mPhone = i_Phone;
    }

    public void SetImgLink(String i_ImgLink)
    {
        this.mImgLink = i_ImgLink;
    }

    public void SetAuthContext(String i_AuthContext)
    {
        this.mAuthContext = i_AuthContext;
    }

    public void SetLocation(UserLocation i_Location)
    {
        this.mLocation = i_Location;
    }

    public void SetAvailabilty(UserAvailability i_Availability)
    {
        this.mAvailability = i_Availability;
    }

    public void UpdateUsersLocation(Location i_location, float i_lang, float i_long) {
        if (mLocation == null) {
            mLocation = new UserLocation(i_location, i_lang, i_long);
        } else {
            updateUserLocation(i_location, i_lang, i_long);
        }
    }

    private void updateUserLocation(Location i_location, float i_lang, float i_long){
        //TODO
    }

}
