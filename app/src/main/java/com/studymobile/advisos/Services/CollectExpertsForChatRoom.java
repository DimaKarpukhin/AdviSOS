package com.studymobile.advisos.Services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.studymobile.advisos.Models.Day;
import com.studymobile.advisos.Models.SubjectUser;
import com.studymobile.advisos.Models.UserAvailability;
import com.studymobile.advisos.Models.UserLocation;
import com.studymobile.advisos.Models.Week;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.exit;

public class CollectExpertsForChatRoom
{
    private final static int NUM_OF_EXPERTS = 5;
    private List<SubjectUser> mExpertUserOfSubjectSelectedId = new ArrayList<>(NUM_OF_EXPERTS);
    private FirebaseDatabase mDatabase;
    private DatabaseReference mSubjectUsersReference;
    private String mSubjectName;
    private UserAvailability mUserAvailability;
    private ArrayList<String> mUserIDs = new ArrayList<>();
    private UserLocation mOpenerLoc = null;

    private boolean m_isUserAvailable;


    private class PairUserIdAndDistance implements Comparable
    {
        private String mUserID;
        private double mDistance;

        public String getUserID() {
            return mUserID;
        }

        public void setUserID(String i_userID) {
            this.mUserID = i_userID;
        }

        public double getDistance() {
            return mDistance;
        }

        public void setDistance(double i_distance) {
            this.mDistance = i_distance;
        }

        @Override
        public int compareTo(Object o) {
            PairUserIdAndDistance pair = (PairUserIdAndDistance)o;
            if(mDistance < pair.mDistance)
                return (-1);
            if(mDistance > pair.mDistance)
                return 1;
            return 0;
        }
    }

    private ArrayList<PairUserIdAndDistance> mPairsUserIdAndDistance = new ArrayList<>();


    public CollectExpertsForChatRoom(String i_subjcetName){
        mSubjectName = i_subjcetName;
        mDatabase = FirebaseDatabase.getInstance();
        mSubjectUsersReference = mDatabase.getReference("SubjectUsers");
    }

