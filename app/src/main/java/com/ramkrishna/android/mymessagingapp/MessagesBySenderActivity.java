package com.ramkrishna.android.mymessagingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by ramkr on 23-Apr-16.
 * <p/>
 * This activity shows all the messages that a particular sender has sent.
 */
public class MessagesBySenderActivity extends AppCompatActivity implements UpdateRecyclerViewMessages {

    //The key used to store the search keyword in the bundle
    private final String searchKeyState = "searchKey";
    //The sender name for which the list of messages is to be shown
    private String sender;
    //The TextView showing the sender name as a heading
    private TextView sender_name;
    //Reference to hold the SearchView from the layout
    private SearchView searchInMessage;

    //Activity Lifecycle Method OnCreate. Used to initialize activity parameters and inflate layout.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_by_sender);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get the name of the sender that was sent from the calling activity via the associated intent
        Intent intent = getIntent();
        sender = intent.getStringExtra(Constants.INTENT_EXTRA_SENDER);

        sender_name = (TextView) findViewById(R.id.sender_address_text_view);
        sender_name.setText(sender);

        //Get the SearchView in the reference.
        searchInMessage = (SearchView) findViewById(R.id.search_message);

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
                *search else display default result i.e. all received messages by sender
                * */
                if (!newText.isEmpty())
                {
                    showMessagesBySenderSearch(sender, newText);
                } else
                {
                    showMessagesBySender(sender);
                }

                return false;
            }
        });

        /**
         * Retrieve the saved search keyword and the sender name on screen configuration change(E.g. screen rotation)
         * and then display the result accordingly
         */
        if (savedInstanceState != null)
        {
            String searchKey = savedInstanceState.getString(searchKeyState);
            sender = savedInstanceState.getString(Constants.INTENT_EXTRA_SENDER);
            if (!searchKey.isEmpty())
            {
                searchInMessage.setQuery(searchKey, true);
                searchInMessage.setIconified(false);
                showMessagesBySenderSearch(sender, searchKey);
            } else
            {
                showMessagesBySender(sender);
            }
        } else
        {
            showMessagesBySender(sender);
        }
    }

    /**
     * Called before activity's OnDestroy method. Used to store this activity's states(E.g. variables, etc.)
     * Here we are saving the search keyword and the sender name
     *
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putString(searchKeyState, searchInMessage.getQuery().toString());
        savedInstanceState.putString(Constants.INTENT_EXTRA_SENDER, sender);
    }

    /**
     * Interface methods to call the MessageListManager and update the List of messages and the associated UI based on
     * query type
     */
    @Override
    public void showMessagesBySearch(String newText) { }

    @Override
    public void showMessagesGroupBySender() { }

    @Override
    public void showMessagesBySender(String sender)
    {
        MessageListManager messageListManager = new MessageListManager(this, DBCursorManager.QueryType.BY_SENDER, sender, null);
        messageListManager.execute();
    }

    @Override
    public void showMessagesBySenderSearch(String sender, String search)
    {
        MessageListManager messageListManager = new MessageListManager(this, DBCursorManager.QueryType.BY_SENDER_SEARCH, sender, search);
        messageListManager.execute();
    }

    @Override
    public void showAllMessages() { }
}
