package com.studymobile.advisos.ViewHolders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.studymobile.advisos.Interfaces.ItemClickListener;
import com.studymobile.advisos.R;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderCheckableSubj extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private ImageView m_SubjectImgView;
    private TextView m_SubjectName;
    private CheckBox m_Checkbox;
    private ItemClickListener m_ItemClickListener;

    public ViewHolderCheckableSubj(View i_ItemView)
    {
        super(i_ItemView);
        m_SubjectImgView = i_ItemView.findViewById(R.id.img_subject_of_item_subject_with_checkbox);
        m_SubjectName = i_ItemView.findViewById(R.id.txt_subject_name_of_item_subject_with_checkbox);
        m_Checkbox = i_ItemView.findViewById(R.id.checkBox_of_item_subject_with_checkbox);
        i_ItemView.setOnClickListener(this);
    }

    public ImageView getSubgectImage()
    {
        return m_SubjectImgView;
    }

    public void setDishName(String i_SubjectName)
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
