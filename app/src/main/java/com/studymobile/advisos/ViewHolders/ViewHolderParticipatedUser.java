package com.studymobile.advisos.ViewHolders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.studymobile.advisos.Interfaces.ItemClickListener;
import com.studymobile.advisos.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolderParticipatedUser extends RecyclerView.ViewHolder {

    private CircleImageView m_UserImgView;
    private TextView m_UserName;
    private TextView m_UserRate;


    public ViewHolderParticipatedUser(View i_ItemView)
    {
        super(i_ItemView);
        m_UserImgView = i_ItemView.findViewById(R.id.img_participated_user);
        m_UserName = i_ItemView.findViewById(R.id.txt_user_name);
        m_UserRate = i_ItemView.findViewById(R.id.txt_rate_mark_of_user);

    }


    public CircleImageView getUserImage()
    {
        return m_UserImgView;
    }
    public void setUserName(String i_UserName)
    {
        m_UserName.setText(i_UserName);
    }
    public void setUserRate(float i_UserRate)
    {
        if(i_UserRate == -1)
        {
            m_UserRate.setText("--");
        }
        else {
            String fullNumber = String.valueOf(i_UserRate);
            m_UserRate.setText(fullNumber.substring(0, fullNumber.indexOf(".") + 2));
        }
    }
}
