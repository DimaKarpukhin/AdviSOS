package com.studymobile.advisos.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.studymobile.advisos.Activities.ActivityChatRoom;
import com.studymobile.advisos.Interfaces.ItemClickListener;
import com.studymobile.advisos.Models.ChatRoom;
import com.studymobile.advisos.R;
import com.studymobile.advisos.ViewHolders.ViewHolderAdviceGroup;

import java.util.List;

public class AdapterPossibleActiveChats extends RecyclerView.Adapter {

    private List<ChatRoom> mChatRooms;
    private Context mContext;

    public AdapterPossibleActiveChats(@NonNull List<ChatRoom> i_ChatRooms, Context i_context){
        Log.e("Adapter Active Chats", "Creating Object");
        mChatRooms = i_ChatRooms;
        mContext = i_context;
    }

    @Override
    public int getItemCount() {
        return mChatRooms.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_advice_group,parent,false);
        return new ViewHolderAdviceGroup(view);
    }
    public void clearRecyclerView() {
        int size = mChatRooms.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                mChatRooms.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder i_ViewHolder, int position) {

        ChatRoom i_ActiveChatRoom = mChatRooms.get(position);
        ViewHolderAdviceGroup holder = (ViewHolderAdviceGroup)i_ViewHolder;
        Picasso.get().load(i_ActiveChatRoom.getImgLink()).into(holder.getGroupProfileImg());
        holder.setParentSubjectName(i_ActiveChatRoom.getSubjectName());
        holder.setGroupTopic(i_ActiveChatRoom.getRoomName());
        holder.setLastMessageTime(i_ActiveChatRoom.getCreationTime());
        holder.setLastMessageDate(i_ActiveChatRoom.getCreationDate());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent IntentChatRoom = new Intent(mContext, ActivityChatRoom.class);
                IntentChatRoom.putExtra("chat_room_id", i_ActiveChatRoom.getRoomId());
                IntentChatRoom.putExtra("room_name", i_ActiveChatRoom.getRoomName());
                IntentChatRoom.putExtra("subject_name", i_ActiveChatRoom.getSubjectName());
                IntentChatRoom.putExtra("subject_image", i_ActiveChatRoom.getImgLink());
                IntentChatRoom.putExtra("type", "observer");
                mContext.startActivity(IntentChatRoom);
            }
        });
    }
}
