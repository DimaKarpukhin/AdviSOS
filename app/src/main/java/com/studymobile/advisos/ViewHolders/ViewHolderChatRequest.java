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
    private CircleImageView m_ImgViewUser;
    private TextView m_TxtSubjectName;
    private TextView m_TxtUserName;


    public ViewHolderChatRequest(View i_ItemView)
    {
        super(i_ItemView);
        m_ImgViewUser = i_ItemView.findViewById(R.id.img_user_of_item_request);
        m_TxtUserName = i_ItemView.findViewById(R.id.txt_user_name_of_item_request);
        m_TxtSubjectName = i_ItemView.findViewById(R.id.txt_subject_name_of_item_request);
    }

    public CircleImageView getUserProfileImg()
    {
        return m_ImgViewUser;
    }

    public void setSubjectName(String i_SubjectName)
    {
        m_TxtSubjectName.setText(i_SubjectName);
    }

    public void setUserName(String i_UserName)
    {
        m_TxtUserName.setText(i_UserName);
    }

}
