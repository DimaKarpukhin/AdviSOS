package com.studymobile.advisos.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.studymobile.advisos.Activities.ActivitySubjectActionManager;
import com.studymobile.advisos.Interfaces.ItemClickListener;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderSubject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSubjects extends Fragment
{
    private static final String SUBJECT_NAME = "subject_name";

    private View mSubjectsView;
    private RecyclerView mSubjectsList;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mSubjectRef;
    private FirebaseAuth mAuth;
    private String mSubjectID;

    private FirebaseRecyclerOptions<Subject> mOptions;
    private FirebaseRecyclerAdapter<Subject, ViewHolderSubject> mAdapter;

    private Dialog m_PopupDialog;
    private CircleImageView m_DialogImgView;

    public FragmentSubjects() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater i_Inflater, ViewGroup i_Container,
                             Bundle i_SavedInstanceState)
    {
        // Inflate the layout for this fragment
        mSubjectsView = i_Inflater.inflate(R.layout.fragment_subjects, i_Container, false);
        mSubjectsList = mSubjectsView.findViewById(R.id.recycler_subjects_list_of_fragment_subjects);
        mSubjectsList.setLayoutManager(new LinearLayoutManager(getContext()));

        mDatabase = FirebaseDatabase.getInstance();
        mSubjectRef = mDatabase.getReference().child("Subjects");

        buildSubjectsOptions();
        populateSubjectsView();

        return mSubjectsView;
    }


    @Override
    public void onStart()
    {
        super.onStart();
        if(mAdapter != null)
        {
            mAdapter.startListening();
        }

    }

    @Override
    public void onStop()
    {
        if(mAdapter != null)
        {
            mAdapter.stopListening();
        }

        super.onStop();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(mAdapter != null)
        {
            mAdapter.startListening();
        }
    }

    private void buildSubjectsOptions()
    {
        mOptions = new FirebaseRecyclerOptions.Builder<Subject>()
                .setQuery(mSubjectRef, Subject.class)
                .build();
    }

    private void populateSubjectsView()
    {
        mAdapter = new FirebaseRecyclerAdapter<Subject, ViewHolderSubject>(mOptions) {
            @NonNull
            @Override
            public ViewHolderSubject onCreateViewHolder
                    (@NonNull ViewGroup i_ViewGroup, int i_Position)
            {
                View view = LayoutInflater
                        .from(i_ViewGroup.getContext())
                        .inflate(R.layout.item_subject, i_ViewGroup, false);

                return new ViewHolderSubject(view);
            }

            @Override
            protected void onBindViewHolder
                    (@NonNull ViewHolderSubject i_ViewHolder, int i_Position,
                     @NonNull final Subject i_Subject)
            {
                Picasso.get().load(i_Subject.getImgLink()).into(i_ViewHolder.getSubjectImage());
                i_ViewHolder.getCheckBox().setVisibility(View.INVISIBLE);
                i_ViewHolder.getArrowRightIcon().setVisibility(View.VISIBLE);
                i_ViewHolder.setSubjectName(i_Subject.getSubjectName());
                i_ViewHolder.getSubjectImage().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupDialog(i_Subject.getImgLink(), i_Subject.getSubjectName());
                    }
                });
                i_ViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View i_View, int i_Position, boolean i_IsLongClick) {
                        startSubjectActionManagerActivity(i_Position);
                    }
                });
            }
        };

        mAdapter.startListening();
        mSubjectsList.setAdapter(mAdapter);
    }

    private void showPopupDialog(String i_ImgLink, String i_Title)
    {
        m_PopupDialog = new Dialog(getContext());
        m_PopupDialog.setContentView(R.layout.dialog_image);
        m_PopupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        m_DialogImgView = m_PopupDialog.findViewById(R.id.img_of_dialog_profile_picture);
        Picasso.get().load(i_ImgLink).into(m_DialogImgView);
        TextView title = m_PopupDialog.findViewById(R.id.txt_title_of_dialog_image);
        title.setText(i_Title);
        title.setVisibility(View.VISIBLE);

        m_PopupDialog.findViewById(R.id.layout_optional_of_dialog_image)
                .setVisibility(View.INVISIBLE);
        m_PopupDialog.findViewById(R.id.btn_ok_of_dialog_profile_picture)
                .setVisibility(View.INVISIBLE);
        m_PopupDialog.findViewById(R.id.btn_remove_of_dialog_profile_picture)
                .setVisibility(View.INVISIBLE);
        m_PopupDialog.findViewById(R.id.btn_add_a_photo_of_dialog_profile_picture)
                .setVisibility(View.INVISIBLE);


        m_PopupDialog.findViewById(R.id.btn_close_of_dialog_profile_picture)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        m_PopupDialog.dismiss();
                    }
                });

        m_PopupDialog.show();
    }

    private void startSubjectActionManagerActivity(int i_Position)
    {
        Intent IntentSubjectActionManager = new Intent(getActivity(),
                ActivitySubjectActionManager.class);
        IntentSubjectActionManager
                .putExtra(SUBJECT_NAME, mAdapter.getRef(i_Position).getKey());
        startActivity(IntentSubjectActionManager);
    }
}
