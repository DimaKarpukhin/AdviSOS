package com.studymobile.advisos.Models;

import java.util.ArrayList;

public class ChatRoom
{
    private int mRoomId;
    private String mRoomName;
    private String mCreationDate;
    private String mCreationTime;
    private String mSubjectName;
    private String mSubjectId;
    private ArrayList<String> mParticipants = new ArrayList<String>();

    public ChatRoom(){}

    public ChatRoom(int i_RoomId, String i_RoomName, String i_CreationDate,
                    String i_CreationTime, String i_SubjectName,
                    String i_SubjectId, ArrayList<String> i_Participants)
    {
        this.mRoomId = i_RoomId;
        this.mRoomName = i_RoomName;
        this.mCreationDate = i_CreationDate;
        this.mCreationTime = i_CreationTime;
        this.mSubjectName = i_SubjectName;
        this.mSubjectId = i_SubjectId;
        this.mParticipants = i_Participants;
    }

    public int GetRoomId()
    {
        return mRoomId;
    }

    public String GetRoomName()
    {
        return mRoomName;
    }

    public String GetCreationDate()
    {
        return mCreationDate;
    }

    public String GetCreationTime()
    {
        return mCreationTime;
    }

    public String GetSubjectName()
    {
        return mSubjectName;
    }

    public String GetSubjectId()
    {
        return mSubjectId;
    }

    public ArrayList<String> GetParticipants()
    {
        return mParticipants;
    }

    public void SetRoomId(int i_RoomId)
    {
        this.mRoomId = i_RoomId;
    }

    public void SetRoomName(String i_RoomName)
    {
        this.mRoomName = i_RoomName;
    }

    public void SetCreationDate(String i_CreationDate)
    {
        this.mCreationDate = i_CreationDate;
    }

    public void SetCreationTime(String i_CreationTime)
    {
        this.mCreationTime = i_CreationTime;
    }

    public void SetSubjectName(String i_SubjectName)
    {
        this.mSubjectName = i_SubjectName;
    }

    public void SetSubjectId(String i_SubjectId)
    {
        this.mSubjectId = i_SubjectId;
    }

    public void setmParticipants(ArrayList<String> i_Participants)
    {
        this.mParticipants = i_Participants;
    }
}
