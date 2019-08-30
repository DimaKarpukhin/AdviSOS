package com.studymobile.advisos.Models;

import java.util.ArrayList;

public class ChatRoom
{
    private int mRoomId;
    private String mChatRoomCreator;
    private String mRoomName;
    private String mCreationDate;
    private String mCreationTime;
    private String mSubjectName;
    private String mSubjectId;

    public ChatRoom(){}

    public ChatRoom(int i_RoomId, String i_RoomName, String i_CreationDate,
                    String i_CreationTime, String i_SubjectName,
                    String i_SubjectId, String i_creator)
    {
        this.setRoomId(i_RoomId);
        this.setRoomName(i_RoomName);
        this.setCreationDate(i_CreationDate);
        this.setCreationTime(i_CreationTime);
        this.setSubjectName(i_SubjectName);
        this.setSubjectId(i_SubjectId);
        this.mChatRoomCreator = i_creator;

    }

    public int getRoomId()
    {
        return mRoomId;
    }

    public String getRoomName()
    {
        return mRoomName;
    }

    public String getCreationDate()
    {
        return mCreationDate;
    }

    public String getCreationTime()
    {
        return mCreationTime;
    }

    public String getSubjectName()
    {
        return mSubjectName;
    }

    public String getSubjectId()
    {
        return mSubjectId;
    }

    public void setRoomId(int i_RoomId)
    {
        this.mRoomId = i_RoomId;
    }

    public void setRoomName(String i_RoomName)
    {
        this.mRoomName = i_RoomName;
    }

    public void setCreationDate(String i_CreationDate)
    {
        this.mCreationDate = i_CreationDate;
    }

    public void setCreationTime(String i_CreationTime)
    {
        this.mCreationTime = i_CreationTime;
    }

    public void setSubjectName(String i_SubjectName)
    {
        this.mSubjectName = i_SubjectName;
    }

    public void setSubjectId(String i_SubjectId)
    {
        this.mSubjectId = i_SubjectId;
    }

    public String getCreatorId(){return this.mChatRoomCreator;}


}