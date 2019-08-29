package com.studymobile.advisos.Models;

public class ChatMessage
{
    private String mMessageBody;
    private int mMessageId;
    private String mSenderName;
    private String mTimeSent;

    public ChatMessage(){}

    public ChatMessage(String i_MessageBody, int i_MessageId, String i_SenderName, String i_TimeSent)
    {
        this.setMessageBody(i_MessageBody);
        this.setMessageId(i_MessageId);
        this.setSenderName(i_SenderName);
        this.setTimeSent(i_TimeSent);
    }

    public String getMessageBody()
    {
        return mMessageBody;
    }

    public int getMessageId()
    {
        return mMessageId;
    }

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

    public void setMessageId(int i_MessageId)
    {
        this.mMessageId = i_MessageId;
    }

    public void setSenderName(String i_SenderName)
    {
        this.mSenderName = mSenderName;
    }

    public void setTimeSent(String i_SentTime)
    {
        this.mTimeSent = i_SentTime;
    }
}

