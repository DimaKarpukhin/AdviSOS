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
    private String mSubjectId;

    public CollectExpertsForChatRoom(String i_subjcetId){
        mSubjectId = i_subjcetId;
        mDatabase = FirebaseDatabase.getInstance();
        mSubjectUsersReference = mDatabase.getReference("SubjectUsers");

    }

    @Override
    public void run() {
        mSubjectUsersReference.child(mSubjectId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    boolean isAvailableToChat = checkIfAvailabilityFitsToChat(ds.getValue(SubjectUser.class));
                    if(isAvailableToChat) {
                        mExpertUserOfSubjectSelectedId.add(ds.getValue(SubjectUser.class).getUserId());
                        if(mExpertUserOfSubjectSelectedId.size() == NUM_OF_EXPERTS)
                            break;
                    }
                    else{
                        continue;
                    }

                }
                notifyAll();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean checkIfAvailabilityFitsToChat(SubjectUser i_userUid) {
        //TODO implement this method to find the users that could be added to the chat
        //TODO according to their availability.
        return true;
    }

    public List<String> getmExpertUserOfSubjectSelectedId() {
        return mExpertUserOfSubjectSelectedId;
    }
}
