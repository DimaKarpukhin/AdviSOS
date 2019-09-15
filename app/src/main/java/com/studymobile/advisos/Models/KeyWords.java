package com.studymobile.advisos.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyWords {

    private Map<String,Integer> mKeyWords;
    public KeyWords(){}


    public void setmKeyWords(Map<String,Integer> i_mKeyWords) {
        this.mKeyWords = new HashMap<>(i_mKeyWords);

    }

    public Map<String,Integer> getmKeyWords() {
        return mKeyWords;
    }
}
