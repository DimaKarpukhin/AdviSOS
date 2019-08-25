package com.studymobile.advisos.Models;

import java.util.LinkedList;
import java.util.List;

public class Subject
{
    private String mName, mDescription, mImgLink;
    private List<SubjectUser> mSubjectUsers = new LinkedList<SubjectUser>();

    public Subject(String i_Name, String i_Description, String i_ImgLink, List<SubjectUser> i_SubjectUsers)
    {
        this.mName = i_Name;
        this.mDescription = i_Description;
        this.mImgLink = i_ImgLink;
        this.mSubjectUsers = i_SubjectUsers;
    }

    public String GetName()
    {
        return mName;
    }

    public String GetDescription()
    {
        return mDescription;
    }

    public String GetImgLink()
    {
        return mImgLink;
    }

    public List<SubjectUser> GetSubjectUsers()
    {
        return mSubjectUsers;
    }

    public void SetName(String i_Name)
    {
        this.mName = i_Name;
    }

    public void SetDescription(String i_Description)
    {
        this.mDescription = i_Description;
    }

    public void SetImgLink(String i_ImgLink)
    {
        this.mImgLink = i_ImgLink;
    }

    public void SetSubjectUsers(List<SubjectUser> i_SubjectUsers)
    {
        this.mSubjectUsers = i_SubjectUsers;
    }
}
