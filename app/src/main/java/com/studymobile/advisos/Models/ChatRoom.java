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
    private ArrayList<String> mParticipants = new ArrayList<>();

    public ChatRoom(){}

    public ChatRoom(int i_RoomId, String i_RoomName, String i_CreationDate,
                    String i_CreationTime, String i_SubjectName,
                    String i_SubjectId, ArrayList<String> i_Participants)
    {
        this.SetRoomId(i_RoomId);
        this.SetRoomName(i_RoomName);
        this.SetCreationDate(i_CreationDate);
        this.SetCreationTime(i_CreationTime);
        this.SetSubjectName(i_SubjectName);
        this.SetSubjectId(i_SubjectId);
        this.SetParticipants(i_Participants);
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

    public void SetParticipants(ArrayList<String> i_Participants)
    {
        this.mParticipants = i_Participants;
    }
}
