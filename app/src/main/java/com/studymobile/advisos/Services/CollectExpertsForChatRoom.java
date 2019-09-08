package com.studymobile.advisos.Services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.studymobile.advisos.Models.SubjectUser;

import java.util.ArrayList;
import java.util.List;

public class CollectExpertsForChatRoom implements Runnable{
    private final static int NUM_OF_EXPERTS = 5;
    private List<String> mExpertUserOfSubjectSelectedId = new ArrayList<>(NUM_OF_EXPERTS);
    private FirebaseDatabase mDatabase;
    private DatabaseReference mSubjectUsersReference;
    private String mSubjectName;

    public CollectExpertsForChatRoom(String i_subjcetName){
        mSubjectName = i_subjcetName;
        mDatabase = FirebaseDatabase.getInstance();
        mSubjectUsersReference = mDatabase.getReference("SubjectUsers");

    }

    @Override
    public void run() {
        mSubjectUsersReference.child(mSubjectName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String lastEnteredUserID = null;
                float minAvgRating = -1;
                SubjectUser su;
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    su = ds.getValue(SubjectUser.class);
                    if(mExpertUserOfSubjectSelectedId.size() == NUM_OF_EXPERTS)
                    {
                        if(su.getIsValid() && ds.child("Rating").exists())
                        {
                            if(isAvailable(su.getUserId()) && su.getRating().getAvgRating() > minAvgRating)
                            {
                                mExpertUserOfSubjectSelectedId.remove(lastEnteredUserID);
                                mExpertUserOfSubjectSelectedId.add(su.getUserId());
                            }
                        }
                    }
                    else if(isAvailable(su.getUserId()))
                    {
                        if(su.getIsValid())
                            mExpertUserOfSubjectSelectedId.add(su.getUserId());
                    }
                    else
                    {
                        continue;
                    }

                    updateMinAvgRating();
                    lastEnteredUserID = su.getUserId();

                }
                notifyAll();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(mExpertUserOfSubjectSelectedId.size() < NUM_OF_EXPERTS)
        {
            // collect randomally/by location
        }

        if(mExpertUserOfSubjectSelectedId.isEmpty())
        {
            // sending proper message to user - NOBODY IS  AVAILABLE
            return;
        }

    }

    private void updateMinAvgRating()
    {
        //TODO
    }

    private boolean isAvailable(String i_userID)
    {
        //TODO
        return true;
    }


    public List<String> getmExpertUserOfSubjectSelectedId() {
        return mExpertUserOfSubjectSelectedId;
    }
}
