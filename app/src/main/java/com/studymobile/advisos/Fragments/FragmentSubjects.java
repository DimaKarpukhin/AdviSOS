package com.studymobile.advisos.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.studymobile.advisos.Activities.ActivityHomeScreen;
import com.studymobile.advisos.Activities.ActivitySubjectActionManager;
import com.studymobile.advisos.Interfaces.ItemClickListener;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderSubject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSubjects extends Fragment
        implements TextWatcher, PopupMenu.OnMenuItemClickListener {
    private static final String SUBJECT_NAME = "subject_name";
    private static final String ALPHABETICALLY = "subjectName";
    private static final String POPULARITY = "popularity";

    private View mSubjectsView;
    private RecyclerView mSubjectsList;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mSubjectRef;

    private FirebaseRecyclerOptions<Subject> mOptions;
    private FirebaseRecyclerAdapter<Subject, ViewHolderSubject> mAdapter;

    private Dialog m_PopupDialog;
    private CircleImageView m_DialogImgView;

    private MaterialSearchBar m_SearchBar;
    private List<String> m_SuggestionsList;
    private NavigationView m_NavigationView;
    private DrawerLayout m_DrawerLayout;

    private boolean m_IsViewCreated = false;
    private boolean m_IsViewShown = false;
    private Boolean m_IsStarted = false;

    public FragmentSubjects() {
        // Required empty public constructor
    }

    @Override
    public void onStart()
    {
        super.onStart();
        m_IsStarted = true;
//        if (m_IsViewShown){
//            //updateOptionsMenu();
//        }
//        //Toast.makeText(getActivity(), "Start=> SUBJECTS", Toast.LENGTH_SHORT).show();
        if(mAdapter != null)
        {
            mAdapter.startListening();
        }

    }

    @Override
    public void onStop()
    {
        //Toast.makeText(getActivity(), "Stop=> SUBJECTS", Toast.LENGTH_SHORT).show();
        if(mAdapter != null)
        {
            mAdapter.stopListening();
        }

        super.onStop();
    }

    @Override
    public void onResume()
    {
        //Toast.makeText(getActivity(), "Resume=> SUBJECTS", Toast.LENGTH_SHORT).show();
        super.onResume();
        //updateOptionsMenu();
        if(mAdapter != null)
        {
            mAdapter.startListening();
        }
    }

    @Override
    public void setUserVisibleHint(boolean i_IsVisible)
    {
        super.setUserVisibleHint(i_IsVisible);

//        if (getView() != null) {
//            m_IsViewShown = true;
//            Toast.makeText(getActivity(), "Hint=> SUBJECTS", Toast.LENGTH_SHORT).show();
//            updateOptionsMenu();
//        } else {
//            m_IsViewShown = false;
//        }

        m_IsViewShown = i_IsVisible;
        if (m_IsViewCreated && m_IsStarted && m_IsViewShown)
        {
            //Toast.makeText(getActivity(), "Hint=> SUBJECTS", Toast.LENGTH_SHORT).show();
            updateOptionsMenu();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        m_IsViewCreated = true;
       // Toast.makeText(getActivity(), "Create=> SUBJECTS", Toast.LENGTH_SHORT).show();
        if(m_IsViewShown)
        {
            updateOptionsMenu();
        }
    }

    private void updateOptionsMenu()
    {
        m_SearchBar = getActivity().findViewById(R.id.search_bar_of_home_screen);
        m_SearchBar.inflateMenu(R.menu.options_of_fragment_subjects);
        m_SearchBar.setMenuIcon(R.drawable.ic_sort);
        m_SearchBar.getSearchEditText().setVisibility(View.VISIBLE);
        m_SearchBar.getMenu().setOnMenuItemClickListener(this);
        m_SearchBar.setSearchIcon(R.drawable.ic_search);
        m_SearchBar.setMaxSuggestionCount(5);
        m_SearchBar.addTextChangeListener(this);
        m_SearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener()
        {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                m_SearchBar.hideSuggestionsList();
            }

            @Override
            public void onSearchConfirmed(CharSequence i_Text) {
                search(i_Text);
            }

            @Override
            public void onButtonClicked(int i_Button) {
                switch (i_Button) {
                    case MaterialSearchBar.BUTTON_NAVIGATION:
                        onNavIconClicked();
                        break;
                    case MaterialSearchBar.BUTTON_BACK:
                        onBackClicked();
                        break;
                }

                m_SuggestionsList.clear();
            }
        });

        m_SuggestionsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater i_Inflater, ViewGroup i_Container,
                             Bundle i_SavedInstanceState)
    {
//        if(!m_IsViewShown) {
//            Toast.makeText(getActivity(), "View=> SUBJECTS", Toast.LENGTH_SHORT).show();
//            updateOptionsMenu();
//        }

//        Toast.makeText(getActivity(), "Create=> SUBJECTS", Toast.LENGTH_SHORT).show();
//        updateOptionsMenu();
//        m_IsViewCreated = true;

        mSubjectsView = i_Inflater.inflate(R.layout.fragment_subjects, i_Container, false);
        mSubjectsList = mSubjectsView.findViewById(R.id.recycler_subjects_list_of_fragment_subjects);
        mSubjectsList.setLayoutManager(new LinearLayoutManager(getContext()));

        mDatabase = FirebaseDatabase.getInstance();
        mSubjectRef = mDatabase.getReference().child("Subjects");

        m_DrawerLayout = getActivity().findViewById(R.id.drawer_layout_home_screen);
        m_NavigationView = getActivity().findViewById(R.id.nav_view_home_screen);

        buildSubjectsOptionsByContext(POPULARITY, null);
        populateSubjectsView();

        return mSubjectsView;
    }

    private void search(CharSequence i_Text)
    {
        if (i_Text.toString().isEmpty())
        {
            Toast.makeText(getContext(),
                    "Please enter your choice", Toast.LENGTH_SHORT).show();
        }
        else if (!m_SuggestionsList.contains(i_Text.toString().toUpperCase())) {
            Toast.makeText(getContext(),
                    "No dishes found, try another category", Toast.LENGTH_SHORT).show();
        }
        else {

            buildSubjectsOptionsByContext(ALPHABETICALLY, i_Text.toString().toUpperCase());
        }

        populateSubjectsView();
    }

    private void onNavIconClicked()
    {
        m_DrawerLayout.openDrawer(Gravity.LEFT);
    }

    private void onBackClicked()
    {
        if(m_SearchBar.getMenu().getMenu().getItem(2).isChecked())
        {
            buildSubjectsOptionsByContext(ALPHABETICALLY, null);
        }
        else
        {
            buildSubjectsOptionsByContext(POPULARITY, null);
        }

        populateSubjectsView();
        m_SearchBar.hideSuggestionsList();
        m_SearchBar.disableSearch();
    }

    @Override
    public boolean onMenuItemClick(MenuItem i_Item)
    {
        switch (i_Item.getItemId())
        {
            case R.id.sort_by_a_z:
                if(i_Item.isChecked())
                {
                    i_Item.setChecked(false);
                }
                else{
                    i_Item.setChecked(true);
                    m_SearchBar.getMenu().getMenu().getItem(1).setChecked(false);
                    buildSubjectsOptionsByContext(ALPHABETICALLY, null);
                    populateSubjectsView();
                }
                break;
            case R.id.sort_by_popularity:
                if(i_Item.isChecked())
                {
                    i_Item.setChecked(false);
                }
                else{
                    i_Item.setChecked(true);
                    m_SearchBar.getMenu().getMenu().getItem(2).setChecked(false);
                    buildSubjectsOptionsByContext(POPULARITY, null);
                    populateSubjectsView();

                }
                break;
        }

        return false;
    }

    private void buildSubjectsOptionsByContext(String i_Context, String i_Key)
    {
        if(i_Key == null)
        {
            mOptions = new FirebaseRecyclerOptions
                    .Builder<Subject>()
                    .setQuery(mSubjectRef.orderByChild(i_Context), Subject.class)
                    .build();
        }
        else{
            mOptions = new FirebaseRecyclerOptions
                    .Builder<Subject>()
                    .setQuery(mSubjectRef.orderByChild(i_Context).equalTo(i_Key), Subject.class)
                    .build();
        }
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
                i_ViewHolder.getAvgRating().setVisibility(View.INVISIBLE);
                i_ViewHolder.getStarIcon().setVisibility(View.INVISIBLE);
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

    @Override
    public void onTextChanged(CharSequence i_Text, int i_Start, int i_Before, int i_Count)
    {
        if(m_SearchBar.isSearchEnabled())
        {


            if (m_SuggestionsList.isEmpty())
            {
                setSuggestions();
            }

            List<String> suggestions = new ArrayList<>();
            for (String searchOption : m_SuggestionsList)
            {
                if (!m_SearchBar.getText().isEmpty() && !m_SearchBar.getText().startsWith(" ") &&
                        searchOption.toLowerCase().contains(m_SearchBar.getText().toLowerCase()) &&
                        !suggestions.contains(searchOption.toLowerCase()))
                {
                    suggestions.add(searchOption.toLowerCase());
                } else {
                    m_SearchBar.hideSuggestionsList();
                }
            }

            m_SearchBar.clearSuggestions();
            m_SearchBar.setLastSuggestions(suggestions);
            if (!m_SearchBar.getText().isEmpty())
            {
                m_SearchBar.showSuggestionsList();
            }
        } else {
            m_SearchBar.hideSuggestionsList();
        }
    }

    private void setSuggestions()
    {
        mSubjectRef.orderByChild("subjectName")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                    {
                        for (DataSnapshot snapshot : i_DataSnapshot.getChildren()) {
                            Subject subject = snapshot.getValue(Subject.class);
                            m_SuggestionsList.add(Objects.requireNonNull(subject).getSubjectName());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError i_DatabaseError) { }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence i_Text, int i_Start, int i_Count, int i_After) {m_SearchBar.hideSuggestionsList(); }

    @Override
    public void afterTextChanged(Editable i_Text) { }

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
