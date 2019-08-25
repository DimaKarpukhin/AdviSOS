package com.studymobile.advisos.Activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;
import com.studymobile.advisos.R;
import com.studymobile.advisos.Services.InputValidation;

import java.util.Calendar;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityExpertSettings extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener
{
    private Button m_BtnTopics;
    private Switch m_SwitchAlwaysAvailable,m_SwitchNeverAvailable;
    private boolean m_IsSundayEnable = true;
    private boolean m_IsMondayEnable = true;
    private boolean m_IsTuesdayEnable = true;
    private boolean m_IsWednesdayEnable = true;
    private boolean m_IsThursdayEnable = true;
    private boolean m_IsFridayEnable = true;
    private boolean m_IsSaturdayEnable = true;
    private boolean m_IsDisableAllDays = true;
    private boolean m_IsAvailable = true;

    private Button m_SwitchSunday;
    private Button m_SwitchMonday;
    private Button m_SwitchTuesday;
    private Button m_SwitchWednesday;
    private Button m_SwitchThursday;
    private Button m_SwitchFriday;
    private Button m_SwitchSaturday;

    private Button m_BtnStartTimeSunday;
    private Button m_BtnStartTimeMonday;
    private Button m_BtnStartTimeTuesday;
    private Button m_BtnStartTimeWednesday;
    private Button m_BtnStartTimeThursday;
    private Button m_BtnStartTimeFriday;
    private Button m_BtnStartTimeSaturday;
    private Button m_BtnEndTimeSunday;
    private Button m_BtnEndTimeMonday;
    private Button m_BtnEndTimeTuesday;
    private Button m_BtnEndTimeWednesday;
    private Button m_BtnEndTimeThursday;
    private Button m_BtnEndTimeFriday;
    private Button m_BtnEndTimeSaturday;

    private TextView m_TxtStartTimeSunday;
    private TextView m_TxtStartTimeMonday;
    private TextView m_TxtStartTimeTuesday;
    private TextView m_TxtStartTimeWednesday;
    private TextView m_TxtStartTimeThursday;
    private TextView m_TxtStartTimeFriday;
    private TextView m_TxtStartTimeSaturday;
    private TextView m_TxtEndTimeSunday;
    private TextView m_TxtEndTimeMonday;
    private TextView m_TxtEndTimeTuesday;
    private TextView m_TxtEndWednesday;
    private TextView m_TxtEndThursday;
    private TextView m_TxtEndFriday;
    private TextView m_TxtEndSaturday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_settings);
        setActivityContent();
        //setFirebaseData();
    }

    @Override
    public void onCheckedChanged(CompoundButton i_ButtonView, boolean i_IsChecked)
    {
        int id = i_ButtonView.getId();

        if(m_SwitchAlwaysAvailable.getId() == id)
        {
            if(i_IsChecked) {
                m_IsAvailable = true;
                m_SwitchNeverAvailable.setChecked(false);
                switchOnAllDays();
            }else if (m_IsDisableAllDays) {
                switchOffAllDays();
            }
        }
        else if(m_SwitchNeverAvailable.getId() == id)
        {
            if (i_IsChecked) {
                m_IsAvailable = false;
                m_SwitchAlwaysAvailable.setChecked(false);
                switchOffAllDays();
            }else {
                //switchOnAllDays();
            }
        }
    }

    @Override
    public void onClick(View i_View)
    {
        int id = i_View.getId();

        onClickWeekDay(id);
        onClickStartTime(id);
        onClickEndTime(id);

    }

    private void switchOnAllDays()
    {
        switchOn(m_SwitchSunday, m_BtnStartTimeSunday, m_BtnEndTimeSunday);
        m_IsSundayEnable = false;
        switchOn(m_SwitchMonday, m_BtnStartTimeMonday, m_BtnEndTimeMonday);
        m_IsMondayEnable = false;
        switchOn(m_SwitchTuesday, m_BtnStartTimeTuesday, m_BtnEndTimeTuesday);
        m_IsTuesdayEnable = false;
        switchOn(m_SwitchWednesday, m_BtnStartTimeWednesday, m_BtnEndTimeWednesday);
        m_IsWednesdayEnable = false;
        switchOn(m_SwitchThursday, m_BtnStartTimeThursday, m_BtnEndTimeThursday);
        m_IsThursdayEnable = false;
        switchOn(m_SwitchFriday, m_BtnStartTimeFriday, m_BtnEndTimeFriday);
        m_IsFridayEnable = false;
        switchOn(m_SwitchSaturday, m_BtnStartTimeSaturday, m_BtnEndTimeSaturday);
        m_IsSaturdayEnable = false;
    }

    private void switchOffAllDays()
    {
        switchOff(m_SwitchSunday, m_BtnStartTimeSunday, m_BtnEndTimeSunday, m_TxtStartTimeSunday, m_TxtEndTimeSunday);
        m_IsSundayEnable = true;
        switchOff(m_SwitchMonday, m_BtnStartTimeMonday, m_BtnEndTimeMonday, m_TxtStartTimeMonday, m_TxtEndTimeMonday);
        m_IsMondayEnable = true;
        switchOff(m_SwitchTuesday, m_BtnStartTimeTuesday, m_BtnEndTimeTuesday, m_TxtStartTimeTuesday, m_TxtEndTimeTuesday);
        m_IsTuesdayEnable = true;
        switchOff(m_SwitchWednesday, m_BtnStartTimeWednesday, m_BtnEndTimeWednesday, m_TxtStartTimeWednesday, m_TxtEndWednesday);
        m_IsWednesdayEnable = true;
        switchOff(m_SwitchThursday, m_BtnStartTimeThursday, m_BtnEndTimeThursday, m_TxtStartTimeThursday, m_TxtEndThursday);
        m_IsThursdayEnable = true;
        switchOff(m_SwitchFriday, m_BtnStartTimeFriday, m_BtnEndTimeFriday, m_TxtStartTimeFriday, m_TxtEndFriday);
        m_IsFridayEnable = true;
        switchOff(m_SwitchSaturday, m_BtnStartTimeSaturday, m_BtnEndTimeSaturday, m_TxtStartTimeSaturday, m_TxtEndSaturday);
    }

    private void switchOn(Button i_WeekDay, Button i_TimeFrom, Button i_TimeTo)
    {
        if(m_SwitchNeverAvailable.isChecked())
        {
            m_SwitchNeverAvailable.setChecked(false);
        }
        i_WeekDay.setBackground(getDrawable(R.drawable.design_btn_round_blue));
        i_WeekDay.setTextColor(getColor(R.color.white));

        i_TimeFrom.setBackground(getDrawable(R.drawable.design_btn_round_green_light));
        i_TimeFrom.setText("+");
        i_TimeFrom.setEnabled(true);

        i_TimeTo.setBackground(getDrawable(R.drawable.design_btn_round_green_light));
        i_TimeTo.setText("+");
        i_TimeTo.setEnabled(true);
    }

    private void switchOff(Button i_WeekDay, Button i_BtnFrom, Button i_BtnTo, TextView i_TxtFrom, TextView i_TxtTo)
    {
        if(m_SwitchAlwaysAvailable.isChecked() && m_IsAvailable)
        {
            m_IsDisableAllDays = false;
            m_SwitchAlwaysAvailable.setChecked(false);
        }
        i_TxtFrom.setVisibility(View.INVISIBLE);
        i_TxtTo.setVisibility(View.INVISIBLE);

        i_WeekDay.setBackground(getDrawable(R.drawable.design_btn_round_white_blue));
        i_WeekDay.setTextColor(getColor(R.color.black));

        i_BtnFrom.setBackground(getDrawable(R.drawable.design_btn_round_white_black));
        i_BtnFrom.setText("-");
        i_BtnFrom.setVisibility(View.VISIBLE);
        i_BtnFrom.setEnabled(false);

        i_BtnTo.setBackground(getDrawable(R.drawable.design_btn_round_white_black));
        i_BtnTo.setText("-");
        i_BtnTo.setVisibility(View.VISIBLE);
        i_BtnTo.setEnabled(false);
    }

    private void onClickWeekDay(int i_ViewId)
    {
        if(i_ViewId == m_SwitchSunday.getId()) {
            if(m_IsSundayEnable) {
                switchOn(m_SwitchSunday, m_BtnStartTimeSunday, m_BtnEndTimeSunday);
            }else{
                switchOff(m_SwitchSunday, m_BtnStartTimeSunday, m_BtnEndTimeSunday, m_TxtStartTimeSunday, m_TxtEndTimeSunday);
            }
            m_IsSundayEnable = !m_IsSundayEnable;

        }else if(i_ViewId == m_SwitchMonday.getId()) {
            if(m_IsMondayEnable) {
                switchOn(m_SwitchMonday, m_BtnStartTimeMonday, m_BtnEndTimeMonday);
            }else{
                switchOff(m_SwitchMonday, m_BtnStartTimeMonday, m_BtnEndTimeMonday, m_TxtStartTimeMonday, m_TxtEndTimeMonday);
            }
            m_IsMondayEnable = !m_IsMondayEnable;

        }else if(i_ViewId == m_SwitchTuesday.getId()) {
            if(m_IsTuesdayEnable) {
                switchOn(m_SwitchTuesday, m_BtnStartTimeTuesday, m_BtnEndTimeTuesday);
            }else{
                switchOff(m_SwitchTuesday, m_BtnStartTimeTuesday, m_BtnEndTimeTuesday, m_TxtStartTimeTuesday, m_TxtEndTimeTuesday);
            }
            m_IsTuesdayEnable = !m_IsTuesdayEnable;

        }else if(i_ViewId == m_SwitchWednesday.getId()) {
            if(m_IsWednesdayEnable) {
                switchOn(m_SwitchWednesday, m_BtnStartTimeWednesday, m_BtnEndTimeWednesday);
            }else{
                switchOff(m_SwitchWednesday, m_BtnStartTimeWednesday, m_BtnEndTimeWednesday, m_TxtStartTimeWednesday, m_TxtEndWednesday);
            }
            m_IsWednesdayEnable = !m_IsWednesdayEnable;

        }else if(i_ViewId == m_SwitchThursday.getId()) {
            if(m_IsThursdayEnable) {
                switchOn(m_SwitchThursday, m_BtnStartTimeThursday, m_BtnEndTimeThursday);
            }else{
                switchOff(m_SwitchThursday, m_BtnStartTimeThursday, m_BtnEndTimeThursday, m_TxtStartTimeThursday, m_TxtEndThursday);
            }
            m_IsThursdayEnable = !m_IsThursdayEnable;

        }else if(i_ViewId == m_SwitchFriday.getId()) {
            if(m_IsFridayEnable) {
                switchOn(m_SwitchFriday, m_BtnStartTimeFriday, m_BtnEndTimeFriday);
            }else{
                switchOff(m_SwitchFriday, m_BtnStartTimeFriday, m_BtnEndTimeFriday, m_TxtStartTimeFriday, m_TxtEndFriday);
            }
            m_IsFridayEnable = !m_IsFridayEnable;

        }else if(i_ViewId == m_SwitchSaturday.getId()) {
            if(m_IsSaturdayEnable) {
                switchOn(m_SwitchSaturday, m_BtnStartTimeSaturday, m_BtnEndTimeSaturday);
            }else{
                switchOff(m_SwitchSaturday, m_BtnStartTimeSaturday, m_BtnEndTimeSaturday, m_TxtStartTimeSaturday, m_TxtEndSaturday);
            }
            m_IsSaturdayEnable = !m_IsSaturdayEnable;
        }
    }

    private void onClickStartTime(int i_ViewId) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePicker;

        if (i_ViewId == m_BtnStartTimeSunday.getId() || i_ViewId == m_TxtStartTimeSunday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setSundayTime(i_SelectedHour,i_SelectedMinute, true);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnStartTimeMonday.getId() || i_ViewId == m_TxtStartTimeMonday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setMondayTime(i_SelectedHour,i_SelectedMinute, true);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnStartTimeTuesday.getId() || i_ViewId == m_TxtStartTimeTuesday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setTuesdayTime(i_SelectedHour,i_SelectedMinute, true);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnStartTimeWednesday.getId() || i_ViewId == m_TxtStartTimeWednesday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setWednesdayTime(i_SelectedHour,i_SelectedMinute, true);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnStartTimeThursday.getId() || i_ViewId == m_TxtStartTimeThursday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setThursdayTime(i_SelectedHour,i_SelectedMinute, true);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnStartTimeFriday.getId() || i_ViewId == m_TxtStartTimeFriday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setFridayTime(i_SelectedHour,i_SelectedMinute, true);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnStartTimeSaturday.getId() || i_ViewId == m_TxtStartTimeSaturday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setSaturdayTime(i_SelectedHour,i_SelectedMinute, true);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        }
    }

    private void onClickEndTime(int i_ViewId) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePicker;

        if (i_ViewId == m_BtnEndTimeSunday.getId() || i_ViewId == m_TxtEndTimeSunday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setSundayTime(i_SelectedHour,i_SelectedMinute, false);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnEndTimeMonday.getId() || i_ViewId == m_TxtEndTimeMonday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setMondayTime(i_SelectedHour,i_SelectedMinute, false);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnEndTimeTuesday.getId() || i_ViewId == m_TxtEndTimeTuesday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setTuesdayTime(i_SelectedHour,i_SelectedMinute, false);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnEndTimeWednesday.getId() || i_ViewId == m_TxtEndWednesday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setWednesdayTime(i_SelectedHour,i_SelectedMinute, false);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnEndTimeThursday.getId() || i_ViewId == m_TxtEndThursday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setThursdayTime(i_SelectedHour,i_SelectedMinute, false);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnEndTimeFriday.getId() || i_ViewId == m_TxtEndFriday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setFridayTime(i_SelectedHour,i_SelectedMinute, false);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnEndTimeSaturday.getId() || i_ViewId == m_TxtEndSaturday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setSaturdayTime(i_SelectedHour,i_SelectedMinute, false);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        }
    }

    public void setSundayTime(int I_Hour, int i_Minute, boolean i_IsStartTime) 
    {
        String errorMessage = "Invalid time span";
        String fixer = (i_Minute < 10) ? ":0" : ":";
        String time = I_Hour + fixer + i_Minute;
        
        if(i_IsStartTime) {

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndTimeSunday.getText().toString())) {
                m_BtnStartTimeSunday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeSunday.setText(I_Hour + fixer + i_Minute);
                m_TxtStartTimeSunday.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
        else {
            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeSunday.getText().toString(), time)) {
                m_BtnEndTimeSunday.setVisibility(View.INVISIBLE);
                m_TxtEndTimeSunday.setText(I_Hour + fixer + i_Minute);
                m_TxtEndTimeSunday.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void setMondayTime(int I_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String fixer = (i_Minute < 10) ? ":0" : ":";
        String time = I_Hour + fixer + i_Minute;

        if(i_IsStartTime) {

            if (InputValidation.IsValidTimeSpan(time, m_TxtStartTimeMonday.getText().toString())) {
                m_BtnStartTimeMonday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeMonday.setText(I_Hour + fixer + i_Minute);
                m_TxtStartTimeMonday.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
        else {
            if (InputValidation.IsValidTimeSpan(m_TxtEndTimeMonday.getText().toString(), time)) {
                m_BtnEndTimeMonday.setVisibility(View.INVISIBLE);
                m_TxtEndTimeMonday.setText(I_Hour + fixer + i_Minute);
                m_TxtEndTimeMonday.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void setTuesdayTime(int I_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String fixer = (i_Minute < 10) ? ":0" : ":";
        String time = I_Hour + fixer + i_Minute;

        if(i_IsStartTime) {

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndTimeTuesday.getText().toString())) {
                m_BtnStartTimeTuesday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeTuesday.setText(I_Hour + fixer + i_Minute);
                m_TxtStartTimeTuesday.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
        else {
            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeTuesday.getText().toString(), time)) {
                m_BtnEndTimeTuesday.setVisibility(View.INVISIBLE);
                m_TxtEndTimeTuesday.setText(I_Hour + fixer + i_Minute);
                m_TxtEndTimeTuesday.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void setWednesdayTime(int I_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String fixer = (i_Minute < 10) ? ":0" : ":";
        String time = I_Hour + fixer + i_Minute;

        if(i_IsStartTime) {

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndWednesday.getText().toString())) {
                m_BtnStartTimeWednesday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeWednesday.setText(I_Hour + fixer + i_Minute);
                m_TxtStartTimeWednesday.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
        else {
            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeWednesday.getText().toString(), time)) {
                m_BtnEndTimeWednesday.setVisibility(View.INVISIBLE);
                m_TxtEndWednesday.setText(I_Hour + fixer + i_Minute);
                m_TxtEndWednesday.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void setThursdayTime(int I_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String fixer = (i_Minute < 10) ? ":0" : ":";
        String time = I_Hour + fixer + i_Minute;

        if(i_IsStartTime) {

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndThursday.getText().toString())) {
                m_BtnStartTimeThursday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeThursday.setText(I_Hour + fixer + i_Minute);
                m_TxtStartTimeThursday.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
        else {
            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeThursday.getText().toString(), time)) {
                m_BtnEndTimeThursday.setVisibility(View.INVISIBLE);
                m_TxtEndThursday.setText(I_Hour + fixer + i_Minute);
                m_TxtEndThursday.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void setFridayTime(int I_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String fixer = (i_Minute < 10) ? ":0" : ":";
        String time = I_Hour + fixer + i_Minute;

        if(i_IsStartTime) {

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndFriday.getText().toString())) {
                m_BtnStartTimeFriday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeFriday.setText(I_Hour + fixer + i_Minute);
                m_TxtStartTimeFriday.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
        else {
            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeFriday.getText().toString(), time)) {
                m_BtnEndTimeFriday.setVisibility(View.INVISIBLE);
                m_TxtEndFriday.setText(I_Hour + fixer + i_Minute);
                m_TxtEndFriday.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void setSaturdayTime(int I_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String fixer = (i_Minute < 10) ? ":0" : ":";
        String time = I_Hour + fixer + i_Minute;

        if(i_IsStartTime) {

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndSaturday.getText().toString())) {
                m_BtnStartTimeSaturday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeSaturday.setText(I_Hour + fixer + i_Minute);
                m_TxtStartTimeSaturday.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
        else {
            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeSaturday.getText().toString(), time)) {
                m_BtnEndTimeSaturday.setVisibility(View.INVISIBLE);
                m_TxtEndSaturday.setText(I_Hour + fixer + i_Minute);
                m_TxtEndSaturday.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void setActivityContent()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        m_BtnTopics = findViewById(R.id.btn_topics_expert_config);
        m_BtnTopics.setOnClickListener(this);
        m_SwitchAlwaysAvailable = findViewById(R.id.switch_always_available);
        m_SwitchAlwaysAvailable.setOnCheckedChangeListener(this);
        m_SwitchNeverAvailable = findViewById(R.id.switch_never_available);
        m_SwitchNeverAvailable.setOnCheckedChangeListener(this);

        setSwitchesOfWeekDays();
        setTimePickButtons();
        setTimePickTexts();
    }

    private void setSwitchesOfWeekDays()
    {
        m_SwitchSunday = findViewById(R.id.switch_sunday);
        m_SwitchSunday.setOnClickListener(this);
        m_SwitchMonday = findViewById(R.id.switch_monday);
        m_SwitchMonday.setOnClickListener(this);
        m_SwitchTuesday = findViewById(R.id.switch_tuesday);
        m_SwitchTuesday.setOnClickListener(this);
        m_SwitchWednesday = findViewById(R.id.switch_wednesday);
        m_SwitchWednesday.setOnClickListener(this);
        m_SwitchThursday = findViewById(R.id.switch_thursday);
        m_SwitchThursday.setOnClickListener(this);
        m_SwitchFriday = findViewById(R.id.switch_friday);
        m_SwitchFriday.setOnClickListener(this);
        m_SwitchSaturday = findViewById(R.id.switch_saturday);
        m_SwitchSaturday.setOnClickListener(this);
    }

    private void setTimePickButtons()
    {
        m_BtnStartTimeSunday = findViewById(R.id.btn_start_time_sunday);
        m_BtnStartTimeSunday.setOnClickListener(this);
        m_BtnStartTimeMonday = findViewById(R.id.btn_start_time_monday);
        m_BtnStartTimeMonday.setOnClickListener(this);
        m_BtnStartTimeTuesday = findViewById(R.id.btn_start_time_tuesday);
        m_BtnStartTimeTuesday.setOnClickListener(this);
        m_BtnStartTimeWednesday = findViewById(R.id.btn_start_time_wednesday);
        m_BtnStartTimeWednesday.setOnClickListener(this);
        m_BtnStartTimeThursday = findViewById(R.id.btn_start_time_thursday);
        m_BtnStartTimeThursday.setOnClickListener(this);
        m_BtnStartTimeFriday = findViewById(R.id.btn_start_time_friday);
        m_BtnStartTimeFriday.setOnClickListener(this);
        m_BtnStartTimeSaturday = findViewById(R.id.btn_start_time_saturday);
        m_BtnStartTimeSaturday.setOnClickListener(this);

        m_BtnEndTimeSunday = findViewById(R.id.btn_end_time_sunday);
        m_BtnEndTimeSunday.setOnClickListener(this);
        m_BtnEndTimeMonday = findViewById(R.id.btn_end_time_monday);
        m_BtnEndTimeMonday.setOnClickListener(this);
        m_BtnEndTimeTuesday = findViewById(R.id.btn_end_time_tuesday);
        m_BtnEndTimeTuesday.setOnClickListener(this);
        m_BtnEndTimeWednesday = findViewById(R.id.btn_end_time_wednesday);
        m_BtnEndTimeWednesday.setOnClickListener(this);
        m_BtnEndTimeThursday = findViewById(R.id.btn_end_time_thursday);
        m_BtnEndTimeThursday.setOnClickListener(this);
        m_BtnEndTimeFriday = findViewById(R.id.btn_end_time_friday);
        m_BtnEndTimeFriday.setOnClickListener(this);
        m_BtnEndTimeSaturday = findViewById(R.id.btn_end_time_saturday);
        m_BtnEndTimeSaturday.setOnClickListener(this);
    }

    private void setTimePickTexts()
    {
        m_TxtStartTimeSunday = findViewById(R.id.txt_start_time_sunday);
        m_TxtStartTimeSunday.setOnClickListener(this);
        m_TxtStartTimeMonday = findViewById(R.id.txt_start_time_monday);
        m_TxtStartTimeMonday.setOnClickListener(this);
        m_TxtStartTimeTuesday = findViewById(R.id.txt_start_time_tuesday);
        m_TxtStartTimeTuesday.setOnClickListener(this);
        m_TxtStartTimeWednesday = findViewById(R.id.txt_start_time_wednesday);
        m_TxtStartTimeWednesday.setOnClickListener(this);
        m_TxtStartTimeThursday = findViewById(R.id.txt_start_time_thursday);
        m_TxtStartTimeThursday.setOnClickListener(this);
        m_TxtStartTimeFriday = findViewById(R.id.txt_start_time_friday);
        m_TxtStartTimeFriday.setOnClickListener(this);
        m_TxtStartTimeSaturday = findViewById(R.id.txt_start_time_saturday);
        m_TxtStartTimeSaturday.setOnClickListener(this);

        m_TxtEndTimeSunday = findViewById(R.id.txt_end_time_sunday);
        m_TxtEndTimeSunday.setOnClickListener(this);
        m_TxtEndTimeMonday = findViewById(R.id.txt_end_time_monday);
        m_TxtEndTimeMonday.setOnClickListener(this);
        m_TxtEndTimeTuesday = findViewById(R.id.txt_end_time_tuesday);
        m_TxtEndTimeTuesday.setOnClickListener(this);
        m_TxtEndWednesday = findViewById(R.id.txt_end_time_wednesday);
        m_TxtEndWednesday.setOnClickListener(this);
        m_TxtEndThursday = findViewById(R.id.txt_end_time_thursday);
        m_TxtEndThursday.setOnClickListener(this);
        m_TxtEndFriday = findViewById(R.id.txt_end_time_friday);
        m_TxtEndFriday.setOnClickListener(this);
        m_TxtEndSaturday = findViewById(R.id.txt_end_time_saturday);
        m_TxtEndSaturday.setOnClickListener(this);
    }
}
