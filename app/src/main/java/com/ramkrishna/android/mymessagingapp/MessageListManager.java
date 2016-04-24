package com.ramkrishna.android.mymessagingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by ramkr on 23-Apr-16.
 * Manages the creation and updation of the messages list in a separate thread from the UI thread
 */
public class MessageListManager extends AsyncTask {

    //List to store messages using the objects of MessageItem class
    private ArrayList<MessageItem> messageList;
    //The reference of the RecyclerView from the layout
    private RecyclerView messageRecyclerView;
    //Reference to hold the DBCursorManager object
    private DBCursorManager dbCursorManager;
    //The context of the activity
    private Context context;
    //Holds the Cursor object returned by the ContentResolver in the DBCursorManager
    private Cursor smsCursor;
    //The enum that defines the type of query needed for retrieving the messages
    private DBCursorManager.QueryType queryType;
    //String refernce to hold the sender name and the search keyword(from the SearchView) respectively
    private String querySender, querySearch;
    //Holds the Adapter responsible for Updating the items in the RecyclerView
    private RecyclerViewAdapter recyclerAdapter;

    /**
     * The constructor of this class
     * @param context
     * @param queryType
     * @param querySender
     * @param querySearch
     */
    MessageListManager(Context context, DBCursorManager.QueryType queryType, String querySender, String querySearch)
    {
        this.context = context;
        this.queryType = queryType;
        this.querySender = querySender;
        this.querySearch = querySearch;
    }

    /**
     * Method that runs before starting the separate thread. Used for initialisations
     */
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        dbCursorManager = new DBCursorManager();

        messageList = new ArrayList<MessageItem>();
        messageRecyclerView = (RecyclerView) ((Activity) context).findViewById(R.id.message_recycler_view);
    }

    /**
     * Method that starts a seperate thread for execution.
     * @param params
     * @return
     */
    @Override
    protected Object doInBackground(Object[] params)
    {
        /*Depending on the QueryType that was passed in the constructor,
        the appropriate method is called to get the associated cursor and populate the List of messages*/
        switch (queryType)
        {
            case NORMAL:
                cursorAllMessages();
                break;
            case BY_SEARCH:
                cursorMessagesBySearch(querySearch);
                break;
            case BY_SENDER:
                cursorMessageBySender(querySender);
                break;
            case GROUP_BY_SENDER:
                cursorMessagesGroupBySender();
                break;
            case BY_SENDER_SEARCH:
                cursorMessagesBySenderSearch(querySender, querySearch);
            default:
        }
        return null;
    }

    /**
     * Method called after the execution thread finished.
     * Here we update the UI with the messages from the List of Messages
     * @param o
     */
    @Override
    protected void onPostExecute(Object o)
    {
        super.onPostExecute(o);
        UpdateRecyclerView();
    }

    /**
     * Methods that are called to retrieve cursor and populate Message list based on QueryType
     */
    private void cursorAllMessages()
    {
        smsCursor = dbCursorManager.getCursor(context, DBCursorManager.QueryType.NORMAL, null, null);
        populateListWithMessage(smsCursor);
    }

    private void cursorMessageBySender(String sender)
    {
        smsCursor = dbCursorManager.getCursor(context, DBCursorManager.QueryType.BY_SENDER, sender, null);
        populateListWithMessage(smsCursor);
    }

    private void cursorMessagesBySearch(String searchKey)
    {
        smsCursor = dbCursorManager.getCursor(context, DBCursorManager.QueryType.BY_SEARCH, null, searchKey);
        populateListWithMessage(smsCursor);
    }

    private void cursorMessagesGroupBySender()
    {
        smsCursor = dbCursorManager.getCursor(context, DBCursorManager.QueryType.GROUP_BY_SENDER, null, null);
        populateListWithMessage(smsCursor);
    }

    private void cursorMessagesBySenderSearch(String sender, String search)
    {
        smsCursor = dbCursorManager.getCursor(context, DBCursorManager.QueryType.BY_SENDER_SEARCH, sender, search);
        populateListWithMessage(smsCursor);
    }

    /**
     * Method to update the items in RecyclerView
     */
    private void UpdateRecyclerView()
    {
        //calls ans sets the RecyclerViewAdapter by passing the message list, the activity context and the search keyword
        recyclerAdapter = new RecyclerViewAdapter(messageList, context, querySearch);
        messageRecyclerView.setAdapter(recyclerAdapter);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        //Registering for listening to touch event on the items of the RecyclerView
        recyclerAdapter.SetOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {

            /**
             * Action to take when an item is clicked.
             *
             * Checks which activity, the RecyclerView is present in and the takes action accordingly:
             * 1) When the activity is the MessageListActivity, an Intent to launch MessagesBySenderActivity is called.
             *    This activity displays all the messages sent by the sender which is present in the current item that was touched.
             *
             * 2) When the activity is MessagesBySenderActivity, the on touching any item, this activity will be closed
             *    and taken back to the previous activity
             * @param view
             * @param position
             */
            @Override
            public void onItemClick(View view, int position)
            {
                if (!messageList.isEmpty() && ((Activity) context).getClass().getName().equals(MessageListActivity.class.getName()))
                {
                    Intent intent = new Intent(context, MessagesBySenderActivity.class);
                    intent.putExtra(Constants.INTENT_EXTRA_SENDER, messageList.get(position).getMessageSender());
                    context.startActivity(intent);
                } else if (((Activity) context).getClass().getName().equals(MessagesBySenderActivity.class.getName()))
                {
                    ((Activity) context).finish();
                }
            }
        });
    }

    /**
     * Method that takes cursor as parameter and uses it to add items in the list.
     * @param cursor
     * @return
     */
    private ArrayList<MessageItem> populateListWithMessage(Cursor cursor)
    {
        messageList.clear();
        String messageSender;
        String messageDate;
        String messageBody;
        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                messageSender = cursor.getString(cursor.getColumnIndex(Constants.COL_ADDR));
                messageDate = cursor.getString(cursor.getColumnIndex(Constants.COL_DATE));
                messageBody = cursor.getString(cursor.getColumnIndex(Constants.COL_MSG_BODY));

                MessageItem item = new MessageItem(messageSender, messageDate, messageBody);
                messageList.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return messageList;
    }
}
