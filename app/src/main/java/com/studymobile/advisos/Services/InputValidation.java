package com.studymobile.advisos.Services;

import android.util.Patterns;

import java.util.regex.Pattern;

public class InputValidation
{
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    //"(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +             //no white spaces
                    ".{6,}" +  "$");          //at least 6 characters

    public static boolean IsValidPassword(String i_Password)
    {
        return PASSWORD_PATTERN.matcher(i_Password).matches();
    }

    public static boolean IsValidEmail(String i_Email)
    {
       return Patterns.EMAIL_ADDRESS.matcher(i_Email).matches();
    }

    public static boolean IsValidPhoneNumber(String i_Phone)
    {
        return Patterns.PHONE.matcher(i_Phone).matches() && i_Phone.length() == 13;
    }

    public static boolean IsValidTimeSpan(String i_StartTime, String i_EndTime)
    {
        if(i_StartTime.isEmpty() && i_EndTime.isEmpty())
        {
            return false;
        }
        else if(!i_StartTime.isEmpty() && !i_EndTime.isEmpty())
        {
            int startHour = Integer.parseInt(i_StartTime.substring(0, i_StartTime.indexOf(":")));
            int endHour = Integer.parseInt(i_EndTime.substring(0, i_EndTime.indexOf(":")));
            int startMinute = Integer.parseInt(i_StartTime.substring(i_StartTime.indexOf(":") + 1));
            int endMinute = Integer.parseInt(i_EndTime.substring(i_EndTime.indexOf(":") + 1));

            if((startHour == endHour) && (startMinute == endMinute))
            {
                return false;
            }
            else if((startHour == endHour) && (startMinute > endMinute))
            {
                return false;
            }
            else if((startHour > endHour))
            {
                return false;
            }
        }

        return true;
    }


}