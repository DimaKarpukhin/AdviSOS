package com.studymobile.advisos.Models;

public class UserLocation {
    private double mLatitude;
    private double mLongitude;
    private double mAltitude;

    public UserLocation(){}

    public UserLocation( double i_lat, double i_long, double i_alt){
        mLatitude = i_lat;
        mLongitude = i_long;
        mAltitude = i_alt;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public double getAltitude() {
        return mAltitude;
    }

    public void setLatitude(double i_lat) {
        mLatitude = i_lat;
    }

    public void setLongitude(double i_long) {
        mLongitude = i_long;
    }

    public void setAltitude(double i_alt) {
        mAltitude = i_alt;
    }

    public double distanceBetween(UserLocation i_loc)
    {
        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(i_loc.mLatitude - mLatitude);
        double lonDistance = Math.toRadians(i_loc.mLongitude - mLongitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(mLatitude)) * Math.cos(Math.toRadians(i_loc.mLatitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        double height = mAltitude - i_loc.mAltitude;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        return Math.sqrt(distance);
    }
}