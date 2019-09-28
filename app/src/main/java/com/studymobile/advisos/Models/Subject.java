package com.studymobile.advisos.Models;

import java.util.LinkedList;
import java.util.List;

public class Subject
{
    private String mSubjectName, mSubjectDescription, mImgLink;
    private List<SubjectUser> mSubjectUsers = new LinkedList<SubjectUser>();
    private int mPopularity;

    public Subject(){}

    public Subject(String i_SubjectName, String i_SubjectDescription,
                   String i_ImgLink, List<SubjectUser> i_SubjectUsers)
    {
        this.setSubjectName(i_SubjectName);
        this.setSubjectDescription(i_SubjectDescription);
        this.setImgLink(i_ImgLink);
        this.setSubjectUsers(i_SubjectUsers);
    }

    public int getPopularity()
    {
        return mPopularity;
    }

    public void setPopularity(int i_Popularity)
    {
        mPopularity = i_Popularity;
    }

    public String getSubjectName()
    {
        return mSubjectName;
    }

    public String getSubjectDescription()
    {
        return mSubjectDescription;
    }

    public String getImgLink()
    {
        return mImgLink;
    }

    public List<SubjectUser> getSubjectUsers()
    {
        return mSubjectUsers;
    }

    public void setSubjectName(String i_SubjectName)
    {
        this.mSubjectName = i_SubjectName;
    }

    public void setSubjectDescription(String i_SubjectDescription)
    {
        this.mSubjectDescription = i_SubjectDescription;
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
