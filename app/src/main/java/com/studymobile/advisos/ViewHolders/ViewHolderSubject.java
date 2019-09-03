package com.studymobile.advisos.ViewHolders;

import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.studymobile.advisos.Interfaces.ItemClickListener;
import com.studymobile.advisos.R;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolderSubject extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private CircleImageView m_SubjectImgView;
    private TextView m_SubjectName;
    private CheckBox m_Checkbox;
    private ImageView m_ArrowRightIcon;
    private ItemClickListener m_ItemClickListener;

    public ViewHolderSubject(View i_ItemView)
    {
        super(i_ItemView);
        m_ArrowRightIcon = i_ItemView.findViewById(R.id.img_arrow_right_of_item_subject);
        m_Checkbox = i_ItemView.findViewById(R.id.checkBox_of_item_subject);
        m_SubjectImgView = i_ItemView.findViewById(R.id.img_subject_of_item_subject);
        m_SubjectName = i_ItemView.findViewById(R.id.txt_subject_name_of_item_subject);
        i_ItemView.setOnClickListener(this);
    }

    public ImageView getArowwRightIcon()
    {
        return m_ArrowRightIcon;
    }

    public CheckBox getCheckBox()
    {
        return m_Checkbox;
    }

    public CircleImageView getSubjectImage()
    {
        return m_SubjectImgView;
    }

    public void setSubjectImage(String i_ImgLink)
    {
        m_SubjectImgView.setImageURI(Uri.parse(i_ImgLink));
    }


    public void setSubjectName(String i_SubjectName)
    {
        m_SubjectName.setText(i_SubjectName);
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
