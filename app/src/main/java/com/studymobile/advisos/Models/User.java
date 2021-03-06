package com.studymobile.advisos.Models;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class User
{
    private String mUserID, mFirstName, mFamilyName, mEmail, mPhone, mImgLink, mAuthContext, mDeviceToken;
    private UserLocation mLocation;
    private boolean mIsOnline;
    private UserAvailability mAvailability;

    public User(){}

    public User(String i_FirstName, String i_FamilyName, String i_Email, String i_Phone)
    {
        this.setFirstName(i_FirstName);
        this.setFamilyName(i_FamilyName);
        this.setEmail(i_Email);
        this.setPhone(i_Phone);
    }

    public String getUserId()
    {
        return mUserID;
    }

    public boolean getIsOnline(){return mIsOnline;}

    public String getDeviceToken()
    {
        return mDeviceToken;
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

    public void setUserId(String i_UserId)
    {
        mUserID = i_UserId;
    }

    public void setIsOnline(boolean i_IsOnline){ this.mIsOnline = i_IsOnline;}

    public void setDeviceToken(String i_DeviceToken)
    {
        this.mDeviceToken = i_DeviceToken;
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

    public void UpdateLocation( double i_lang, double i_long, double i_altitude) {
        if (mLocation == null) {
            mLocation = new UserLocation( i_lang, i_long, i_altitude);
        } else {
            updateUserLocation( i_lang, i_long);
        }
    }

    private void updateUserLocation( double i_lang, double i_long){
        //TODO
    }

}
