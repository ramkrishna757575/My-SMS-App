package com.ramkrishna.android.mymessagingapp;

/**
 * Created by ramkr on 23-Apr-16.
 * Interface that provides methods to be over-ridden for displaying the messages according to the requirement
 */
public interface UpdateRecyclerViewMessages {
    void showMessagesBySearch(String newText);

    void showMessagesGroupBySender();

    void showMessagesBySender(String sender);

    void showMessagesBySenderSearch(String sender, String search);

    void showAllMessages();
}
