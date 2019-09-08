package com.studymobile.advisos.Models;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class User
{
    private String mFirstName, mFamilyName, mEmail, mPhone, mImgLink, mAuthContext;
    private UserLocation mLocation;
    private UserAvailability mAvailability;

    public User(){}

    public User(String i_FirstName, String i_FamilyName, String i_Email, String i_Phone)
    {
        this.setFirstName(i_FirstName);
        this.setFamilyName(i_FamilyName);
        this.setEmail(i_Email);
        this.setPhone(i_Phone);
    }

    public String getFirstName()
    {
        return mFirstName;
    }

    public String getFamilyName()
    {
        return mFamilyName;
    }

    public String getEmail()
    {
        return mEmail;
    }

    public String getPhone()
    {
        return mPhone;
    }

    public String getImgLink()
    {
        return mImgLink;
    }

    public String getAuthContext()
    {
        return mAuthContext;
    }

    public UserLocation getLocation()
    {
        return mLocation;
    }

    public UserAvailability getUserAvailability()
    {
        return mAvailability;
    }

    public void setFirstName(String i_FirstName)
    {
        this.mFirstName = i_FirstName;
    }

    public void setFamilyName(String i_FamilyName)
    {
        this.mFamilyName = i_FamilyName;
    }

    public void setEmail(String i_Email)
    {
        this.mEmail = i_Email;
    }

    public void setPhone(String i_Phone)
    {
        this.mPhone = i_Phone;
    }

    public void setImgLink(String i_ImgLink)
    {
        this.mImgLink = i_ImgLink;
    }

    public void setAuthContext(String i_AuthContext)
    {
        this.mAuthContext = i_AuthContext;
    }

    public void setUserLocation(UserLocation i_Location)
    {
        this.mLocation = i_Location;
    }

    public void setUserAvailability(UserAvailability i_Availability)
    {
        this.mAvailability = i_Availability;
    }

    public void UpdateLocation( double i_lang, double i_long) {
        if (mLocation == null) {
            mLocation = new UserLocation( i_lang, i_long);
        } else {
            updateUserLocation( i_lang, i_long);
        }
    }

    private void updateUserLocation( double i_lang, double i_long){
        //TODO
    }

}
