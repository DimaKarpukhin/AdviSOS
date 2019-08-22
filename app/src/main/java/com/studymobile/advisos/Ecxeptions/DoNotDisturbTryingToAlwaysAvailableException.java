package com.studymobile.advisos.Ecxeptions;

public class DoNotDisturbTryingToAlwaysAvailableException extends Exception {

    @Override
    public String getMessage() {
        return " The current setting property to do not disturb is enabled.\n" +
                " Do you wish to override it and be always available?";
    }
}
