package com.studymobile.advisos.Models;

import java.util.LinkedList;
import java.util.List;

public class Subject
{
    private boolean mIsChecked;//?
    private String mName, mDescription, mImgLink;
    private List<SubjectUser> mSubjectUsers = new LinkedList<SubjectUser>();

    public Subject(){}

    public Subject(String i_Name, String i_Description, String i_ImgLink, List<SubjectUser> i_SubjectUsers)
    {
        this.setName(i_Name);
        this.setDescription(i_Description);
        this.setImgLink(i_ImgLink);
        this.setSubjectUsers(i_SubjectUsers);
    }

    public boolean getChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean i_Checked) {
        mIsChecked = i_Checked;
    }

    public String getName()
    {
        return mName;
    }

    public String getDescription()
    {
        return mDescription;
    }

    public String getImgLink()
    {
        return mImgLink;
    }

    public List<SubjectUser> getSubjectUsers()
    {
        return mSubjectUsers;
    }

    public void setName(String i_Name)
    {
        this.mName = i_Name;
    }

    public void setDescription(String i_Description)
    {
        this.mDescription = i_Description;
    }

    public void setImgLink(String i_ImgLink)
    {
        this.mImgLink = i_ImgLink;
    }

    public void setSubjectUsers(List<SubjectUser> i_SubjectUsers)
    {
        this.mSubjectUsers = i_SubjectUsers;
    }
}
