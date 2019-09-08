package com.studymobile.advisos.Models;

import android.location.Location;

public class UserLocation {
    private double langtitude;
    private double longtitude;

    public UserLocation(){}

    public UserLocation( double i_lang, double i_long){
        this.langtitude = i_lang;
        this.longtitude = i_long;
    }

    public double getLangtitude() {
        return langtitude;
    }

    public double getLongtitude() {
        return longtitude;
    }



    public void setLangtitude(double langtitude) {
        this.langtitude = langtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
    public double CalculateUsersDistance(UserLocation i_user1, UserLocation i_user2){
        //TODO
        return 0.0f;
    }
}

