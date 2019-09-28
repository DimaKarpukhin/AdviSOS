package com.studymobile.advisos.Models;

public class ChatRoom
{
    private String mRoomId,mImgLink;
    private String mChatRoomCreatorUid;
    private String mRoomName;
    private String mCreationDate;
    private String mCreationTime;
    private String mSubjectName;
    private int mNumOfParticipants;
    private Boolean mIsChatClosed = false;



    public ChatRoom(){}

    public ChatRoom(String i_RoomId, String i_RoomName, String i_CreationDate,
                    String i_CreationTime, String i_SubjectName,
                     String i_creator,  String i_ImgLink, int i_NumOfParticipants)
    {
        this.mImgLink = i_ImgLink;

        this.setRoomId(i_RoomId);
        this.setRoomName(i_RoomName);
        this.setCreationDate(i_CreationDate);
        this.setCreationTime(i_CreationTime);
        this.setSubjectName(i_SubjectName);
        this.mChatRoomCreatorUid = i_creator;
        this.setNumOfParticipants(i_NumOfParticipants);

    }

    public void setNumOfParticipants(int i_NumOfParticipants)
    {
        mNumOfParticipants = i_NumOfParticipants;
    }

    public int getNumOfParticipants()
    {
        return mNumOfParticipants;
    }

    public void setImgLink(String i_ImgLink)
    {
        mImgLink = i_ImgLink;
    }

    public String getImgLink()
    {
        return mImgLink;
    }

    public String getRoomId()
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

    public void setRoomId(String i_RoomId)
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

    public String getCreatorId(){return this.mChatRoomCreatorUid;}

    public void setmIsChatClosed(Boolean mIsChatClosed) {
        this.mIsChatClosed = mIsChatClosed;
    }

    public Boolean getmIsChatClosed() {
        return mIsChatClosed;
    }
}
