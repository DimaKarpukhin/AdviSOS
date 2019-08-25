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
        this.SetMessageBody(i_MessageBody);
        this.SetMessageId(i_MessageId);
        this.SetSenderName(i_SenderName);
        this.SetTimeSent(i_TimeSent);
    }

    public String GetMessageBody()
    {
        return mMessageBody;
    }

    public int GetMessageId()
    {
        return mMessageId;
    }

    public String GetSenderName()
    {
        return mSenderName;
    }

    public String GetTimeSent()
    {
        return mTimeSent;
    }

    public void SetMessageBody(String i_MessageBody)
    {
        this.mMessageBody = i_MessageBody;
    }

    public void SetMessageId(int i_MessageId)
    {
        this.mMessageId = i_MessageId;
    }

    public void SetSenderName(String i_SenderName)
    {
        this.mSenderName = mSenderName;
    }

    public void SetTimeSent(String i_SentTime)
    {
        this.mTimeSent = i_SentTime;
    }
}

