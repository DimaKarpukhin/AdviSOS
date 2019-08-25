package com.studymobile.advisos.Models;

public class ChatMessage
{
    private String mMessageBody;
    private int mMessageId;
    private String mSenderName;
    private String mSentTime;

    public ChatMessage(){}

    public ChatMessage(String i_MessageBody, int i_MessageId, String i_SenderName, String i_SentTime)
    {
        this.mMessageBody = mMessageBody;
        this.mMessageId = i_MessageId;
        this.mSenderName = i_SenderName;
        this.mSentTime = i_SentTime;
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
        return mSentTime;
    }

    public void SetMessageBody(String i_MessageBody)
    {
        this.mMessageBody = i_MessageBody;
    }

    public void SetMessageId(int i_MessageId)
    {
        this.mMessageId = i_MessageId;
    }

    public void setSenderName(String i_SenderName)
    {
        this.mSenderName = mSenderName;
    }

    public void setTimeSent(String i_SentTime)
    {
        this.mSentTime = i_SentTime;
    }
}

