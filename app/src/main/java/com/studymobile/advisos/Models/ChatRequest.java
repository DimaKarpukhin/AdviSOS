package com.studymobile.advisos.Models;

public class ChatRequest
{
    private String mRequestId;
    private String mChatRoomId;
    private String mChatRoomName;
    private String mTopic;
    private String mChatCreatorId;
    private String mChatCreatorImgLink;
    private String mChatCreatorName;
    private String mRequestedUserId;
    private String mSubjectImgLink;

    public ChatRequest(){}

    public ChatRequest(String i_ChatRoomId, String i_ChatRoomName, String i_Topic,
                       String i_ChatCreatorId, String i_ChatCreatorImgLink, String i_ChatCreatorName) {
        this.mChatRoomId = i_ChatRoomId;
        this.mChatRoomName = i_ChatRoomName;
        this.mTopic = i_Topic;
        this.mChatCreatorId = i_ChatCreatorId;
        this.mChatCreatorImgLink = i_ChatCreatorImgLink;
        this.mChatCreatorName = i_ChatCreatorName;
    }

    public String getSubjectImgLink() {
        return mSubjectImgLink;
    }

    public void setSubjectImgLink(String i_SubjectImgLink) {
        this.mSubjectImgLink = i_SubjectImgLink;
    }

    public String getRequestId() {
        return mRequestId;
    }

    public void setRequestId(String i_RequestId) {
        this.mRequestId = i_RequestId;
    }

    public String getChatRoomId() {
        return mChatRoomId;
    }

    public void setChatRoomId(String i_ChatRoomId) {
        this.mChatRoomId = i_ChatRoomId;
    }

    public String getChatRoomName() {
        return mChatRoomName;
    }

    public void setChatRoomName(String i_ChatRoomName) {
        this.mChatRoomName = i_ChatRoomName;
    }

    public String getTopic() {
        return mTopic;
    }

    public void setTopic(String i_Topic) {
        this.mTopic = i_Topic;
    }

    public String getChatCreatorId() {
        return mChatCreatorId;
    }

    public void setChatCreatorId(String i_ChatCreatorId) {
        this.mChatCreatorId = i_ChatCreatorId;
    }

    public String getChatCreatorImgLink() {
        return mChatCreatorImgLink;
    }

    public void setChatCreatorImgLink(String i_ChatCreatorImgLink) {
        this.mChatCreatorImgLink = i_ChatCreatorImgLink;
    }

    public String getChatCreatorName() {
        return mChatCreatorName;
    }

    public void setChatCreatorName(String i_ChatCreatorName) {
        this.mChatCreatorName = i_ChatCreatorName;
    }

    public String getRequestedUserId() {
        return mRequestedUserId;
    }

    public void setRequestedUserId(String i_RequestedUserId)
    {
        this.mRequestedUserId = i_RequestedUserId;
    }
}
