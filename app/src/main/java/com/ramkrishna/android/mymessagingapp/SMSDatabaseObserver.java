package com.ramkrishna.android.mymessagingapp;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

/**
 * Created by ramkr on 23-Apr-16.
 * Creates a content observer that monitors changes in the SMS database.
 */
public class SMSDatabaseObserver extends ContentObserver implements UpdateRecyclerViewMessages {

    private Context context;

    public SMSDatabaseObserver(Handler handler, Context context)
    {
        super(handler);
        this.context = context;
    }

    @Override
    public boolean deliverSelfNotifications()
    {
        return true;
    }

    @Override
    public void onChange(boolean selfChange)
    {
        this.onChange(selfChange, null);
    }

    //If database changes, then update the RecyclerView with the latest items(Particularly when new message received)
    @Override
    public void onChange(boolean selfChange, Uri uri)
    {
        showMessagesGroupBySender();
    }

    /**
     * Interface methods to call the MessageListManager and update the List of messages and the associated UI based on
     * query type
     */
    @Override
    public void showMessagesBySearch(String newText) { }

    @Override
    public void showMessagesGroupBySender()
    {
        MessageListManager messageListManager = new MessageListManager(context, DBCursorManager.QueryType.GROUP_BY_SENDER, null, null);
        messageListManager.execute();
    }

    @Override
    public void showMessagesBySender(String sender) { }

    @Override
    public void showMessagesBySenderSearch(String sender, String search) { }

    @Override
    public void showAllMessages() { }
}
