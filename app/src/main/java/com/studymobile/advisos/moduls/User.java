package com.studymobile.advisos.moduls;

import android.location.Location;

public class User {

    private  String name;
    private String last_name;
    private String phone_number;
    LocationUser location;

    public User(String i_name, String i_last, String i_phone){
        this.name = i_name;
        this.last_name = i_last;
        this.phone_number = i_phone;
    }
    public void UpdateUsersLocation(Location i_location, float i_lang, float i_long){
        if (location == null){
            location = new LocationUser(i_location, i_lang,i_long);
        }
        else {
            updateUserLocation( i_location, i_lang,i_long);
        }
    }
    private void updateUserLocation(Location i_location, float i_lang, float i_long){
        //TODO
    }

}
