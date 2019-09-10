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
        if(isAvailable("H1pM0hYrerZ77JX5oXx2IEsiB6I3")) {
            SubjectUser user = new SubjectUser();
            user.setUserId("H1pM0hYrerZ77JX5oXx2IEsiB6I3");
            mExpertUserOfSubjectSelectedId.add(user);
        }
//        mSubjectUsersReference.child(mSubjectName).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                SubjectUser su;
//                for(DataSnapshot ds : dataSnapshot.getChildren())
//                {
//                    su = ds.getValue(SubjectUser.class);
//
//                    if(mExpertUserOfSubjectSelectedId.size() == NUM_OF_EXPERTS &&
//                            su.getIsValid() && ds.child("Rating").exists() &&
//                            isAvailable(su.getUserId()))
//                    {
//                        for(SubjectUser x : mExpertUserOfSubjectSelectedId)
//                        {
//                            if(su.getRating().getAvgRating() > x.getRating().getAvgRating())
//                            {
//                                mExpertUserOfSubjectSelectedId.remove(x);
//                                mExpertUserOfSubjectSelectedId.add(su);
//                                break;
//                            }
//                        }
//                    }
//                    else if(su.getIsValid() && isAvailable(su.getUserId()))
//                    {
//                        mExpertUserOfSubjectSelectedId.add(su);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        if(mExpertUserOfSubjectSelectedId.size() < NUM_OF_EXPERTS && mOpenerLoc != null)
//        {
//            ////collect by location
//            FirebaseDatabase.getInstance().getReference("Users")
//            .addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    UserLocation peerLoc;
//                    for (DataSnapshot ds : dataSnapshot.getChildren())
//                    {
//                        if(isAvailable(ds.getKey()) && ds.child("userLocation").exists())
//                        {
//                            PairUserIdAndDistance pair = new PairUserIdAndDistance();
//                            pair.setUserID(ds.getKey());
//                            peerLoc = ds.child("userLocation").getValue(UserLocation.class);
//                            pair.setDistance(mOpenerLoc.distanceBetween(peerLoc));
//                            mPairsUserIdAndDistance.add(pair);
//                        }
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//
//            Collections.sort(mPairsUserIdAndDistance);
//            SubjectUser su = new SubjectUser();
//            for (PairUserIdAndDistance pair : mPairsUserIdAndDistance)
//            {
//                su.setUserId(pair.getUserID());
//                mExpertUserOfSubjectSelectedId.add(su);
//                if(mExpertUserOfSubjectSelectedId.size() == NUM_OF_EXPERTS)
//                    break;
//            }
//        }
//
//        if(mExpertUserOfSubjectSelectedId.size() < NUM_OF_EXPERTS)
//        {   //collect randomly
//            getAllUserIds();
//            Collections.shuffle(mUserIDs);
//            String userID;
//            Iterator<String> itr = mUserIDs.iterator();
//            while (mExpertUserOfSubjectSelectedId.size() < NUM_OF_EXPERTS && itr.hasNext())
//            {
//                userID = itr.next();
//                if( isAvailable(userID) && !containsUserID(mExpertUserOfSubjectSelectedId, userID) )
//                {
//                    SubjectUser su = new SubjectUser();
//                    su.setUserId(userID);
//                    mExpertUserOfSubjectSelectedId.add(su);
//                }
//            }
//        }
    }

    private boolean isAvailable(String i_userID)
    {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(i_userID).child("userAvailability");
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
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(mUserAvailability == null)
            return false;

        if(mUserAvailability.getIsAlwaysAvailable())
            return true;
        if(mUserAvailability.getIsNeverAvailable())
            return false;


        Week weekAvailability = mUserAvailability.getWeekAvailability();
        if(weekAvailability == null)
            return false;

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

    private void getAllUserIds()
    {
        FirebaseDatabase.getInstance().getReference("Users").
        addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    mUserIDs.add(ds.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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