    public void run() {
        mSubjectUsersReference.child(mSubjectName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                SubjectUser su;
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    su = ds.getValue(SubjectUser.class);
                    checkAndSetUserAvailability(su);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void collectExpertsByLocation()
    {
        FirebaseDatabase.getInstance().getReference("Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserLocation peerLoc;
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            //checkAndSetUserAvailability(ds.getKey());
                            if(m_isUserAvailable && ds.child("userLocation").exists())
                            {
                                PairUserIdAndDistance pair = new PairUserIdAndDistance();
                                pair.setUserID(ds.getKey());
                                peerLoc = ds.child("userLocation").getValue(UserLocation.class);
                                pair.setDistance(mOpenerLoc.distanceBetween(peerLoc));
                                mPairsUserIdAndDistance.add(pair);
                            }
                        }

                        Collections.sort(mPairsUserIdAndDistance);
                        SubjectUser su = new SubjectUser();
                        for (PairUserIdAndDistance pair : mPairsUserIdAndDistance)
                        {
                            su.setUserId(pair.getUserID());
                            mExpertUserOfSubjectSelectedId.add(su);
                            if(mExpertUserOfSubjectSelectedId.size() == NUM_OF_EXPERTS)
                                break;
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void collectExpertsRandomly()
    {
        FirebaseDatabase.getInstance().getReference("Users").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            mUserIDs.add(ds.getKey());
                        }
                        Collections.shuffle(mUserIDs);
                        String userID;
                        Iterator<String> itr = mUserIDs.iterator();
                        while (mExpertUserOfSubjectSelectedId.size() < NUM_OF_EXPERTS && itr.hasNext())
                        {
                            userID = itr.next();
                            //checkAndSetUserAvailability(userID);
                            if( m_isUserAvailable && !containsUserID(mExpertUserOfSubjectSelectedId, userID) )
                            {
                                SubjectUser su = new SubjectUser();
                                su.setUserId(userID);
                                mExpertUserOfSubjectSelectedId.add(su);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
    }

    private void checkAndSetUserAvailability(final SubjectUser i_User)
    {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(i_User.getUserId()).child("userAvailability");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot i_DataSnapshot) {
                if (i_DataSnapshot.exists())
                {
                    mUserAvailability = i_DataSnapshot.getValue(UserAvailability.class);
                    String path = "weekAvailability/";
                    if (i_DataSnapshot.child(path).exists())
                    {
                        Week week = i_DataSnapshot.getValue(Week.class);
                        if (i_DataSnapshot.child(path + "sunday").exists()) {
                            week.setSunday(i_DataSnapshot.child(path + "sunday").getValue(Day.class));
                        }
                        if (i_DataSnapshot.child(path + "monday").exists()) {
                            week.setMonday(i_DataSnapshot.child(path + "monday").getValue(Day.class));
                        }
                        if (i_DataSnapshot.child(path + "tuesday").exists()) {
                            week.setTuesday(i_DataSnapshot.child(path + "tuesday").getValue(Day.class));
                        }
                        if (i_DataSnapshot.child(path + "wednesday").exists()) {
                            week.setWednesday(i_DataSnapshot.child(path + "wednesday").getValue(Day.class));
                        }
                        if (i_DataSnapshot.child(path + "thursday").exists()) {
                            week.setThursday(i_DataSnapshot.child(path + "thursday").getValue(Day.class));
                        }
                        if (i_DataSnapshot.child(path + "friday").exists()) {
                            week.setFriday(i_DataSnapshot.child(path + "friday").getValue(Day.class));
                        }
                        if (i_DataSnapshot.child(path + "saturday").exists()) {
                            week.setSaturday(i_DataSnapshot.child(path + "saturday").getValue(Day.class));
                        }

                        mUserAvailability.setWeekAvailability(week);
                        if(mExpertUserOfSubjectSelectedId.size() == NUM_OF_EXPERTS
                                && i_User.getIsValid() && i_User.getRating() != null && isNowAvailable())
                        {
                            for(SubjectUser x : mExpertUserOfSubjectSelectedId) {
                                if(i_User.getRating().getAvgRating() > x.getRating().getAvgRating()) {
                                    mExpertUserOfSubjectSelectedId.remove(x);
                                    mExpertUserOfSubjectSelectedId.add(i_User);
                                    break;
                                }
                            }
                        }
                        else if(i_User.getIsValid() && m_isUserAvailable)
                        {
                            mExpertUserOfSubjectSelectedId.add(i_User);
                        }


                        if(mExpertUserOfSubjectSelectedId.size() < NUM_OF_EXPERTS && mOpenerLoc != null)
                        {
                            ////collect by location
                            collectExpertsByLocation();
                        }
                        if(mExpertUserOfSubjectSelectedId.size() < NUM_OF_EXPERTS)
                        {   //collect randomly
                            collectExpertsRandomly();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isNowAvailable()
    {
        m_isUserAvailable = false;

        if(mUserAvailability != null
                && !mUserAvailability.getIsNeverAvailable()
                && mUserAvailability.getWeekAvailability() != null)
        {
            Week weekAvailability = mUserAvailability.getWeekAvailability();
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            Date date = new Date();
            DateFormat format = new SimpleDateFormat("HH:mm");
            String currTime = format.format(date);
            String startTime = null, endTime = null;

            switch (day) {
                case Calendar.SUNDAY:
                    // Current day is Sunday
                    startTime = weekAvailability.getSunday().getStartTime();
                    endTime = weekAvailability.getSunday().getEndTime();
                    break;
                case Calendar.MONDAY:
                    // Current day is Monday
                    startTime = weekAvailability.getMonday().getStartTime();
                    endTime = weekAvailability.getMonday().getEndTime();
                    break;
                case Calendar.TUESDAY:
                    // Current day is Tuesday
                    startTime = weekAvailability.getTuesday().getStartTime();
                    endTime = weekAvailability.getTuesday().getEndTime();
                    break;
                case Calendar.WEDNESDAY:
                    // Current day is Wednesday
                    startTime = weekAvailability.getWednesday().getStartTime();
                    endTime = weekAvailability.getWednesday().getEndTime();
                    break;
                case Calendar.THURSDAY:
                    // Current day is Thursday
                    startTime = weekAvailability.getThursday().getStartTime();
                    endTime = weekAvailability.getThursday().getEndTime();
                    break;
                case Calendar.FRIDAY:
                    // Current day is Friday
                    startTime = weekAvailability.getFriday().getStartTime();
                    endTime = weekAvailability.getFriday().getEndTime();
                    break;
                case Calendar.SATURDAY:
                    // Current day is Saturday
                    startTime = weekAvailability.getSaturday().getStartTime();
                    endTime = weekAvailability.getSaturday().getEndTime();
                    break;
            }

            return isBetween(currTime, startTime, endTime);
        }

        return true;
    }


    private boolean isBetween(String i_currTime, String i_startTime, String i_endTime)
    {
        // all parameters are in format hh:mm

        int currTimehours, currTimeMinutes, startTimeHours, startTimeMinutes, endTimeHours, endTimeMinutes;

        String[] arr = i_currTime.split(":");
        currTimehours = Integer.parseInt(arr[0]);
        currTimeMinutes = Integer.parseInt(arr[1]);

        arr = i_startTime.split(":");
        startTimeHours = Integer.parseInt(arr[0]);
        startTimeMinutes = Integer.parseInt(arr[1]);

        arr = i_endTime.split(":");
        endTimeHours = Integer.parseInt(arr[0]);
        endTimeMinutes = Integer.parseInt(arr[1]);

        if(!(currTimehours >= startTimeHours && currTimehours <= endTimeHours))
            return false;

        if(currTimehours == startTimeHours && currTimeMinutes < startTimeMinutes)
            return false;

        if(currTimehours == endTimeHours && currTimeMinutes > endTimeMinutes)
            return false;

        return true;
    }


    public List<SubjectUser> getExpertUserOfSubjectSelectedId() {
        return mExpertUserOfSubjectSelectedId;
    }


    private boolean containsUserID(List<SubjectUser> i_list, String i_userID)
    {
        for(SubjectUser su : i_list)
        {
            if(su.getUserId().equals(i_userID))
                return true;
        }
        return false;
    }
}