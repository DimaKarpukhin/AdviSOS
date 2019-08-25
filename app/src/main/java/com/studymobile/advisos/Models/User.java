package com.studymobile.advisos.Models;

import android.location.Location;

import com.studymobile.advisos.Services.UserAvailability;
import com.studymobile.advisos.Services.UserLocation;

public class User {

    private String firstName, lastName, email, phone, authContex;
    private boolean isExpert;
    private UserLocation location;
    private UserAvailability availabilty;
    private Rating rating;
    
    public void UpdateUsersLocation(Location i_location, float i_lang, float i_long){
        if (location == null){
            location = new UserLocation(i_location, i_lang,i_long);
        }
        else {
            updateUserLocation( i_location, i_lang,i_long);
        }
    }
    public void ConfigureUserAvalibiltyFrom(){
        //TODO
        //
    }
    public void ConfigureUserAvailibilityTo(){

    }
    public void ConfigureIsAlwayAvail(){

    }
    public void ConfigureIsDoNotDisurbe(){

    }
    public void ConfigureMaxCurrentConversations(){

    }
    public void ConfigureIsNumberOfConversationsUnLimited(){

    }
    private void updateUserLocation(Location i_location, float i_lang, float i_long){
        //TODO
    }

}
