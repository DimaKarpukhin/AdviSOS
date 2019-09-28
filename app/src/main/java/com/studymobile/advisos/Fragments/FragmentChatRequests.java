package com.studymobile.advisos.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.studymobile.advisos.Activities.ActivitySubjectActionManager;
import com.studymobile.advisos.Activities.ActivityUserDetails;
import com.studymobile.advisos.Models.ActiveChatRoom;
import com.studymobile.advisos.Models.ChatRequest;
import com.studymobile.advisos.Models.ChatRoom;
import com.studymobile.advisos.Models.Rating;
import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.Models.SubjectUser;
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderChatRequest;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentChatRequests extends Fragment
        implements PopupMenu.OnMenuItemClickListener
{
    private static final String SUBJECT_NAME = "chatRoomName";
    private static final String CREATOR_NAME = "chatCreatorName";

    private View mChatRequestsView;
    private RecyclerView mChatRequestsList;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserRequestsRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private FirebaseRecyclerOptions<ChatRequest> mOptions;
    private FirebaseRecyclerAdapter<ChatRequest, ViewHolderChatRequest> mAdapter;

    private Dialog m_PopupDialog;
    private Uri m_DialogImgURI;
    private CircleImageView m_DialogImgView;

    private MaterialSearchBar m_SearchBar;
    private List<String> m_SuggestionsList;

    private NavigationView m_NavigationView;
    private DrawerLayout m_DrawerLayout;

    private boolean m_IsViewCreated = false;
    private boolean m_IsViewShown = false;
    private Boolean m_IsStarted = false;

    public FragmentChatRequests() {
        // Required empty public constructor
    }

    @Override
    public void onStart()
    {
        super.onStart();
        //Toast.makeText(getActivity(), "Start=> REQUESTS", Toast.LENGTH_SHORT).show();
        m_IsStarted = true;
//        if (m_IsViewShown){
//           // updateOptionsMenu();
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
        //Toast.makeText(getActivity(), "Resume=> REQUESTS", Toast.LENGTH_SHORT).show();
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
//            Toast.makeText(getActivity(), "Hint=> REQUESTS", Toast.LENGTH_SHORT).show();
//            updateOptionsMenu();
//        } else {
//            m_IsViewShown = false;
//        }
        m_IsViewShown = i_IsVisible;
        if (m_IsViewCreated && m_IsStarted && m_IsViewShown)
        {
            //Toast.makeText(getActivity(), "Hint=> REQUESTS", Toast.LENGTH_SHORT).show();
            updateOptionsMenu();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        m_IsViewCreated = true;
       //// Toast.makeText(getActivity(), "Create=> REQUESTS", Toast.LENGTH_SHORT).show();
        if(m_IsViewShown)
        {
            updateOptionsMenu();
        }
    }

    private void updateOptionsMenu()
    {
        m_SearchBar = getActivity().findViewById(R.id.search_bar_of_home_screen);
        m_SearchBar.inflateMenu(R.menu.options_of_fragment_chat_requests);
        m_SearchBar.setMenuIcon(R.drawable.ic_sort);
        m_SearchBar.getMenu().setOnMenuItemClickListener(this);
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
            public void onButtonClicked(int i_Button)
            {
                switch (i_Button) {
                    case MaterialSearchBar.BUTTON_NAVIGATION:
                        onNavIconClicked();
                        break;
                    case MaterialSearchBar.BUTTON_BACK:
                        onBackClicked();
                        break;
                }
            }
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
        m_SearchBar.disableSearch();
    }

    @Override
    public View onCreateView(LayoutInflater i_Inflater, ViewGroup i_Container,
                             Bundle i_SavedInstanceState)
    {
//        if(!m_IsViewShown) {
//            Toast.makeText(getActivity(), "View=> REQUESTS", Toast.LENGTH_SHORT).show();
//            updateOptionsMenu();
//        }
//        Toast.makeText(getActivity(), "Create=> REQUESTS", Toast.LENGTH_SHORT).show();
//        updateOptionsMenu();
//        m_IsViewCreated = true;

        mChatRequestsView = i_Inflater.inflate(R.layout.fragment_chat_requests, i_Container, false);
        mChatRequestsList = mChatRequestsView.findViewById(R.id.recycler_chat_requests);
        mChatRequestsList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mUserRequestsRef = mDatabase.getReference("ChatRequests")
                .child(mCurrentUser.getUid());

        m_DrawerLayout = getActivity().findViewById(R.id.drawer_layout_home_screen);
        m_NavigationView = getActivity().findViewById(R.id.nav_view_home_screen);

        buildChatRequestsOptionsByContext(CREATOR_NAME, null);
        populateChatRequestsView();

        return mChatRequestsView;
    }

    private void buildChatRequestsOptionsByContext(String i_Context, String i_Key)
    {
        if(i_Key == null)
        {
            mOptions = new FirebaseRecyclerOptions
                    .Builder<ChatRequest>()
                    .setQuery(mUserRequestsRef.orderByChild(i_Context), ChatRequest.class)
                    .build();
        }
        else{
            mOptions = new FirebaseRecyclerOptions
                    .Builder<ChatRequest>()
                    .setQuery(mUserRequestsRef.orderByChild(i_Context).equalTo(i_Key), ChatRequest.class)
                    .build();
        }
    }

    private void populateChatRequestsView()
    {
        mAdapter = new FirebaseRecyclerAdapter<ChatRequest, ViewHolderChatRequest>(mOptions) {
            @NonNull
            @Override
            public ViewHolderChatRequest onCreateViewHolder(
                    @NonNull ViewGroup i_ViewGroup, int i_Position)
            {
                View view = LayoutInflater
                        .from(i_ViewGroup.getContext())
                        .inflate(R.layout.item_chat_request, i_ViewGroup, false);

                return new ViewHolderChatRequest(view);
            }

            @Override
            protected void onBindViewHolder(
                    @NonNull ViewHolderChatRequest i_ViewHolder, int i_Position,
                    @NonNull final ChatRequest i_ChatRequest)
            {
                Picasso.get().load(i_ChatRequest.getChatCreatorImgLink()).into(i_ViewHolder.getChatCreatorImg());
                i_ViewHolder.setChatCreatorName(i_ChatRequest.getChatCreatorName());
                i_ViewHolder.setSubjectName(i_ChatRequest.getChatRoomName());
                i_ViewHolder.setTopic(i_ChatRequest.getTopic());

                i_ViewHolder.getChatCreatorImg().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View i_View) {
                        showImageDialog(i_ChatRequest.getChatCreatorImgLink(),
                                i_ChatRequest.getChatCreatorName());
                    }
                });

                i_ViewHolder.getBtnAccept().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View i_View)
                    {

                        addUserToActiveChats(i_ChatRequest.getChatRoomId());
                        removeRequestFromDB(i_ChatRequest.getRequestId());
                        Intent intent = new Intent(getContext(), ActivityChatRoom.class);
                        intent.putExtra("chat_room_id", i_ChatRequest.getChatRoomId());
                        intent.putExtra("room_name",i_ChatRequest.getTopic());
                        intent.putExtra("subject_name", i_ChatRequest.getChatRoomName());
                        intent.putExtra("subject_image",i_ChatRequest.getSubjectImgLink());
                        startActivity(intent);
                    }
                });

                i_ViewHolder.getBtnReject().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View i_View) {
                        showConfirmDialog("By rejecting a request you will lose 1% of your rating",
                                "Cancel", "I agree", i_ChatRequest);
                    }
                });
            }
        };

        mAdapter.startListening();
        mChatRequestsList.setAdapter(mAdapter);
    }

    private void showConfirmDialog(String i_Title, String i_LeftBtnTxt, String i_RightBtnTxt,
                                   ChatRequest i_ChatRequest)
    {
        final Dialog confirmDialog = new Dialog(getContext());
        confirmDialog.setContentView(R.layout.dialog_confirm);

        ImageButton closeBtn = confirmDialog.findViewById(R.id.btn_close_of_dialog_confirm);
        TextView fieldTitle = confirmDialog.findViewById(R.id.txt_title_of_dialog_confirm);
        TextView rightBtn = confirmDialog.findViewById(R.id.btn_right_of_dialog_confirm);
        TextView leftBtn = confirmDialog.findViewById(R.id.btn_left_of_dialog_confirm);

        closeBtn.setVisibility(View.VISIBLE);
        fieldTitle.setText(i_Title);
        rightBtn.setText(i_RightBtnTxt);
        leftBtn.setText(i_LeftBtnTxt);

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downgradeUser(i_ChatRequest.getRequestedUserId(),
                        i_ChatRequest.getChatRoomId(), 1);

                removeRequestFromDB(i_ChatRequest.getRequestId());
                confirmDialog.dismiss();
            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });

        confirmDialog.show();
    }

    private void downgradeUser(String i_UserId, String i_ChatRoomId, int i_PercentsDelta)
    {
        mDatabase.getReference("ChatRooms").child(i_ChatRoomId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot) {
                        if(i_DataSnapshot.exists())
                        {
                            String subjectName = i_DataSnapshot.child("subjectName")
                                    .getValue(String.class);

                            mDatabase.getReference("SubjectUsers").child(subjectName).child(i_UserId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot i_DataSnapshot)
                                {
                                    if(i_DataSnapshot.exists())
                                    {
                                        SubjectUser user = i_DataSnapshot.getValue(SubjectUser.class);
                                        Rating userRating = i_DataSnapshot.child("rating").getValue(Rating.class);
                                        user.setRating(userRating);
                                        user.ChangeRating(i_PercentsDelta, false);

                                        mDatabase.getReference("SubjectUsers").child(subjectName)
                                                .child(i_UserId).child("rating").setValue(user.getRating());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError i_DataSnapshot) {

                                }
                            });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError i_DataSnapshot) { }
                });

    }


    private void addUserToActiveChats(String i_ChatRoomId)
    {
        String userID = mCurrentUser.getUid();

        mDatabase.getReference("Participants")
                .child(i_ChatRoomId).child(userID).setValue(userID);

        mDatabase.getReference("ChatRooms").child(i_ChatRoomId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot i_DataSnapshot) {
                        if(i_DataSnapshot.exists())
                        {
                            ChatRoom chatRoom = i_DataSnapshot.getValue(ChatRoom.class);
                            chatRoom.setNumOfParticipants(chatRoom.getNumOfParticipants() + 1);

                            mDatabase.getReference("ChatRooms").child(i_ChatRoomId).setValue(chatRoom);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError i_DatabaseError) {

                    }
                });

        ActiveChatRoom activeChatRoom = new ActiveChatRoom
                (i_ChatRoomId, userID,false);
        mDatabase.getReference("ActiveChats").child(userID)
                .child(i_ChatRoomId).setValue(activeChatRoom);
    }

    private void removeRequestFromDB(String i_RequestId)
    {
        mDatabase.getReference("ChatRequests")
                .child(mCurrentUser.getUid()).child(i_RequestId).removeValue();
    }

    private void showImageDialog(String i_ImgLink, String i_Title)
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

    @Override
    public boolean onMenuItemClick(MenuItem i_Item)
    {
        switch (i_Item.getItemId())
        {
            case R.id.sort_by_user_request:
                if(i_Item.isChecked())
                {
                    i_Item.setChecked(false);
                }
                else{
                    i_Item.setChecked(true);
                    m_SearchBar.getMenu().getMenu().getItem(2).setChecked(false);
                    buildChatRequestsOptionsByContext(CREATOR_NAME, null);
                    populateChatRequestsView();
                }
                break;
            case R.id.sort_by_subject_request:
                if(i_Item.isChecked())
                {
                    i_Item.setChecked(false);
                }
                else{
                    i_Item.setChecked(true);

                    m_SearchBar.getMenu().getMenu().getItem(1).setChecked(false);
                    buildChatRequestsOptionsByContext(SUBJECT_NAME, null);
                    populateChatRequestsView();

                }
                break;
        }

        return false;
    }
}
