package com.ramkrishna.android.mymessagingapp;

/**
 * Created by ramkr on 23-Apr-16.
 *
 * A Message Item consisting of a sms data - i.e. sender name, message date, message body
 */
public class MessageItem {
    private String messageSender;
    private String messageDate;
    private String messageBody;

    MessageItem(String messageSender, String messageDate, String messageBody)
    {
        this.messageSender = messageSender;
        this.messageDate = messageDate;
        this.messageBody = messageBody;
    }

    public String getMessageSender()
    {
        return messageSender;
    }

    public String getMessageDate()
    {
        return messageDate;
    }

    public String getMessageBody() { return messageBody; }
}
