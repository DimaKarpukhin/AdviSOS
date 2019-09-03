package com.studymobile.advisos.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studymobile.advisos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAdviceMe extends Fragment {


    public FragmentAdviceMe() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advice_me, container, false);
    }

}
