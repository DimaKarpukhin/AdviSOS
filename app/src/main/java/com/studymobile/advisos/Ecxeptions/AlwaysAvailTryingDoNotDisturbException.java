package com.studymobile.advisos.Ecxeptions;

public class AlwaysAvailTryingDoNotDisturbException extends Exception {

    @Override
    public String getMessage() {
        return "The current setting of property is always available\n "+
                "Do you wish to override the configuration and not receive massages? ";
    }
}
