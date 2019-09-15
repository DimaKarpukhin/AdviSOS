package com.studymobile.advisos.ViewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.studymobile.advisos.R;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolderRatedUser extends RecyclerView.ViewHolder {

    private CircleImageView m_UserImgView;
    private TextView m_UserName;
    private RatingBar m_RatingBar;
    private ImageView m_IconChecked;
    private Button m_BtnOk;


    public ViewHolderRatedUser(View i_ItemView)
    {
        super(i_ItemView);
        m_UserImgView = i_ItemView.findViewById(R.id.img_user_of_item_rated_user);
        m_UserName = i_ItemView.findViewById(R.id.txt_user_name_of_item_rated_user);
        m_RatingBar = i_ItemView.findViewById(R.id.rating_bar_of_item_rated_user);
        m_IconChecked = i_ItemView.findViewById(R.id.img_checked_of_item_rated_user);
        m_BtnOk = i_ItemView.findViewById(R.id.btn_ok_of_item_rated_user);

    }


    public CircleImageView getUserImageView()
    {
        return m_UserImgView;
    }

    public RatingBar getRatingBar()
    {
        return m_RatingBar;
    }

    public ImageView getIconChecked()
    {
        return m_IconChecked;
    }

    public Button getButtonOk()
    {
        return m_BtnOk;
    }

    public void setUserName(String i_UserName)
    {
        m_UserName.setText(i_UserName);
    }
}
