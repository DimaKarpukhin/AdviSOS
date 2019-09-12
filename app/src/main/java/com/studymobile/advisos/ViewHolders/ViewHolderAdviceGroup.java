package com.studymobile.advisos.ViewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.studymobile.advisos.Interfaces.ItemClickListener;
import com.studymobile.advisos.R;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolderAdviceGroup extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private CircleImageView m_GroupProfileImg;
    private TextView m_ParentSubjectName, m_GroupTopic, m_LastMessageTime;
    private Button m_BtnUnreadMessages;
    private ItemClickListener m_ItemClickListener;

    public ViewHolderAdviceGroup(View i_ItemView)
    {
        super(i_ItemView);
        m_GroupProfileImg = i_ItemView.findViewById(R.id.img_subject_of_item_advice_group);
        m_ParentSubjectName = i_ItemView.findViewById(R.id.txt_parent_subject_of_item_advice_group);
        m_GroupTopic = i_ItemView.findViewById(R.id.txt_topic_of_item_advice_group);
        m_LastMessageTime = i_ItemView.findViewById(R.id.txt_time_of_item_advice_group);
        m_BtnUnreadMessages = i_ItemView.findViewById(R.id.btn_unread_of_item_advice_group);
        i_ItemView.setOnClickListener(this);
    }

    public Button getBtnUnreadMessages()
    {
        return m_BtnUnreadMessages;
    }

    public CircleImageView getGroupProfileImg()
    {
        return m_GroupProfileImg;
    }

    public void setParentSubjectName(String i_ParentSubjectName)
    {
        m_ParentSubjectName.setText(i_ParentSubjectName);
    }

    public void setGroupTopic(String i_GroupTopic)
    {
        m_GroupTopic.setText(i_GroupTopic);
    }

    public void setLastMessageTime(String i_LastMessageTime)
    {
        m_LastMessageTime.setText(i_LastMessageTime);
    }

    public void setItemClickListener(ItemClickListener i_ItemClickListener)
    {
        this.m_ItemClickListener = i_ItemClickListener;
    }

    @Override
    public void onClick(View i_View)
    {
        m_ItemClickListener.onClick(i_View, getAdapterPosition(), false);
    }
}
