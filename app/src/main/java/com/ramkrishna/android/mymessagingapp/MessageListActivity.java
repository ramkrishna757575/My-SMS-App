package com.ramkrishna.android.mymessagingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SearchView;

/**
 * Created by ramkr on 23-Apr-16.
 * <p/>
 * The launcher activity class. Shows all received messages grouped by sender.
 */
public class MessageListActivity extends AppCompatActivity implements UpdateRecyclerViewMessages {

    //Permission request code to get the sms read - write permissions
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    //The key used to store the search keyword in the bundle
    private final String searchKeyState = "searchKey";
    //Reference to hold the SearchView from the layout
    private SearchView searchInMessage;
    //Reference to the SMSDatabaseObserver instance.
    private SMSDatabaseObserver smsDatabaseObserver;

    //Activity Lifecycle Method OnCreate. Used to initialize activity parameters and inflate layout.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get the SearchView in the reference.
        searchInMessage = (SearchView) findViewById(R.id.search_message);
        //Set SMS related permissions before beginning any SMS operations like send or receive. Required from Android API 23.
        setSMSPermission();

        //Register to listen for changes in the query text of the SearchView
        searchInMessage.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //Action to take when submit button pressed on the keypad
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            //Action to take when query text changed
            @Override
            public boolean onQueryTextChange(String newText)
            {
                /*If query text is not empty then display results based on query text
                *search else display default result i.e. received messages grouped by sender
                * */
                if (!newText.isEmpty())
                {
                    showMessagesBySearch(newText);
                } else
                {
                    showMessagesGroupBySender();
                }
                return false;
            }
        });

        /**
         * Retrieve the saved search keyword on screen configuration change(E.g. screen rotation)
         * and then display the result accordingly
         */
        if (savedInstanceState != null)
        {
            String searchKey = savedInstanceState.getString(searchKeyState);
            if (!searchKey.isEmpty())
            {
                searchInMessage.setQuery(searchKey, true);
                searchInMessage.setIconified(false);
                showMessagesBySearch(searchKey);
            } else
            {
                showMessagesGroupBySender();
            }
        } else
        {
            showMessagesGroupBySender();
        }
    }

    /*
    *Activity Lifecycle Method OnResume. Called when activity comes into focus.
    * Here we are registering for listening to changes in the sms database using the SMSDatabaseObserver class.
    */
    @Override
    protected void onResume()
    {
        super.onResume();
        smsDatabaseObserver = new SMSDatabaseObserver(new Handler(Looper.getMainLooper()), this);
        Uri inboxURI = Uri.parse(Constants.URI_SMS_DB);
        this.getContentResolver().registerContentObserver(inboxURI, true, smsDatabaseObserver);
    }

    /*
    *Activity Lifecycle Method OnPause. Called when activity goes out of focus.
    * Here we are un-registering for listening to changes in the sms database using the SMSDatabaseObserver class.
    */
    @Override
    protected void onPause()
    {
        super.onPause();
        getContentResolver().unregisterContentObserver(smsDatabaseObserver);
    }

    /**
     * Called before activity's OnDestroy method. Used to store this activity's states(E.g. variables, etc.)
     * Here we are saving the search keyword
     *
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putString(searchKeyState, searchInMessage.getQuery().toString());
    }

    /**
     * Called when the New message button(present in this activity) is pressed.
     * Fires an Intent to start the NewMessageActivity
     *
     * @param v
     */
    public void createNewMessage(View v)
    {
        Intent intent = new Intent(this, NewMessageActivity.class);
        startActivity(intent);
    }

    /**
     * Checks if SMS permissions are granted and if not requests for the same
     */
    private void setSMSPermission()
    {
        int hasSMSPermission = ContextCompat.checkSelfPermission(this, Manifest.permission_group.SMS);
        if (hasSMSPermission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
    }

    /**
     * Interface methods to call the MessageListManager and update the List of messages and the associated UI based on
     * query type
     */
    @Override
    public void showMessagesBySearch(String newText)
    {
        MessageListManager messageListManager = new MessageListManager(this, DBCursorManager.QueryType.BY_SEARCH, null, newText);
        messageListManager.execute();
    }

    @Override
    public void showMessagesGroupBySender()
    {
        MessageListManager messageListManager = new MessageListManager(this, DBCursorManager.QueryType.GROUP_BY_SENDER, null, null);
        messageListManager.execute();
    }

    @Override
    public void showMessagesBySender(String sender) { }

    @Override
    public void showMessagesBySenderSearch(String sender, String search) { }

    @Override
    public void showAllMessages() { }
}
