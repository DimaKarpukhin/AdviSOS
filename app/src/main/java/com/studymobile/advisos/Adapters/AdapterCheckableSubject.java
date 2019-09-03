package com.studymobile.advisos.Adapters;


import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.studymobile.advisos.Models.Subject;
import com.studymobile.advisos.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class AdapterCheckableSubject extends
        RecyclerView.Adapter<AdapterCheckableSubject.ViewHolder> {

    private List<Subject> m_SubjectList = new ArrayList<>();
    private SparseBooleanArray m_SubjListStates = new SparseBooleanArray();

    AdapterCheckableSubject() { }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup i_Parent, int i_ViewType)
    {
        Context context = i_Parent.getContext();
        int layoutForItem = R.layout.item_subject;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForItem, i_Parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder i_Holder, int i_Position)
    {
        i_Holder.bind(i_Position);
    }

    @Override
    public int getItemCount()
    {
        if (m_SubjectList == null) {
            return 0;
        }

        return m_SubjectList.size();
    }

    void loadItems(List<Subject> i_Tournaments)
    {
        this.m_SubjectList = i_Tournaments;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        CheckBox m_CheckBox;

        ViewHolder(View i_ItemView)
        {
            super(i_ItemView);
            m_CheckBox = i_ItemView.findViewById(R.id.checkBox_of_item_subject);
            i_ItemView.setOnClickListener(this);
        }

        void bind(int i_Position)
        {
            // use the sparse boolean array to check
            if (!m_SubjListStates.get(i_Position, false))
            {
                m_CheckBox.setChecked(false);
            }
            else {
                m_CheckBox.setChecked(true);
            }
        }

        @Override
        public void onClick(View i_View) {
            int adapterPosition = getAdapterPosition();

            if (!m_SubjListStates.get(adapterPosition, false))
            {
                m_CheckBox.setChecked(true);
                m_SubjListStates.put(adapterPosition, true);
            }
            else {
                m_CheckBox.setChecked(false);
                m_SubjListStates.put(adapterPosition, false);
            }
        }

    }
}

