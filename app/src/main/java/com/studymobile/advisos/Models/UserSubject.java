package com.studymobile.advisos.Models;

public class UserSubject
{
    private String mSubjectName;
    private String mUserName;
    private String mSubjectImgLink;
    private String mUserId;

    public UserSubject(){}

    public UserSubject(String i_UserId, String i_UserName, String i_SubjectImgLink, String i_SubjectName)
    {
        this.setUserId(i_UserId);
        this.setSubjectName(i_SubjectName);
        this.setUserName(i_UserName);
        this.setSubjectImgLink(i_SubjectImgLink);
    }

    public String getUserName()
    {
        return mUserName;
    }

    public String getSubjectImgLinkk()
    {
        return mSubjectImgLink;
    }


    public String getUserId()
    {
        return mUserId;
    }

    public String getSubjectName()
    {
        return mSubjectName;
    }


    public void setUserName(String i_UserName)
    {
        this.mUserName = i_UserName;
    }

    public void setSubjectImgLink(String i_SubjectImgLink)
    {
        this.mSubjectImgLink = i_SubjectImgLink;
    }

    public void setUserId(String i_UserId)
    {
        this.mUserId = i_UserId;
    }

    public void setSubjectName(String i_SubjectName)
    {
        this.mSubjectName = i_SubjectName;
    }

}
