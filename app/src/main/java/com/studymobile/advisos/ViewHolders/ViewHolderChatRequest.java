package com.studymobile.advisos.ViewHolders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.studymobile.advisos.Interfaces.ItemClickListener;
import com.studymobile.advisos.R;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolderChatRequest extends RecyclerView.ViewHolder
{
    private CircleImageView m_ImgViewChatCreator;
    private TextView m_TxtChatCreatorName;
    private TextView m_TxtSubjectName;
    private TextView m_TxtTopic;
    private TextView m_BtnAccept;
    private TextView m_BtnReject;


    public ViewHolderChatRequest(View i_ItemView)
    {
        super(i_ItemView);
        m_ImgViewChatCreator = i_ItemView.findViewById(R.id.img_chat_creator_of_item_request);
        m_TxtChatCreatorName = i_ItemView.findViewById(R.id.txt_chat_creator_name_of_item_request);
        m_TxtSubjectName = i_ItemView.findViewById(R.id.txt_subject_name_of_item_request);
        m_TxtTopic =  i_ItemView.findViewById(R.id.txt_topic_of_item_request);

        m_BtnAccept = i_ItemView.findViewById(R.id.btn_accept_of_item_request);
        m_BtnReject = i_ItemView.findViewById(R.id.btn_reject_of_item_request);
    }

    public TextView getBtnAccept()
    {
        return m_BtnAccept;
    }

    public TextView getBtnReject()
    {
        return m_BtnReject;
    }

    public CircleImageView getChatCreatorImg()
    {
        return m_ImgViewChatCreator;
    }

    public void setSubjectName(String i_SubjectName)
    {
        m_TxtSubjectName.setText(i_SubjectName);
    }

    public void setTopic(String i_Topic)
    {
        m_TxtTopic.setText(i_Topic);
    }

    public void setChatCreatorName(String i_CreatorName)
    {
        m_TxtChatCreatorName.setText(i_CreatorName);
    }

}
