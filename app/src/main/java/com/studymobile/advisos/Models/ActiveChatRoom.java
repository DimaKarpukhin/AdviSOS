package com.studymobile.advisos.Models;

public class ActiveChatRoom
{
    private String mChatRoomId;
    private String mUserId;
    private boolean mIsCreator;

    public ActiveChatRoom(){}

    public ActiveChatRoom(String i_ChatRoomId, String i_UserId, boolean i_IsCreator)
    {
        this.setUserId(i_UserId);
        this.setChatRoomId(i_ChatRoomId);
        this.setIsCreator(i_IsCreator);
    }

    public String getChatRoomId()
    {
        return mChatRoomId;
    }

    public boolean getIsCreator() {
        return mIsCreator;
    }

    public String getUserId()
    {
        return mUserId;
    }


    public void setChatRoomId(String i_ChatRoomId)
    {
        this.mChatRoomId = i_ChatRoomId;
    }

    public void setIsCreator(boolean i_IsCreator) {
        mIsCreator = i_IsCreator;
    }

    public void setUserId(String i_UserId)
    {
        this.mUserId = i_UserId;
    }
}
