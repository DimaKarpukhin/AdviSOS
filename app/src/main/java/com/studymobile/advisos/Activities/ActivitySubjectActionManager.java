package com.studymobile.advisos.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.studymobile.advisos.R;

public class ActivitySubjectActionManager extends AppCompatActivity
{

    private static final String SUBJECT_NAME = "subject_name";

    @Override
    protected void onCreate(Bundle i_SavedInstanceState)
    {
        super.onCreate(i_SavedInstanceState);
        setContentView(R.layout.activity_subject_action_manager);
    }
}
