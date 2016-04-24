package com.ramkrishna.android.mymessagingapp;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;

/**
 * Created by ramkr on 22-Apr-16.
 *
 * Returns a cursor based on the query type supplied in the parameter.
 */
public class DBCursorManager {

    public Cursor getCursor(Context context, QueryType qtype, String sender, String search)
    {
        // Create Inbox URI
        Uri inboxURI = Uri.parse(Constants.URI_SMS_TABLE);
        // Get Content Resolver object, which will deal with Content Provider
        ContentResolver cr = context.getContentResolver();
        Cursor c;
        switch (qtype)
        {
            case NORMAL:
                c = cr.query(inboxURI, Constants.REQ_COLS, null, null, null);
                break;
            case GROUP_BY_SENDER:
                c = cr.query(inboxURI, Constants.REQ_COLS, Constants.GROUP_BY_ADDR, null, null);
                break;
            case BY_SEARCH:
                c = cr.query(inboxURI, Constants.REQ_COLS, Constants.SELECTION_BY_SEARCH.replaceAll("[?]", search), null, null);
                break;
            case BY_SENDER:
                c = cr.query(inboxURI, Constants.REQ_COLS, Constants.SELECTION_BY_SENDER.replace("?", sender), null, null);
                break;
            case BY_SENDER_SEARCH:
                String conditionOne = Constants.SELECTION_BY_SENDER.replace("?", sender);
                String conditionTwo = Constants.SELECTION_BY_SENDER_SEARCH.replace("?", search);
                String finalQuery = conditionOne + Constants.CONDITION_AND + conditionTwo;
                c = cr.query(inboxURI, Constants.REQ_COLS, finalQuery, null, null);
                break;
            default:
                c = null;
        }
        return c;
    }

    /*
    * Global enum to define query types
    *
    * NORMAL - returns all the records in the table
    * GROUP_BY_SENDER - returns the records grouped by sender
    * BY_SEARCH - returns the records based on the search string
    * BY_SENDER - returns all records belonging to a sender
    * BY_SENDER_SEARCH - returns all the records belonging to a sender and containing search string in message body
    * */
    public enum QueryType {NORMAL, GROUP_BY_SENDER, BY_SEARCH, BY_SENDER, BY_SENDER_SEARCH}
}
