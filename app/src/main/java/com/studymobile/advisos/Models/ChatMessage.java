package com.studymobile.advisos.Models;

public class ChatMessage
{
    private String mMessageBody;
    //private int mMessageId;
    private String mSenderName;
    private String mTimeSent;
    private String mMessageSenderID;

    public ChatMessage(){}

    public ChatMessage(String i_MessageBody, String i_SenderName, String i_TimeSent, String i_messageSenderId)
    {
        this.setMessageBody(i_MessageBody);
        //this.setMessageId(i_MessageId);
        this.setSenderName(i_SenderName);
        this.setTimeSent(i_TimeSent);
        mMessageSenderID = i_messageSenderId;
    }

    public void setmMessageSenderID(String mMessageSenderID) {
        this.mMessageSenderID = mMessageSenderID;
    }

    public String getmMessageSenderID() {
        return mMessageSenderID;
    }

    public String getMessageBody()
    {
        return mMessageBody;
    }

    /*public int getMessageId()
    {
        return mMessageId;
    }*/

    public String getSenderName()
    {
        return mSenderName;
    }

    public String getTimeSent()
    {
        return mTimeSent;
    }

    public void setMessageBody(String i_MessageBody)
    {
        this.mMessageBody = i_MessageBody;
    }

    /*public void setMessageId(int i_MessageId)
    {
        this.mMessageId = i_MessageId;
    }*/

    public void setSenderName(String i_SenderName)
    {
        this.mSenderName = mSenderName;
    }

    public void setTimeSent(String i_SentTime)
    {
        this.mTimeSent = i_SentTime;
    }
}

