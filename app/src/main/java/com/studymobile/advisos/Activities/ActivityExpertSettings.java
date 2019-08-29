package com.studymobile.advisos.Activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.studymobile.advisos.Enums.eWeekDay;
import com.studymobile.advisos.Models.Day;
import com.studymobile.advisos.Models.UserAvailability;
import com.studymobile.advisos.Models.Week;
import com.studymobile.advisos.R;
import com.studymobile.advisos.Services.InputValidation;

import java.util.Calendar;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityExpertSettings extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener
{
    private Button m_BtnSubjects;
    private ImageButton m_BtnNext;

    private Switch m_SwitchAlwaysAvailable,m_SwitchNotDisturb;

    private short m_AvailableDays = 0;
    private boolean m_IsNoNumChatsLimit = true;
    private boolean m_IsDisableAllDays = true;
    private boolean m_IsNotDisturb = false;
    private boolean m_IsAlwaysAvailable = false;
    private boolean m_IsSundayAvailable = false;
    private boolean m_IsMondayAvailable = false;
    private boolean m_IsTuesdayAvailable = false;
    private boolean m_IsWednesdayAvailable = false;
    private boolean m_IsThursdayAvailable = false;
    private boolean m_IsFridayAvailable = false;
    private boolean m_IsSaturdayAvailable = false;

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
    private TextView m_TxtEndTimeWednesday;
    private TextView m_TxtEndTimeThursday;
    private TextView m_TxtEndTimeFriday;
    private TextView m_TxtEndTimeSaturday;

    private FirebaseAuth m_Auth;
    private FirebaseUser m_CurrentUser;
    private FirebaseDatabase m_Database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_settings);
        setActivityContent();
        setFirebaseData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View i_View)
    {
        int id = i_View.getId();

        if(id == m_BtnNext.getId()) {
            populateDatabase();
            startHomeActivity();
        }else if(id == m_SwitchSunday.getId()) {
            setSundayClick();
        }else if(id == m_SwitchMonday.getId()) {
            setMondayClick();
        }else if(id == m_SwitchTuesday.getId()) {
            setTuesdayClick();
        }else if(id == m_SwitchWednesday.getId()) {
            setWednesdayClick();
        }else if(id == m_SwitchThursday.getId()) {
            setThursdayClick();
        }else if(id == m_SwitchFriday.getId()) {
            setFridayClick();
        }else if(id == m_SwitchSaturday.getId()) {
            setSaturdayClick();
        }

        setStartTimeClicks(id);
        setEndTimeClicks(id);
    }

    private void populateDatabase()
    {
        Day sunday, monday, tuesday, wednesday, thursday, friday, saturday;
        sunday = monday = tuesday = thursday
                = wednesday = friday = saturday = null;

        if(m_IsSundayAvailable) {
            sunday = new Day();
            setDayAvailability(sunday, eWeekDay.Sunday,
                    m_TxtStartTimeSunday.getText().toString(),
                    m_TxtEndTimeSunday.getText().toString());
        }
        if(m_IsMondayAvailable) {
            monday = new Day();
            setDayAvailability(monday, eWeekDay.Monday,
                    m_TxtStartTimeMonday.getText().toString(),
                    m_TxtEndTimeMonday.getText().toString());
        }
        if(m_IsTuesdayAvailable) {
            tuesday = new Day();
            setDayAvailability(tuesday, eWeekDay.Tuesday,
                    m_TxtStartTimeTuesday.getText().toString(),
                    m_TxtEndTimeTuesday.getText().toString());
        }
        if(m_IsWednesdayAvailable) {
            wednesday = new Day();
            setDayAvailability(wednesday, eWeekDay.Wednesday,
                    m_TxtStartTimeWednesday.getText().toString(),
                    m_TxtEndTimeWednesday.getText().toString());
        }
        if(m_IsThursdayAvailable) {
            thursday = new Day();
            setDayAvailability(thursday, eWeekDay.Thursday,
                    m_TxtStartTimeThursday.getText().toString(),
                    m_TxtEndTimeThursday.getText().toString());
        }
        if(m_IsFridayAvailable) {
            friday = new Day();
            setDayAvailability(friday, eWeekDay.Friday,
                    m_TxtStartTimeFriday.getText().toString(),
                    m_TxtEndTimeFriday.getText().toString());
        }
        if(m_IsSaturdayAvailable) {
            saturday = new Day();
            setDayAvailability(saturday, eWeekDay.Saturday,
                    m_TxtStartTimeSaturday.getText().toString(),
                    m_TxtEndTimeSaturday.getText().toString());
        }

        Week week = new Week();
        setWeekAvailability(week, sunday, monday, tuesday,
                wednesday, thursday, friday, saturday);
        UserAvailability availability = new UserAvailability();
        availability.setWeekAvailability(week);
        availability.setIsAlwaysAvailable(m_IsAlwaysAvailable);
        availability.setIsNotDisturb(m_IsNotDisturb);
        availability.setIsNoNumChatsLimit(m_IsNoNumChatsLimit);
        DatabaseReference databaseRef = m_Database.getReference("Users");
        databaseRef.child(m_CurrentUser.getUid()).child("userAvailability").setValue(availability);

    }

    private void setDayAvailability(Day i_Day, eWeekDay i_WeekDay,
                                    String i_StartTime,  String i_EndTime)
    {
        i_Day.setDay(i_WeekDay);
        i_Day.setIsAvailable(true);
        i_Day.setTimeFrom(i_StartTime);
        i_Day.setTimeTo(i_EndTime);
    }


    private void setWeekAvailability(Week i_Week, Day i_Sun, Day i_Mon, Day i_Tue,
                                     Day i_Wed, Day i_Thu, Day i_Fri, Day i_Sat)
    {
        if(i_Sun != null) {
            i_Week.setSunday(i_Sun);
        }
        if(i_Mon != null) {
            i_Week.setMonday(i_Mon);
        }
        if(i_Tue != null) {
            i_Week.setTuesday(i_Tue);
        }
        if(i_Wed != null) {
            i_Week.setWednesday(i_Wed);
        }
        if(i_Thu != null) {
            i_Week.setThursday(i_Thu);
        }
        if(i_Fri != null) {
            i_Week.setFriday(i_Fri);
        }
        if(i_Sat != null) {
            i_Week.setSaturday(i_Sat);
        }
    }




    @Override
    public void onCheckedChanged(CompoundButton i_ButtonView, boolean i_IsChecked)
    {
        int id = i_ButtonView.getId();

        if(m_SwitchAlwaysAvailable.getId() == id)
        {
            if(i_IsChecked) {
                m_IsAlwaysAvailable = true;
                m_IsNotDisturb = false;
                m_SwitchNotDisturb.setChecked(false);
                switchOnAllDays();
            }else{
                if(m_IsDisableAllDays) {
                    switchOffAllDays();
                }
                m_IsAlwaysAvailable = false;
            }
        }
        else if(m_SwitchNotDisturb.getId() == id)
        {
            if (i_IsChecked) {
                m_IsDisableAllDays = true;
                m_IsNotDisturb = true;
                m_IsAlwaysAvailable = false;
                m_SwitchAlwaysAvailable.setChecked(false);
                switchOffAllDays();
            }else {
                //switchOnAllDays();
            }
        }
    }


    private void switchOnAllDays()
    {
        m_AvailableDays = 7;
        switchOn(m_SwitchSunday, m_BtnStartTimeSunday, m_BtnEndTimeSunday,
                m_TxtStartTimeSunday, m_TxtEndTimeSunday);
        m_IsSundayAvailable = true;
        switchOn(m_SwitchMonday, m_BtnStartTimeMonday, m_BtnEndTimeMonday,
                m_TxtStartTimeMonday, m_TxtEndTimeMonday);
        m_IsMondayAvailable = true;
        switchOn(m_SwitchTuesday, m_BtnStartTimeTuesday, m_BtnEndTimeTuesday,
                m_TxtStartTimeTuesday, m_TxtEndTimeTuesday);
        m_IsTuesdayAvailable = true;
        switchOn(m_SwitchWednesday, m_BtnStartTimeWednesday, m_BtnEndTimeWednesday,
                m_TxtStartTimeWednesday, m_TxtEndTimeWednesday);
        m_IsWednesdayAvailable = true;
        switchOn(m_SwitchThursday, m_BtnStartTimeThursday, m_BtnEndTimeThursday,
                m_TxtStartTimeThursday, m_TxtEndTimeThursday);
        m_IsThursdayAvailable = true;
        switchOn(m_SwitchFriday, m_BtnStartTimeFriday, m_BtnEndTimeFriday,
                m_TxtStartTimeFriday, m_TxtEndTimeFriday);
        m_IsFridayAvailable = true;
        switchOn(m_SwitchSaturday, m_BtnStartTimeSaturday, m_BtnEndTimeSaturday,
                m_TxtStartTimeSaturday, m_TxtEndTimeSaturday);
        m_IsSaturdayAvailable = true;
    }

    private void switchOffAllDays()
    {
        m_AvailableDays = 0;
        switchOff(m_SwitchSunday, m_BtnStartTimeSunday, m_BtnEndTimeSunday,
                m_TxtStartTimeSunday, m_TxtEndTimeSunday);
        m_IsSundayAvailable = false;
        switchOff(m_SwitchMonday, m_BtnStartTimeMonday, m_BtnEndTimeMonday,
                m_TxtStartTimeMonday, m_TxtEndTimeMonday);
        m_IsMondayAvailable = false;
        switchOff(m_SwitchTuesday, m_BtnStartTimeTuesday, m_BtnEndTimeTuesday,
                m_TxtStartTimeTuesday, m_TxtEndTimeTuesday);
        m_IsTuesdayAvailable = false;
        switchOff(m_SwitchWednesday, m_BtnStartTimeWednesday, m_BtnEndTimeWednesday,
                m_TxtStartTimeWednesday, m_TxtEndTimeWednesday);
        m_IsWednesdayAvailable = false;
        switchOff(m_SwitchThursday, m_BtnStartTimeThursday, m_BtnEndTimeThursday,
                m_TxtStartTimeThursday, m_TxtEndTimeThursday);
        m_IsThursdayAvailable = false;
        switchOff(m_SwitchFriday, m_BtnStartTimeFriday, m_BtnEndTimeFriday,
                m_TxtStartTimeFriday, m_TxtEndTimeFriday);
        m_IsFridayAvailable = false;
        switchOff(m_SwitchSaturday, m_BtnStartTimeSaturday, m_BtnEndTimeSaturday,
                m_TxtStartTimeSaturday, m_TxtEndTimeSaturday);
        m_IsSaturdayAvailable = false;
    }

    private void switchOn(Button i_WeekDay, Button i_TimeFrom, Button i_TimeTo,
                          TextView i_TxtFrom, TextView i_TxtTo)
    {
        if(m_SwitchNotDisturb.isChecked())
        {
            m_IsNotDisturb = false;
            m_SwitchNotDisturb.setChecked(false);
        }
        if(m_AvailableDays == 7)
        {
            m_IsDisableAllDays = true;
            m_IsAlwaysAvailable = true;
            m_SwitchAlwaysAvailable.setChecked(true);
        }
//        i_TxtFrom.setText("00:00");
//        i_TxtTo.setText("23:59");
        i_WeekDay.setBackground(getDrawable(R.drawable.btn_round_blue));
        i_WeekDay.setTextColor(getColor(R.color.white));

        i_TimeFrom.setBackground(getDrawable(R.drawable.btn_round_green_light));
        i_TimeFrom.setText("+");
        i_TimeFrom.setEnabled(true);

        i_TimeTo.setBackground(getDrawable(R.drawable.btn_round_green_light));
        i_TimeTo.setText("+");
        i_TimeTo.setEnabled(true);
    }

    private void switchOff(Button i_WeekDay, Button i_BtnFrom, Button i_BtnTo, TextView i_TxtFrom, TextView i_TxtTo)
    {
        if(m_SwitchAlwaysAvailable.isChecked())
        {
            m_IsDisableAllDays = false;
            m_IsAlwaysAvailable = false;
            m_SwitchAlwaysAvailable.setChecked(false);
        }
        i_TxtFrom.setVisibility(View.INVISIBLE);
        i_TxtFrom.setText("00:00");
        i_TxtTo.setVisibility(View.INVISIBLE);
        i_TxtTo.setText("23:59");

        i_WeekDay.setBackground(getDrawable(R.drawable.btn_round_white_with_blue_frame));
        i_WeekDay.setTextColor(getColor(R.color.black));

        i_BtnFrom.setBackground(getDrawable(R.drawable.btn_round_white_with_black_frame));
        i_BtnFrom.setText("-");
        i_BtnFrom.setVisibility(View.VISIBLE);
        i_BtnFrom.setEnabled(false);

        i_BtnTo.setBackground(getDrawable(R.drawable.btn_round_white_with_black_frame));
        i_BtnTo.setText("-");
        i_BtnTo.setVisibility(View.VISIBLE);
        i_BtnTo.setEnabled(false);
    }

    private void setSundayClick()
    {
        if(m_IsSundayAvailable)
        {
            m_IsSundayAvailable = false;
            m_AvailableDays--;
            switchOff(m_SwitchSunday, m_BtnStartTimeSunday, m_BtnEndTimeSunday,
                    m_TxtStartTimeSunday, m_TxtEndTimeSunday);
        }else{
            m_IsSundayAvailable = true;
            m_AvailableDays++;
            switchOn(m_SwitchSunday, m_BtnStartTimeSunday, m_BtnEndTimeSunday,
                    m_TxtStartTimeSunday, m_TxtEndTimeSunday);
        }
    }

    private void setMondayClick()
    {
        if(m_IsMondayAvailable)
        {
            m_IsMondayAvailable = false;
            m_AvailableDays--;
            switchOff(m_SwitchMonday, m_BtnStartTimeMonday, m_BtnEndTimeMonday,
                    m_TxtStartTimeMonday, m_TxtEndTimeMonday);
        }
        else {
            m_IsMondayAvailable = true;
            m_AvailableDays++;
            switchOn(m_SwitchMonday, m_BtnStartTimeMonday, m_BtnEndTimeMonday,
                    m_TxtStartTimeMonday, m_TxtEndTimeMonday);
        }
    }

    private void setTuesdayClick()
    {
        if(m_IsTuesdayAvailable)
        {
            m_IsTuesdayAvailable = false;
            m_AvailableDays--;
            switchOff(m_SwitchTuesday, m_BtnStartTimeTuesday, m_BtnEndTimeTuesday,
                    m_TxtStartTimeTuesday, m_TxtEndTimeTuesday);
        }
        else{
            m_IsTuesdayAvailable = true;
            m_AvailableDays++;
            switchOn(m_SwitchTuesday, m_BtnStartTimeTuesday, m_BtnEndTimeTuesday,
                    m_TxtStartTimeTuesday, m_TxtEndTimeTuesday);
        }
    }

    private void setWednesdayClick()
    {
        if(m_IsWednesdayAvailable)
        {
            m_IsWednesdayAvailable = false;
            m_AvailableDays--;
            switchOff(m_SwitchWednesday, m_BtnStartTimeWednesday, m_BtnEndTimeWednesday,
                    m_TxtStartTimeWednesday, m_TxtEndTimeWednesday);
        }
        else{
            m_IsWednesdayAvailable = true;
            m_AvailableDays++;
            switchOn(m_SwitchWednesday, m_BtnStartTimeWednesday, m_BtnEndTimeWednesday,
                    m_TxtStartTimeWednesday, m_TxtEndTimeWednesday);
        }
    }

    private void setThursdayClick()
    {
        if(m_IsThursdayAvailable)
        {
            m_IsThursdayAvailable = false;
            m_AvailableDays--;
            switchOff(m_SwitchThursday, m_BtnStartTimeThursday, m_BtnEndTimeThursday,
                    m_TxtStartTimeThursday, m_TxtEndTimeThursday);
        }
        else{
            m_IsThursdayAvailable = true;
            m_AvailableDays++;
            switchOn(m_SwitchThursday, m_BtnStartTimeThursday, m_BtnEndTimeThursday,
                    m_TxtStartTimeThursday, m_TxtEndTimeThursday);
        }
    }

    private void setFridayClick()
    {
        if(m_IsFridayAvailable)
        {
            m_IsFridayAvailable = false;
            m_AvailableDays--;
            switchOff(m_SwitchFriday, m_BtnStartTimeFriday, m_BtnEndTimeFriday,
                    m_TxtStartTimeFriday, m_TxtEndTimeFriday);
        }
        else{
            m_IsFridayAvailable = true;
            m_AvailableDays++;
            switchOn(m_SwitchFriday, m_BtnStartTimeFriday, m_BtnEndTimeFriday,
                    m_TxtStartTimeFriday, m_TxtEndTimeFriday);
        }
    }

    private void setSaturdayClick()
    {
        if(m_IsSaturdayAvailable)
        {
            m_IsSaturdayAvailable = false;
            m_AvailableDays--;
            switchOff(m_SwitchSaturday, m_BtnStartTimeSaturday, m_BtnEndTimeSaturday,
                    m_TxtStartTimeSaturday, m_TxtEndTimeSaturday);
        }
        else{
            m_IsSaturdayAvailable = true;
            m_AvailableDays++;
            switchOn(m_SwitchSaturday, m_BtnStartTimeSaturday, m_BtnEndTimeSaturday,
                    m_TxtStartTimeSaturday, m_TxtEndTimeSaturday);
        }
    }

    private void setStartTimeClicks(int i_ViewId) {
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

    private void setEndTimeClicks(int i_ViewId) {
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
        } else if (i_ViewId == m_BtnEndTimeWednesday.getId() || i_ViewId == m_TxtEndTimeWednesday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setWednesdayTime(i_SelectedHour,i_SelectedMinute, false);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnEndTimeThursday.getId() || i_ViewId == m_TxtEndTimeThursday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setThursdayTime(i_SelectedHour,i_SelectedMinute, false);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnEndTimeFriday.getId() || i_ViewId == m_TxtEndTimeFriday.getId()) {
            timePicker = new TimePickerDialog(ActivityExpertSettings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker i_TimePicker, int i_SelectedHour, int i_SelectedMinute) {
                    setFridayTime(i_SelectedHour,i_SelectedMinute, false);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        } else if (i_ViewId == m_BtnEndTimeSaturday.getId() || i_ViewId == m_TxtEndTimeSaturday.getId()) {
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
        
        if(i_IsStartTime)
        {
            m_BtnEndTimeSunday.setVisibility(View.INVISIBLE);
            m_TxtEndTimeSunday.setText("23:59");
            m_TxtEndTimeSunday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndTimeSunday.getText().toString())) {
                m_BtnStartTimeSunday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeSunday.setText(I_Hour + fixer + i_Minute);
                m_TxtStartTimeSunday.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
//                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
        else {
            m_BtnStartTimeSunday.setVisibility(View.INVISIBLE);
            m_TxtStartTimeSunday.setText("00:00");
            m_TxtStartTimeSunday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeSunday.getText().toString(), time)) {
                m_BtnEndTimeSunday.setVisibility(View.INVISIBLE);
                m_TxtEndTimeSunday.setText(I_Hour + fixer + i_Minute);
                m_TxtEndTimeSunday.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
//                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void setMondayTime(int I_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String fixer = (i_Minute < 10) ? ":0" : ":";
        String time = I_Hour + fixer + i_Minute;

        if(i_IsStartTime)
        {
            m_BtnEndTimeMonday.setVisibility(View.INVISIBLE);
            m_TxtEndTimeMonday.setText("23:59");
            m_TxtEndTimeMonday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndTimeMonday.getText().toString())) {
                m_BtnStartTimeMonday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeMonday.setText(I_Hour + fixer + i_Minute);
                m_TxtStartTimeMonday.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
//                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
        else {
            m_BtnStartTimeMonday.setVisibility(View.INVISIBLE);
            m_TxtStartTimeMonday.setText("00:00");
            m_TxtStartTimeMonday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeMonday.getText().toString(), time)) {
                m_BtnEndTimeMonday.setVisibility(View.INVISIBLE);
                m_TxtEndTimeMonday.setText(I_Hour + fixer + i_Minute);
                m_TxtEndTimeMonday.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
//                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void setTuesdayTime(int I_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String fixer = (i_Minute < 10) ? ":0" : ":";
        String time = I_Hour + fixer + i_Minute;

        if(i_IsStartTime)
        {
            m_BtnEndTimeTuesday.setVisibility(View.INVISIBLE);
            m_TxtEndTimeTuesday.setText("23:59");
            m_TxtEndTimeTuesday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndTimeTuesday.getText().toString())) {
                m_BtnStartTimeTuesday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeTuesday.setText(I_Hour + fixer + i_Minute);
                m_TxtStartTimeTuesday.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
//                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
        else {
            m_BtnStartTimeTuesday.setVisibility(View.INVISIBLE);
            m_TxtStartTimeTuesday.setText("00:00");
            m_TxtStartTimeTuesday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeTuesday.getText().toString(), time)) {
                m_BtnEndTimeTuesday.setVisibility(View.INVISIBLE);
                m_TxtEndTimeTuesday.setText(I_Hour + fixer + i_Minute);
                m_TxtEndTimeTuesday.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
//                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void setWednesdayTime(int I_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String fixer = (i_Minute < 10) ? ":0" : ":";
        String time = I_Hour + fixer + i_Minute;

        if(i_IsStartTime)
        {
            m_BtnEndTimeWednesday.setVisibility(View.INVISIBLE);
            m_TxtEndTimeWednesday.setText("23:59");
            m_TxtEndTimeWednesday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndTimeWednesday.getText().toString())) {
                m_BtnStartTimeWednesday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeWednesday.setText(I_Hour + fixer + i_Minute);
                m_TxtStartTimeWednesday.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
//                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
        else {
            m_BtnStartTimeWednesday.setVisibility(View.INVISIBLE);
            m_TxtStartTimeWednesday.setText("00:00");
            m_TxtStartTimeWednesday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeWednesday.getText().toString(), time)) {
                m_BtnEndTimeWednesday.setVisibility(View.INVISIBLE);
                m_TxtEndTimeWednesday.setText(I_Hour + fixer + i_Minute);
                m_TxtEndTimeWednesday.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
//                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void setThursdayTime(int I_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String fixer = (i_Minute < 10) ? ":0" : ":";
        String time = I_Hour + fixer + i_Minute;

        if(i_IsStartTime)
        {
            m_BtnEndTimeThursday.setVisibility(View.INVISIBLE);
            m_TxtEndTimeThursday.setText("23:59");
            m_TxtEndTimeThursday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndTimeThursday.getText().toString())) {
                m_BtnStartTimeThursday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeThursday.setText(I_Hour + fixer + i_Minute);
                m_TxtStartTimeThursday.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
//                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
        else {
            m_BtnStartTimeThursday.setVisibility(View.INVISIBLE);
            m_TxtStartTimeThursday.setText("00:00");
            m_TxtStartTimeThursday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeThursday.getText().toString(), time)) {
                m_BtnEndTimeThursday.setVisibility(View.INVISIBLE);
                m_TxtEndTimeThursday.setText(I_Hour + fixer + i_Minute);
                m_TxtEndTimeThursday.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
//                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void setFridayTime(int I_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String fixer = (i_Minute < 10) ? ":0" : ":";
        String time = I_Hour + fixer + i_Minute;

        if(i_IsStartTime)
        {
            m_BtnEndTimeFriday.setVisibility(View.INVISIBLE);
            m_TxtEndTimeFriday.setText("23:59");
            m_TxtEndTimeFriday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndTimeFriday.getText().toString())) {
                m_BtnStartTimeFriday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeFriday.setText(I_Hour + fixer + i_Minute);
                m_TxtStartTimeFriday.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
//                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
        else {
            m_BtnStartTimeFriday.setVisibility(View.INVISIBLE);
            m_TxtStartTimeFriday.setText("00:00");
            m_TxtStartTimeFriday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeFriday.getText().toString(), time)) {
                m_BtnEndTimeFriday.setVisibility(View.INVISIBLE);
                m_TxtEndTimeFriday.setText(I_Hour + fixer + i_Minute);
                m_TxtEndTimeFriday.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
//                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void setSaturdayTime(int I_Hour, int i_Minute, boolean i_IsStartTime)
    {
        String errorMessage = "Invalid time span";
        String fixer = (i_Minute < 10) ? ":0" : ":";
        String time = I_Hour + fixer + i_Minute;

        if(i_IsStartTime)
        {
            m_BtnEndTimeSaturday.setVisibility(View.INVISIBLE);
            m_TxtEndTimeSaturday.setText("23:59");
            m_TxtEndTimeSaturday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(time, m_TxtEndTimeSaturday.getText().toString())) {
                m_BtnStartTimeSaturday.setVisibility(View.INVISIBLE);
                m_TxtStartTimeSaturday.setText(I_Hour + fixer + i_Minute);
                m_TxtStartTimeSaturday.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
//                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
        else {
            m_BtnStartTimeSaturday.setVisibility(View.INVISIBLE);
            m_TxtStartTimeSaturday.setText("00:00");
            m_TxtStartTimeSaturday.setVisibility(View.VISIBLE);

            if (InputValidation.IsValidTimeSpan(m_TxtStartTimeSaturday.getText().toString(), time)) {
                m_BtnEndTimeSaturday.setVisibility(View.INVISIBLE);
                m_TxtEndTimeSaturday.setText(I_Hour + fixer + i_Minute);
                m_TxtEndTimeSaturday.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ActivityExpertSettings.this, errorMessage, Toast.LENGTH_SHORT).show();
//                Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void setActivityContent()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        m_BtnSubjects = findViewById(R.id.btn_subjects_expert_settings);
        m_BtnSubjects.setOnClickListener(this);
        m_BtnNext = findViewById(R.id.btn_next_of_expert_settings);
        m_BtnNext.setOnClickListener(this);
        m_SwitchAlwaysAvailable = findViewById(R.id.switch_always_available);
        m_SwitchAlwaysAvailable.setOnCheckedChangeListener(this);
        m_SwitchNotDisturb = findViewById(R.id.switch_never_available);
        m_SwitchNotDisturb.setOnCheckedChangeListener(this);

        setSwitchesOfWeekDays();
        setTimePickButtons();
        setTimePickTexts();
    }

    private void setFirebaseData()
    {
        m_Auth = FirebaseAuth.getInstance();
        m_CurrentUser = m_Auth.getCurrentUser();
        m_Database = FirebaseDatabase.getInstance();
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
        m_TxtEndTimeWednesday = findViewById(R.id.txt_end_time_wednesday);
        m_TxtEndTimeWednesday.setOnClickListener(this);
        m_TxtEndTimeThursday = findViewById(R.id.txt_end_time_thursday);
        m_TxtEndTimeThursday.setOnClickListener(this);
        m_TxtEndTimeFriday = findViewById(R.id.txt_end_time_friday);
        m_TxtEndTimeFriday.setOnClickListener(this);
        m_TxtEndTimeSaturday = findViewById(R.id.txt_end_time_saturday);
        m_TxtEndTimeSaturday.setOnClickListener(this);
    }

    private void startHomeActivity()
    {
        Intent IntentHome = new Intent
                (ActivityExpertSettings.this, HomeActivity.class);
        startActivity(IntentHome);
    }
}
