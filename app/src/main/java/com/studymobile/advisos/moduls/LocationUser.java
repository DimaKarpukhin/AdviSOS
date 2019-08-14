package com.studymobile.advisos.moduls;

import android.location.Location;

public class LocationUser {
    private Location location;
    private float langtitude;
    private float longtitude;

    public LocationUser(Location i_location, float i_lang, float i_long){
        this.location =i_location;
        this.langtitude = i_lang;
        this.longtitude = i_long;
    }

    public float getLangtitude() {
        return langtitude;
    }

    public float getLongtitude() {
        return longtitude;
    }

    public Location getLocation() {
        return location;
    }

    public void setLangtitude(float langtitude) {
        this.langtitude = langtitude;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }
    public float CalculateUsersDistance(LocationUser i_user1, LocationUser i_user2){
        //TODO
        return 0.0f;
    }
}

