package com.studymobile.advisos.Services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;
import com.studymobile.advisos.Models.KeyWords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WordCounter implements Runnable {
    private FirebaseDatabase mDataBase;
    private DatabaseReference mChatMessagesReference;
    private String mChatRoomUid;
    private Map<String,Integer> mWordCount;

    public WordCounter(String i_ChatRoomUid)
    {
        mChatRoomUid = i_ChatRoomUid;
        mDataBase = FirebaseDatabase.getInstance();
        mChatMessagesReference = mDataBase.getReference("Messages").child(mChatRoomUid);
        mWordCount = new HashMap<>();

    }
    @Override
    public void run() {
        String[] ignoreList = {"if",
                "and",
                "or",
                "kind",
                "should","would","could","have","been",
                "for","its",
                "you","in", "at","it","i","will",
                "because",
                "me",
                "is",
                "are",
                "they",
                "not",
                "yes",
                "no",
                "them","then","can","this",
                "there",
                "here",
                "of",
                "to","the",
                "dont",
                "wasnt",
                "ok",
                "cool",
                "sure",
                "thanks",
                "thank you",
                "were",
                "as",
                "we",
                "well","tell",
                "also",
                "furthermore",
                "additionally","also",
                "be",
                "else",
                "all",
                "see",
                "tell","hh", "lol","LOL","LoL","hi","hello","hey"};
        Set<String> ignore = new HashSet<>(Arrays.asList(ignoreList));
        countKeyWords(ignore);

    }

    private void countKeyWords(final Set<String> i_Ignore) {

        mChatMessagesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot message : dataSnapshot.getChildren())
                    {
                        String body = message.child("messageBody").getValue(String.class);
                        String[] words = body.split("\\s+");
                        for(String word : words)
                        {
                            word = word.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
                            if(i_Ignore.contains(word)|| word.isEmpty() || word.contains(" ") || word.matches("\\s*")||word.length()<3)
                            {
                                continue;
                            }
                            else{
                                if(!mWordCount.containsKey(word))
                                {
                                    mWordCount.put(word,1);
                                    Log.w("key", word);
                                }
                                else
                                {
                                    mWordCount.put(word,mWordCount.get(word)+1);
                                }
                            }
                        }
                    }
                    if(!mWordCount.isEmpty()) {mWordCount = MapUtil.sortByValue(mWordCount);
                    setChatRoomKeyWordsToDataBase();}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setChatRoomKeyWordsToDataBase() {
        Map<String,Integer>keys = mWordCount.entrySet().stream().limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, HashMap::new));
         DatabaseReference chatRoomReference = mDataBase.getReference("ChatRooms").child(mChatRoomUid);
         chatRoomReference.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(dataSnapshot.exists())
                 {
                    String[]topic = dataSnapshot.child("roomName").getValue(String.class).split("\\s+");
                    for(String str: topic)
                    {
                        str = str.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
                        if( str.isEmpty() || str.contains(" ")|| str.matches("\\s*") || str.length()<3) continue;
                        else if(keys.containsKey(str)) keys.put(str,keys.get(str)+1);
                        else keys.put(str,1);
                        Log.w("key:", str);
                    }
                     KeyWords toDB = new KeyWords();
                    toDB.setmKeyWords(keys);
                    mDataBase.getReference("ChatRoomKeys").child(mChatRoomUid).setValue(toDB);
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
    }
}
