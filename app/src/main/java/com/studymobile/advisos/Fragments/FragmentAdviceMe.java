package com.studymobile.advisos.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.studymobile.advisos.Activities.ActivityChatRoom;
import com.studymobile.advisos.Interfaces.ItemClickListener;
import com.studymobile.advisos.Models.ActiveChatRoom;
import com.studymobile.advisos.Models.ChatRoom;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderAdviceGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAdviceMe extends Fragment
{
    private View mAdviceMeView;
    private RecyclerView mAdviceMeList;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mActiveChat;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private FirebaseRecyclerOptions<ActiveChatRoom> mOptions;
    private FirebaseRecyclerAdapter<ActiveChatRoom, ViewHolderAdviceGroup> mAdapter;

    private MaterialSearchBar m_SearchBar;
    private NavigationView m_NavigationView;
    private DrawerLayout m_DrawerLayout;

    private boolean m_IsViewCreated = false;
    private boolean m_IsViewShown = false;
    private Boolean m_IsStarted = false;

    public FragmentAdviceMe() {
        // Required empty public constructor
    }

    @Override
    public void onStart()
    {
        super.onStart();
        //Toast.makeText(getActivity(), "Start=> ME", Toast.LENGTH_SHORT).show();
        m_IsStarted = true;
//        if (m_IsViewShown){
//            //updateOptionsMenu();
//        }
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
        //Toast.makeText(getActivity(), "Resume=> ME", Toast.LENGTH_SHORT).show();
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
//            Toast.makeText(getActivity(), "Hint=> ME", Toast.LENGTH_SHORT).show();
//            updateOptionsMenu();
//        } else {
//            m_IsViewShown = false;
//        }
        m_IsViewShown = i_IsVisible;
        if (m_IsViewCreated && m_IsStarted && m_IsViewShown)
        {
//            Toast.makeText(getActivity(), "Hint=> ME", Toast.LENGTH_SHORT).show();
            updateOptionsMenu();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        m_IsViewCreated = true;
        //Toast.makeText(getActivity(), "Create=> ME", Toast.LENGTH_SHORT).show();
        if(m_IsViewShown)
        {
            updateOptionsMenu();
        }
    }

    private void updateOptionsMenu()
    {
        m_SearchBar = getActivity().findViewById(R.id.search_bar_of_home_screen);
        m_SearchBar.setMenuIcon(0);
        m_SearchBar.getMenu().getMenu().clear();
        m_SearchBar.setSearchIcon(0);
        m_SearchBar.getSearchEditText().setVisibility(View.GONE);
        m_SearchBar.setClearIcon(0);
        m_SearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener()
        {
            @Override
            public void onSearchStateChanged(boolean enabled) {}

            @Override
            public void onSearchConfirmed(CharSequence i_Text) {}

            @Override
            public void onButtonClicked(int i_Button) { switch (i_Button)
            {
                case MaterialSearchBar.BUTTON_NAVIGATION:
                    onNavIconClicked();
                    break;
                case MaterialSearchBar.BUTTON_BACK:
                    onBackClicked();
                    break;
            } }
        });
    }

    private void onNavIconClicked()
    {
        m_DrawerLayout.openDrawer(Gravity.LEFT);
    }

    private void onBackClicked()
    {
//        if(m_SearchBar.getMenu().getMenu().getItem(2).isChecked())
//        {
//            buildSubjectsOptionsByContext(ALPHABETICALLY, null);
//        }
//        else
//        {
//            buildSubjectsOptionsByContext(POPULARITY, null);
//        }
//
//        populateSubjectsView();
//        m_SearchBar.hideSuggestionsList();
//        m_SearchBar.disableSearch();
    }

    @Override
    public View onCreateView(LayoutInflater i_Inflater, ViewGroup i_Container,
                             Bundle i_SavedInstanceState)

    {
//        if(!m_IsViewShown) {
//            Toast.makeText(getActivity(), "View=> ME", Toast.LENGTH_SHORT).show();
//            updateOptionsMenu();
//        }
//
//        Toast.makeText(getActivity(), "Create=> ME", Toast.LENGTH_SHORT).show();
//        updateOptionsMenu();
//        m_IsViewCreated = true;

       // Toast.makeText(getActivity(), "View=> ME", Toast.LENGTH_SHORT).show();
        mAdviceMeView = i_Inflater.inflate(R.layout.fragment_advice_me, i_Container, false);
        mAdviceMeList = mAdviceMeView.findViewById(R.id.recycler_subjects_list_of_fragment_advice_me);
        mAdviceMeList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mActiveChat = mDatabase.getReference().child("ActiveChats");

        m_DrawerLayout = getActivity().findViewById(R.id.drawer_layout_home_screen);
        m_NavigationView = getActivity().findViewById(R.id.nav_view_home_screen);

        buildAdviceMeOptions();
        populateAdviceMeView();

        return mAdviceMeView;
    }

    private void buildAdviceMeOptions()
    {
        mOptions = new FirebaseRecyclerOptions
                .Builder<ActiveChatRoom>()
                .setQuery(mActiveChat.child(mCurrentUser.getUid())
                        .orderByChild("isCreator").equalTo(true), ActiveChatRoom.class)
                .build();
    }

    private void populateAdviceMeView()
    {
        mAdapter = new FirebaseRecyclerAdapter<ActiveChatRoom, ViewHolderAdviceGroup>(mOptions) {
            @NonNull
            @Override
            public ViewHolderAdviceGroup onCreateViewHolder
                    (@NonNull ViewGroup i_ViewGroup, int i_Position)
            {
                View view = LayoutInflater
                        .from(i_ViewGroup.getContext())
                        .inflate(R.layout.item_advice_group, i_ViewGroup, false);

                return new ViewHolderAdviceGroup(view);
            }

            @Override
            protected void onBindViewHolder
                    (@NonNull final ViewHolderAdviceGroup i_ViewHolder,
                     final int i_Position, @NonNull final ActiveChatRoom i_ActiveChatRoom)
            {
                DatabaseReference chatRoomRef = mDatabase.getReference("ChatRooms");
                chatRoomRef.child(i_ActiveChatRoom.getChatRoomId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                            {
                                final ChatRoom chatRoom = i_DataSnapshot.getValue(ChatRoom.class);
                                Picasso.get().load(chatRoom.getImgLink()).into(i_ViewHolder.getGroupProfileImg());
                                i_ViewHolder.setParentSubjectName(chatRoom.getSubjectName());
                                i_ViewHolder.setGroupTopic(chatRoom.getRoomName());
                                i_ViewHolder.setLastMessageTime(chatRoom.getCreationTime());
                                i_ViewHolder.setLastMessageDate(chatRoom.getCreationDate());

                                i_ViewHolder.setItemClickListener(new ItemClickListener() {
                                    @Override
                                    public void onClick(View view, int position, boolean isLongClick)
                                    {
                                        Intent IntentChatRoom = new Intent(getContext(), ActivityChatRoom.class);
                                        IntentChatRoom.putExtra("chat_room_id", chatRoom.getRoomId());
                                        IntentChatRoom.putExtra("room_name",chatRoom.getRoomName());
                                        IntentChatRoom.putExtra("subject_name",chatRoom.getSubjectName() );
                                        IntentChatRoom.putExtra("subject_image",chatRoom.getImgLink());
                                        startActivity(IntentChatRoom);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError i_DataSnapshot) {

                            }
                        });

            }
        };

        mAdapter.startListening();
        mAdviceMeList.setAdapter(mAdapter);
    }
}
